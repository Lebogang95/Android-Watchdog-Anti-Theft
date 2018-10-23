package za.co.lbnkosi.watchdog.splashscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.ui.verification.VerificationActivity;
import za.co.lbnkosi.watchdog.utils.FingerprintActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isOnline()){

            // Check if user is signed in (non-null)
            FirebaseUser currentUser = mAuth.getCurrentUser();

            // Checks if there isn't a user registered to use the app
            if (currentUser == null){
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity .class));
                finish();
            }
            else {
                IsEmailVerified();
            }
        }
        else{

            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void IsEmailVerified() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()){
            // If the users email has been verified, then it starts the main activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fingerPrint();
            }
            else {
                securityPreference2();
            }
        }
        else {
            // If the users email has not been verified, it starts the verification activity
            startActivity(new Intent(SplashScreenActivity.this, VerificationActivity.class));
            finish();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void securityPreference() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            db.collection("userdata").document("preferences").collection(Objects.requireNonNull(user.getEmail())).document("security");
            }

            @SuppressLint("NewApi") DocumentReference docRef = db.collection("userdata").document("preferences").collection(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).document("security");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("", "DocumentSnapshot data: " + document.getData());
                            //name.setText(document.getString("name") + " "+ document.getString("surname"));

                            String userPr;
                            userPr = document.getString("require_authentication");

                            if (userPr.equals("true")){
                                //startActivity(new Intent(SplashScreenActivity.this, FingerprintActivity.class));
                                String userPr2;
                                userPr2 = document.getString("fingerprint_unlock");

                                if (userPr2.equals("true")){
                                    startActivity(new Intent(SplashScreenActivity.this, FingerprintActivity.class));
                                }
                                else {
                                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                }
                            }
                            else {
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            }

                        }
                    }
                }
            });
        }

    }

    public void securityPreference2() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                db.collection("userdata").document("preferences").collection(Objects.requireNonNull(user.getEmail())).document("security");
            }

            DocumentReference docRef = db.collection("userdata").document("preferences").collection(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).document("security");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("", "DocumentSnapshot data: " + document.getData());
                            //name.setText(document.getString("name") + " "+ document.getString("surname"));

                            String userPr;
                            userPr = document.getString("require_authentication");

                            if (userPr.equals("true")){
                                //startActivity(new Intent(SplashScreenActivity.this, FingerprintActivity.class));
                                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                            }
                            else {
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            }

                        }
                    }
                }
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fingerPrint(){
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        // Check whether the device has a Fingerprint sensor.
        if(!fingerprintManager.isHardwareDetected()){

            /**
             * An error message will be displayed if the device does not contain the fingerprint hardware.
             * However if you plan to implement a default authentication method,
             * you can redirect the user to a default authentication activity from here.
             * Example:
             * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
             * startActivity(intent);
             */

            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        }else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    }else{
                        securityPreference();

                    }
                }
            }
        }
    }
}
