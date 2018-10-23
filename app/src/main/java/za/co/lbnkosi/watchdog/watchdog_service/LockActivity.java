package za.co.lbnkosi.watchdog.watchdog_service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.utils.DemoDeviceAdminReceiver;

public class LockActivity extends AppCompatActivity {

    private View mDecorView;
    private DevicePolicyManager mDpm;
    private boolean mIsKioskEnabled = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        enableKioskMode(true);

        Button button = findViewById(R.id.button_unlock);
        button.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.pinedit);
            if (editText.getText().toString().equals("123456")){
                stopLockTask();
                startActivity(new Intent(LockActivity.this, MainActivity.class));
                finish();
            }

            else {
                editText.setText("");
                editText.setBackgroundResource(R.drawable.edittext_background2);
            }

        });

        ComponentName deviceAdmin = new ComponentName(this, DemoDeviceAdminReceiver.class);
        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!mDpm.isAdminActive(deviceAdmin)) {
            Toast.makeText(this, "Not device admin", Toast.LENGTH_SHORT).show();
        }

        if (mDpm.isDeviceOwnerApp(getPackageName())) {
            mDpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});
        } else {
            Toast.makeText(this, "Not device owner", Toast.LENGTH_SHORT).show();
        }

        mDecorView = getWindow().getDecorView();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_APP_SWITCH){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void enableKioskMode(boolean enabled) {
        try {
            if (enabled) {
                if (mDpm.isLockTaskPermitted(this.getPackageName())) {
                    startLockTask();
                    mIsKioskEnabled = true;
                } else {
                    Toast.makeText(this, "not permitted", Toast.LENGTH_SHORT).show();
                }
            } else {
                stopLockTask();
                mIsKioskEnabled = false;
            }
        } catch (Exception e) {
            // TODO: Log and handle appropriately
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
