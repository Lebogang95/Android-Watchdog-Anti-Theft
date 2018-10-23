package za.co.lbnkosi.watchdog.ui.introslider;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import za.co.lbnkosi.watchdog.R;

public class IntroSliderActivity4 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introslider4);

        Button button = findViewById(R.id.adminbutton);
        button.setOnClickListener(v->{
            DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mAdminName = new ComponentName(this, DeviceAdmin.class);

            if(mDPM != null &&mDPM.isAdminActive(mAdminName)) {
                // admin active
            }
            else {
                startActivity(new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings")));
            }
        });

        ImageView imageView = findViewById(R.id.next_activity);
        imageView.setOnClickListener(v->{
            startActivity(new Intent(IntroSliderActivity4.this, IntroSliderActivity5.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
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
