package za.co.lbnkosi.watchdog.ui.logind;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import za.co.lbnkosi.watchdog.R;


public abstract class LoginBaseActivity extends AppCompatActivity implements Login {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    /*
    Universal error handler for the loginbaseactivity. If you want to throw an error to the user
    then use this method.
    You call this method by also providing the error or custom message you want to show them
     */

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
    public void isOffline(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Error");
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setNegativeButton(
                "Close Application",
                (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
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
    public void showLoadingScreen(){
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingScreen(){
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

    }

}
