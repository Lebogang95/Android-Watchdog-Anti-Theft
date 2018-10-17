package za.co.lbnkosi.watchdog.ui.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.Objects;

import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.MvpView.Base;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.utils.BlurBack;
import za.co.lbnkosi.watchdog.utils.Constants;
import za.co.lbnkosi.watchdog.utils.CustomTypefaceSpan;
import za.co.lbnkosi.watchdog.utils.imageUploader.ImageUploaderContract;
import za.co.lbnkosi.watchdog.utils.imageUploader.ImageUploaderPresenter;


public abstract class BaseActivity extends AppCompatActivity
        implements Base, NavigationView.OnNavigationItemSelectedListener, ImageUploaderContract.View {

    NavigationView navigationView;
    private ImageUploaderPresenter imageUploaderPresenter;

    private static final int PICK_IMAGE_REQUEST = 234;
    ProgressDialog progressDialog;
    private StorageReference fileRef;
    private StorageReference storageReference2;
    //view objects
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editTextName;
    private TextView textViewShow;
    private ImageView imageView;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String uploadPath = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        imageView = findViewById(R.id.nav_imageView);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        fileRef = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "profilePicture");
        storageReference2 = FirebaseStorage.getInstance().getReference().child("default/bmwM4.jpg");

    }

    @Override
    public void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
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
    public void navDrawerFontStyle(int view){
        NavigationView navigationView = findViewById(view);

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "droidsanschinese.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void showToolBar(int view){
        Toolbar toolbar = findViewById(view);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void signOutButtonClick(int view){
        NavigationView navigationView = findViewById(view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        final Button signoutButton = headerview.findViewById(R.id.sign_out_button);

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //showProgressBar(R.id.mainactivity_progressBar);
                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    @Override
    public void onProfilePictureClick(int view){
        NavigationView navigationView = findViewById(view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        final ImageView imageView = headerview.findViewById(R.id.nav_imageView);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                Intent intent = new Intent(v.getContext(), SetupProfile.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_leads) {

//            startActivity(new Intent(this, AddLeadActivity.class));
//            finish();

        } else if (id == R.id.nav_points_redemption) {

            /*startActivity(new Intent(this, OurAppsActivity.class));
            finish();*/

        }  else if (id == R.id.nav_dashboard) {

            /*startActivity(new Intent(this, DashboardActivity.class));
            finish();*/

        } else if (id == R.id.nav_home) {


        } else if (id == R.id.nav_contact_us) {

            /*startActivity(new Intent(this, ContactUsActivity.class));
            finish();*/

        } else if (id == R.id.nav_privacy_policy){
            /*startActivity(new Intent(this, PrivacyPolicyActivity.class));
            finish();*/
        }

        else if (id == R.id.nav_settings){
            /*startActivity(new Intent(this, SettingsActivity.class));
            finish();*/
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void updateUI(int view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        NavigationView navigationView = findViewById(view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        final TextView name = headerview.findViewById(R.id.name_position_textview);
        final TextView email = headerview.findViewById(R.id.email_textview);
        final Button signoutButton1 = headerview.findViewById(R.id.sign_out_button);

        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                db.collection("Users").document(Objects.requireNonNull(user.getEmail())).collection("name");
            }
            DocumentReference docRef = db.collection("Users").document(Objects.requireNonNull(user.getEmail()));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("", "DocumentSnapshot data: " + document.getData());
                            name.setText(document.getString("name") + " "+ document.getString("surname"));
                        } else {
                            Log.d("", "No such document");
                            //Toast.makeText(this, ""+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("", "get failed with ", task.getException());
                    }
                }
            });
            email.setText(user.getEmail());
        }
        else{
            signoutButton1.setText("Disabled");
            signoutButton1.setEnabled(false);
        }
    }

    public void setNavDrawerProfile(int view){
        NavigationView navigationView = findViewById(view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        ImageView imageView = headerview.findViewById(R.id.nav_imageView);

        imageUploaderPresenter = new ImageUploaderPresenter(this);

        if(isOnline()){
            imageUploaderPresenter.performUpdateProfileOnline(this, imageView);
        }
        else {
            imageUploaderPresenter.performUpdateProfileOffline(this, imageView);
        }

    }

    @Override
    public void setProfilePicture(ImageView imageView){

        imageUploaderPresenter = new ImageUploaderPresenter(this);

        if(isOnline()){
            imageUploaderPresenter.performUpdateProfileOnline(this, imageView);
        }
        else {
            imageUploaderPresenter.performUpdateProfileOffline(this, imageView);
        }

    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void setBackground(ImageView imageView){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.background18);
        Bitmap blurredBitmap = BlurBack.blur( this, bm );
        //imageView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
        imageView.setImageBitmap(blurredBitmap);
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
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onSuccess(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Thank You");
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Continue",
                (dialog, id) -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void blurBackground(int view){
        ImageView imageView;
        imageView = findViewById(view);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.background18);
        Bitmap blurredBitmap = BlurBack.blur( this, bm );
        imageView.setImageBitmap(blurredBitmap);

    }

}
