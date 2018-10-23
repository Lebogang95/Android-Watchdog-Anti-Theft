package za.co.lbnkosi.watchdog.ui.introslider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.danimahardhika.cafebar.CafeBar;
import com.danimahardhika.cafebar.CafeBarTheme;
import com.hzn.lib.EasyTransition;

import za.co.lbnkosi.watchdog.R;

public class IntroSliderActivity3 extends AppCompatActivity {
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introslider3);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.REORDER_TASKS,
                Manifest.permission.USE_FINGERPRINT,
                Manifest.permission.USE_BIOMETRIC,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.EXPAND_STATUS_BAR,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.CAMERA,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission_group.LOCATION,
                Manifest.permission_group.MICROPHONE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ANSWER_PHONE_CALLS,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission_group.SMS,
        };

        Button button = findViewById(R.id.permissionsbutton);
        button.setOnClickListener(v ->{

            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
            else {
                CafeBar.Builder builder = new CafeBar.Builder(IntroSliderActivity3.this)
                        .content("All Permissions have been granted")
                        .floating(true)
                        .contentTypeface("droidsanschinese.ttf")
                        .showShadow(false)
                        .duration(2000);
                builder.theme(CafeBarTheme.DARK);
                CafeBar cafeBar = builder.build();
                cafeBar.show();
            }
        });

        ImageView imageView = findViewById(R.id.next_activity);
        imageView.setOnClickListener(v->{
            startActivity(new Intent(IntroSliderActivity3.this, IntroSliderActivity4.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
