package za.co.lbnkosi.watchdog.watchdog_service;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Device admin enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Admin warning";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Device admin disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        Toast.makeText(context, "Kiosk mode enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        Toast.makeText(context, "Kiosk mode disabled", Toast.LENGTH_SHORT).show();
    }
}
