package za.co.lbnkosi.watchdog;

import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.ArrayList;
import java.util.List;
import za.co.lbnkosi.watchdog.ui.base.BaseActivity;
import za.co.lbnkosi.watchdog.utils.Adapter1;
import za.co.lbnkosi.watchdog.utils.Constants;
import za.co.lbnkosi.watchdog.utils.DemoDeviceAdminReceiver;
import za.co.lbnkosi.watchdog.utils.Model1;
import za.co.lbnkosi.watchdog.utils.RecyclerItemClickListener;
import za.co.lbnkosi.watchdog.watchdog_service.BackgroundService;
import za.co.lbnkosi.watchdog.watchdog_service.ForegroundService;
import za.co.lbnkosi.watchdog.watchdog_service.LockActivity;

public class MainActivity extends BaseActivity implements OnDSListener, OnDSPermissionsListener {

    String[] main_header1 = {"Account","Configure","Donate","Settings"};
    String[] middle_header1 = {"Manage your account","Make changes to Watchdog","Buy the developer coffee","Manage app settings"};

    final int[] banner_images1 = {
            R.drawable.accounticon
            ,R.drawable.configureicon
            ,R.drawable.donateicon
            ,R.drawable.settingsicon
    };

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    DroidSpeech droidSpeech;
    DevicePolicyManager devicePolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        RecyclerView mRecyclerView1 = findViewById(R.id.main_rec);

        if (mRecyclerView1 != null) {
            mRecyclerView1.setHasFixedSize(true);}

        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(this,2);
        assert mRecyclerView1 != null;
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        List<Model1> itemList1 = new ArrayList<>();

        for (int i = 0; i < main_header1.length; i++) {
            Model1 model1 = new Model1(main_header1[i], middle_header1[i], banner_images1[i]);
            itemList1.add(model1);}

        Adapter1 itemAdapter5 = new Adapter1(itemList1);
        mRecyclerView1.setAdapter(itemAdapter5);
        itemAdapter5.notifyDataSetChanged();

        mRecyclerView1.addOnItemTouchListener(new RecyclerItemClickListener(this, (view1, position) -> Toast.makeText(getApplicationContext(), "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show()));


        droidSpeech = new DroidSpeech(this, null);
        droidSpeech.setOnDroidSpeechListener(this);

        Button button = findViewById(R.id.appbar_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent startIntent = new Intent(getApplicationContext(), ForegroundService.class);
                startIntent.setAction(Constants.ACTION.ACTION_START_SERVICE);
                startService(startIntent);*/
                startActivity(new Intent(MainActivity.this, LockActivity.class));
            }
        });

    }

    @Override
    public void onUploadSuccess(String message) {

    }

    @Override
    public void onUploadFailure(String message) {

    }

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {

    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {

    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {

    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        TextView textView = findViewById(R.id.status);

        if (finalSpeechResult.equals("activate")){
            textView.setText(finalSpeechResult);
        }

        if (finalSpeechResult.equals("hey")){
            textView.setText(finalSpeechResult);
        }

    }

    @Override
    public void onDroidSpeechClosedByUser() {

    }

    @Override
    public void onDroidSpeechError(String errorMsg) {

    }

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny) {

    }
}
