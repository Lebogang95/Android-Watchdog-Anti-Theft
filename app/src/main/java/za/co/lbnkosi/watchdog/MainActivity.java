package za.co.lbnkosi.watchdog;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import za.co.lbnkosi.watchdog.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private String[] title = {
            "Account",
            "Configure",
            "Settings",
            "Donate",
            "About"};

    private String[] description = {
            "Delete your account, sign out",
            "Make changes to Watchdog",
            "Make changes to the app",
            "Some coffee would be nice",
            "Find out more about the app"
    };

    private int[] icon = {
            R.drawable.ic_account_box_black_24dp,
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_settings_black_24dp,
            R.drawable.ic_monetization_on_black_24dp,
            R.drawable.ic_info_black_24dp
    };

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatusBar();

        mAuth = FirebaseAuth.getInstance();
        String name = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        TextView textView = findViewById(R.id.nameTextview);
        textView.setText(name);

        createMainMenu(R.id.testRecycler,title,description,icon);

        ImageView imageView = findViewById(R.id.profilePic);

        imageView.setOnClickListener(v-> {
            showLoadingScreen();
            uploadProfilePicture();
        });

        setProfilePicture(imageView);
        serviceChecker(R.id.statusTextView);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
