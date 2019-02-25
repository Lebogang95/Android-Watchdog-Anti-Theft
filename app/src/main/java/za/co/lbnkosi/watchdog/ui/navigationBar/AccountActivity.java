package za.co.lbnkosi.watchdog.ui.navigationBar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.base.BaseActivity;

public class AccountActivity extends BaseActivity {

    private String[] title = {
            "Update",
            "Family Link",
            "Sign Out",
            "Delete Account"};

    private String[] description = {
            "Update your account details such as your name and profile picture",
            "Here you can add family memebers and friends as trusted members",
            "All of your settings /configurations are stored on the cloud",
            "Delete your account and information"
    };

    private int[] icon = {
            R.drawable.ic_navigate_next_black_24dp,
            R.drawable.ic_navigate_next_black_24dp,
            R.drawable.ic_error_orange_24dp,
            R.drawable.ic_warning_black_24dp,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        ImageView backImageView = findViewById(R.id.backicon);
        backImageView.setOnClickListener(v->{
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        createAccountMenu(R.id.testRecycler,title,description,icon);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }
}
