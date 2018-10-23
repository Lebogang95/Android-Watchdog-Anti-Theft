package za.co.lbnkosi.watchdog.ui.navigationBar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.ArrayList;
import devlight.io.library.ArcProgressStackView;
import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;

public class AccountActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("Circle", 25, R.color.colorPrimary, R.color.colorAccent));

        ImageView backImageView = findViewById(R.id.backicon);
        backImageView.setOnClickListener(v->{
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }
}
