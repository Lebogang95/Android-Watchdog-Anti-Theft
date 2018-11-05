package za.co.lbnkosi.watchdog.ui.introslider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.danimahardhika.cafebar.CafeBar;
import com.danimahardhika.cafebar.CafeBarTheme;
import za.co.lbnkosi.watchdog.R;

public class IntroSliderActivity2 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introslider2);

        CheckBox checkBox = findViewById(R.id.checkbox);

        ImageView nextButton = findViewById(R.id.next_activity);
        nextButton.setOnClickListener(v -> {
            if (checkBox.isChecked()){
                startActivity(new Intent(IntroSliderActivity2.this, IntroSliderActivity3.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            else {
                CafeBar.Builder builder = new CafeBar.Builder(IntroSliderActivity2.this)
                        .content("To continue please agree to the privacy policy presented")
                        .floating(true)
                        .contentTypeface("droidsanschinese.ttf")
                        .showShadow(false)
                        .duration(2000);

                //--- You can use custom theme in release 1.0.7
                //Text color (content and buttons) automatically set
                builder.theme(CafeBarTheme.DARK);

                CafeBar cafeBar = builder.build();
                //--- Old method
                //View v = cafeBar.getView();
                //v.setBackgroundColor(Color.parseColor("#455A64"));

                cafeBar.show();
            }

        });

        ImageView previousButton = findViewById(R.id.previous_activity);
        previousButton.setOnClickListener(v -> {
            startActivity(new Intent(IntroSliderActivity2.this, IntroSliderActivity1.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        Button button = findViewById(R.id.fullpolicy);
        button.setOnClickListener(v ->{

        });

    }
}
