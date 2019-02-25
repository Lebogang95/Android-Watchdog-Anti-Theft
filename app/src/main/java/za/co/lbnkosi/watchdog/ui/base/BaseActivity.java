package za.co.lbnkosi.watchdog.ui.base;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.MvpView.Base;
import za.co.lbnkosi.watchdog.ui.accountDir.FamilyLinkActivity;
import za.co.lbnkosi.watchdog.ui.accountDir.UpdateAccountActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.AboutActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.AccountActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.ConfigureActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.SettingsActivity;
import za.co.lbnkosi.watchdog.utils.RecyclerViewAdapter.Adapter.Adapter1;
import za.co.lbnkosi.watchdog.utils.Constants;
import za.co.lbnkosi.watchdog.utils.RecyclerViewAdapter.Adapter.Adapter2;
import za.co.lbnkosi.watchdog.utils.RecyclerViewAdapter.Model.Model1;
import za.co.lbnkosi.watchdog.utils.RecyclerItemClickListener;
import za.co.lbnkosi.watchdog.utils.RecyclerViewAdapter.Model.Model2;
import za.co.lbnkosi.watchdog.watchdog_service.ForegroundService;

public abstract class BaseActivity extends AppCompatActivity
        implements Base {

    private static final int PICK_IMAGE_REQUEST = 234;

    private Uri filePath;

    FirebaseAuth mAuth;

    private StorageReference storageReference;

    private DatabaseReference mDatabase;

    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        profileImageView = findViewById(R.id.profilePic);
    }

    @Override
    public void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void slideAnimations(){
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void fadeAnimations(){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void setProfilePicture(ImageView imageView){

    }

    @Override
    public void showLoadingScreen(){
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingScreen(){
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onError(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Error");
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNegativeButton(
                "Try Again",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onSuccess(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Thank You");
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Continue",
                (dialog, id) -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void createMainMenu(int view , String[] title, String[] description, int[] icon){
        RecyclerView mRecyclerView1 = findViewById(view);

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

        mRecyclerView1.addOnItemTouchListener(new RecyclerItemClickListener(this, (view_, position) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(getApplicationContext(), ConfigureActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                    break;
            }
        }));
    }

    @Override
    public void serviceChecker(int view){
        if(isMyServiceRunning(ForegroundService.class)){
            TextView textView1 = findViewById(view);
            textView1.setText(getString(R.string.active));
            textView1.setTextColor(getResources().getColor(R.color.q_green));
        }
        else{
            TextView textView1 = findViewById(view);
            textView1.setText(getString(R.string.disabled));
            textView1.setTextColor(getResources().getColor(R.color.errorColor));
        }
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

    @Override
    public void uploadProfilePicture(){
        hideLoadingScreen();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        profileImageView = findViewById(R.id.profilePic);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImageView.setImageBitmap(bitmap);
                uploadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        //checking if file is available
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
                        progressDialog.dismiss();
                        onSuccess("Profile Picture Updated");
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        onError(exception.getMessage());
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        } else {
            //display an error if no file is selected
            hideLoadingScreen();
            onError("No file selected");
        }
    }

    @Override
    public void createAccountMenu(int view, String[] title, String[] description, int[] icon){
        RecyclerView mRecyclerView1 = findViewById(view);

        if (mRecyclerView1 != null) { mRecyclerView1.setHasFixedSize(true); }

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        assert mRecyclerView1 != null;
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        List<Model2> itemList1 = new ArrayList<>();

        for (int i = 0; i < title.length; i++) { Model2 model2 = new Model2(title[i],description[i], icon[i]);
            itemList1.add(model2);}

        Adapter2 itemAdapter1 = new Adapter2(itemList1);
        mRecyclerView1.setAdapter(itemAdapter1);
        itemAdapter1.notifyDataSetChanged();

        mRecyclerView1.addOnItemTouchListener(new RecyclerItemClickListener(this, (view_, position) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(getApplicationContext(), UpdateAccountActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(getApplicationContext(), FamilyLinkActivity.class));
                    break;
                case 2:
                    warningMessage("Warning","Are you sure you want to logout","No","Yes");
                    break;
                case 3:
                    warningMessage("Warning","Are you sure you want to delete your account ? This action cannot be reversed. You will loose all your data including app preferences.",
                            "No, Return To App","Yes, I Am Sure");
                    break;
            }
        }));
    }

    private void warningMessage(String title, String message, String pButton, String nButton){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setPositiveButton(pButton, (dialog, which) -> {

        });
        builder.setNegativeButton(nButton, (dialog, which) -> {

        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}
