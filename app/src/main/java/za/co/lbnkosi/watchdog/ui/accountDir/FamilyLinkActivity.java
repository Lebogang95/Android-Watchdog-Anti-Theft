package za.co.lbnkosi.watchdog.ui.accountDir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.accountDir.familyLink.AddFamilyActivity;
import za.co.lbnkosi.watchdog.ui.accountDir.familyLink.RemoveFamilyActivity;
import za.co.lbnkosi.watchdog.ui.accountDir.familyLink.ViewFamilyActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.AccountActivity;

public class FamilyLinkActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_link);

        CardView cardAddFamily = findViewById(R.id.cardviewAddFamily);
        cardAddFamily.setOnClickListener(v->{
            startActivity(new Intent(FamilyLinkActivity.this, AddFamilyActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardRemoveFamily = findViewById(R.id.cardviewRemoveFamily);
        cardRemoveFamily.setOnClickListener(v->{
            startActivity(new Intent(FamilyLinkActivity.this, RemoveFamilyActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardViewFamily = findViewById(R.id.cardviewViewFamily);
        cardViewFamily.setOnClickListener(v->{
            startActivity(new Intent(FamilyLinkActivity.this, ViewFamilyActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        ImageView backImageView = findViewById(R.id.backicon);
        backImageView.setOnClickListener(v->{
            startActivity(new Intent(FamilyLinkActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FamilyLinkActivity.this, AccountActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();

    }
}
