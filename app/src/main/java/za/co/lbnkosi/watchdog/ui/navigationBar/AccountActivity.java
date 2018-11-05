package za.co.lbnkosi.watchdog.ui.navigationBar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.accountDir.FamilyLinkActivity;
import za.co.lbnkosi.watchdog.ui.accountDir.UpdateAccountActivity;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;

public class AccountActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        ImageView backImageView = findViewById(R.id.backicon);
        backImageView.setOnClickListener(v->{
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardView = findViewById(R.id.cardviewUpdate);
        cardView.setOnClickListener(v->{
            startActivity(new Intent(AccountActivity.this, UpdateAccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardView2 = findViewById(R.id.cardviewFamilyLink);
        cardView2.setOnClickListener(v->{
            startActivity(new Intent(AccountActivity.this, FamilyLinkActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardView3 = findViewById(R.id.cardviewSignOut);
        cardView3.setOnClickListener(v->{
            alertDialogLogout("Warning","Are you sure you want to logout","No","Yes");
        });

        CardView cardView4 = findViewById(R.id.cardviewDeleteAccount);
        cardView4.setOnClickListener(v-> alertDialog("Warning","Are you sure you want to delete your account ? This action cannot be reversed. You will loose all your data including app preferences.",
                "No, Return To App","Yes, I Am Sure"));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void alertDialog(String title, String message, String pButton, String nButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setPositiveButton(pButton, (dialog, which) -> {

        });
        builder.setNegativeButton(nButton, (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertDialogLogout(String title, String message, String pButton, String nButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setPositiveButton(pButton, (dialog, which) -> {

        });
        builder.setNegativeButton(nButton, (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
