package za.co.lbnkosi.watchdog.ui.introslider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import za.co.lbnkosi.watchdog.R;

public class IntroSliderActivity1 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introslider1);

        ImageView nextButton = findViewById(R.id.next_activity);
        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(IntroSliderActivity1.this, IntroSliderActivity2.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

    }
}
