package za.co.lbnkosi.watchdog;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.utils.Adapter1;
import za.co.lbnkosi.watchdog.utils.Constants;
import za.co.lbnkosi.watchdog.utils.Model1;
import za.co.lbnkosi.watchdog.utils.imageUploader.ImageUploaderContract;
import za.co.lbnkosi.watchdog.utils.imageUploader.ImageUploaderPresenter;
import za.co.lbnkosi.watchdog.watchdog_service.ForegroundService;

public class MainActivity extends AppCompatActivity implements ImageUploaderContract.View {

    RecyclerView mainMenu;

    private String[] title = {
            "Account",
            "Configure",
            "Settings",
            "Donate",
            "About"};

    private String[] description = {
            "Delete your account, sign out",
            "Make changes to Watchdog",
            "Make changes to the app",
            "Some coffee would be nice",
            "Find out more about the app"
    };

    private int[] icon = {
            R.drawable.ic_account_box_black_24dp,
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_settings_black_24dp,
            R.drawable.ic_monetization_on_black_24dp,
            R.drawable.ic_info_black_24dp
    };

    private static final int PICK_IMAGE_REQUEST = 234;

    private Uri filePath;

    private StorageReference fileRef;

    private ImageView imageView;

    FirebaseAuth mAuth;

    private StorageReference storageReference;

    private DatabaseReference mDatabase;

    private ImageUploaderPresenter imageUploaderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        mAuth = FirebaseAuth.getInstance();
        String name = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        TextView textView = findViewById(R.id.nameTextview);
        textView.setText(name);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        RecyclerView mRecyclerView1 = findViewById(R.id.testRecycler);

        if (mRecyclerView1 != null) { mRecyclerView1.setHasFixedSize(true); }

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        assert mRecyclerView1 != null;
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        List<Model1> itemList1 = new ArrayList<>();

        for (int i = 0; i < title.length; i++) { Model1 model1 = new Model1(title[i],description[i], icon[i]);
            itemList1.add(model1);}

        Adapter1 itemAdapter1 = new Adapter1(itemList1);
        mRecyclerView1.setAdapter(itemAdapter1);
        itemAdapter1.notifyDataSetChanged();

        String firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        storageReference = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        fileRef = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "profilePicture");

        imageView = findViewById(R.id.profilePic);

        ProgressBar progressBar = findViewById(R.id.progress_bar);

        imageView.setOnClickListener(v-> {
            progressBar.setVisibility(View.VISIBLE);
            showFileChooser();
        });

        if (isOnline()){
            secondDownloader();
        }
        else {
            thirdDownloader();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isMyServiceRunning(ForegroundService.class)){
            TextView textView1 = findViewById(R.id.statusTextView);
            textView1.setText(getString(R.string.active));
            textView1.setTextColor(getResources().getColor(R.color.q_green));
        }
        else{
            TextView textView1 = findViewById(R.id.statusTextView);
            textView1.setText(getString(R.string.disabled));
            textView1.setTextColor(getResources().getColor(R.color.statusBarColor));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showFileChooser() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        //checking if file is available
        String uploadPath = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(firebaseUser+"/"+ "profilePicture");

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //displaying success toast
                        Toast.makeText(getApplicationContext(), "Profile Picture Updated", Toast.LENGTH_LONG).show();

                        //creating the upload object to store uploaded image details
                        //Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());

                        //adding an upload to firebase database
                        String uploadId = mDatabase.push().getKey();
                        //mDatabase.child(uploadId).setValue(upload);
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        } else {
            //display an error if no file is selected
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUploadSuccess(String message) {

    }

    @Override
    public void onUploadFailure(String message) {

    }

    private void secondDownloader(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        fileRef = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "profilePicture");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(fileRef)
                .signature(new StringSignature(System.currentTimeMillis()+""))
                .into(imageView);
    }

    private void thirdDownloader(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        fileRef = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "profilePicture");

        if (isOnline()){
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(fileRef)
                    .into(imageView);
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
