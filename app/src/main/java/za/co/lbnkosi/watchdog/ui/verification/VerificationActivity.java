package za.co.lbnkosi.watchdog.ui.verification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.utils.BlurBack;

public class VerificationActivity extends VerificationBaseActivity {

    private FirebaseAuth mAuth;
    private ImageView imageView;
    String firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        transparentStatusBar();
        fadeAnimations();
        mAuth = FirebaseAuth.getInstance();
        Button button1 = findViewById(R.id.button_signout);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(VerificationActivity.this, LoginActivity.class));
                finish();
            }
        });

        Button button2 = findViewById(R.id.button_resendverification);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerification();
            }
        });

        Button button3 = findViewById(R.id.button_verify);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadUser();
                IsEmailVerified();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        }

        EditText editText = findViewById(R.id.login_emailedit);
        editText.setText(firebaseUser);

        imageView = findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.background14);
        Bitmap blurredBitmap = BlurBack.blur( this, bm );
        //imageView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
        imageView.setImageBitmap(blurredBitmap);

    }

    private void sendVerification(){
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(VerificationActivity.this,
                                        "Sent verification email.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("", "sendEmailVerification", task.getException());
                                Toast.makeText(VerificationActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }

    private void IsEmailVerified() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()){
            // If the users email has been verified, then it starts the main activity
            startActivity(new Intent(VerificationActivity.this, MainActivity.class));
            finish();
        }
        else {
            // If the users email has not been verified, it starts the verification activity
            Toast.makeText(VerificationActivity.this, "Email not verified"+user.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    private void reloadUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.getCurrentUser().reload();
            user.reload();
        }
    }

}
