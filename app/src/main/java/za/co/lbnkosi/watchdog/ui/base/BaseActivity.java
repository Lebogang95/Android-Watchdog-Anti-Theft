package za.co.lbnkosi.watchdog.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.MvpView.Base;
import za.co.lbnkosi.watchdog.utils.imageUploader.ImageUploaderContract;
import za.co.lbnkosi.watchdog.utils.imageUploader.ImageUploaderPresenter;


public abstract class BaseActivity extends AppCompatActivity
        implements Base, NavigationView.OnNavigationItemSelectedListener, ImageUploaderContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

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
    public void setProfilePicture(ImageView imageView){

        ImageUploaderPresenter imageUploaderPresenter = new ImageUploaderPresenter(this);

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
}
