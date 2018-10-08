package za.co.lbnkosi.watchdog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ms_square.etsyblur.BlurSupport;

import za.co.lbnkosi.watchdog.ui.base.BaseActivity;
import za.co.lbnkosi.watchdog.utils.BlurBack;

public class MainActivity extends BaseActivity {

    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatusBar();

//        showToolBar(R.id.generalToolbar);

        navDrawerFontStyle(R.id.nav_view);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        setNavDrawerProfile(R.id.nav_view);
        updateUI(R.id.nav_view);
        signOutButtonClick(R.id.nav_view);
        onProfilePictureClick(R.id.nav_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparentColor));
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        BlurSupport.addTo(drawerLayout);
        ImageView imageView2;
        imageView2 = findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.background5);
        Bitmap blurredBitmap = BlurBack.blur( this, bm );
        //imageView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
        imageView2.setImageBitmap(blurredBitmap);

    }

    @Override
    public void onUploadSuccess(String message) {

    }

    @Override
    public void onUploadFailure(String message) {

    }
}
