package za.co.lbnkosi.watchdog.ui.introslider;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;

public class IntroSliderActivity5 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introslider5);

        Button button = findViewById(R.id.adminbutton);
        button.setOnClickListener(v->{
            startActivity(new Intent(IntroSliderActivity5.this, LoginActivity.class));
            finish();

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            } else{
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }*/
        });


    }

    public class DeviceAdmin extends DeviceAdminReceiver {

        @Override
        public void onEnabled(Context context, Intent intent) {
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            return "admin_receiver_status_disable_warning";
        }

        @Override
        public void onDisabled(Context context, Intent intent) {
        }
    }

}
