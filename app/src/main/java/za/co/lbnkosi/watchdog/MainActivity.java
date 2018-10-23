package za.co.lbnkosi.watchdog;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import za.co.lbnkosi.watchdog.ui.base.BaseActivity;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.AboutActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.AccountActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.ConfigureActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.SettingsActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        ImageView imageView = findViewById(R.id.infoicon);
        imageView.setOnClickListener(v->{
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.cardviewAccount), "Hi there", "This is just the home screen. If you're finding it difficult to configure the app then just hit the configure button then help")
                            // All options below are optional
                            .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .targetCircleColor(R.color.white)   // Specify a color for the target circle
                            .titleTextSize(20)                  // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.white)      // Specify the color of the title text
                            .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                            .textColor(R.color.textColorWhite)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.blueGrey)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional

                        }
                    });
        });

        SmoothProgressBar smoothProgressBar = findViewById(R.id.progress_bar);

        CardView cardAccount = findViewById(R.id.cardviewAccount);
        cardAccount.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardConfigure = findViewById(R.id.cardviewConfigure);
        cardConfigure.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, ConfigureActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardSettings = findViewById(R.id.cardviewSettings);
        cardSettings.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardDonate = findViewById(R.id.cardviewDonate);
        cardDonate.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardAbout = findViewById(R.id.cardviewAbout);
        cardAbout.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

    }

    @Override
    public void onUploadSuccess(String message) {

    }

    @Override
    public void onUploadFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
