package za.co.lbnkosi.watchdog.ui.logind;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.firebase.login.LoginContract;
import za.co.lbnkosi.watchdog.firebase.login.LoginPresenter;
import za.co.lbnkosi.watchdog.ui.registration.RegisterActivity;
import za.co.lbnkosi.watchdog.ui.verification.VerificationActivity;


/**
 * Created by Lebogang Nkosi on 17-06-2018
 */

public class LoginActivity extends LoginBaseActivity implements LoginContract.View, View.OnClickListener {

    private LoginPresenter mLoginPresenter;
    private FirebaseAuth mAuth;
    private Button buttonSignin, buttonRegister, buttonForgotPassword;
    private TextView textViewRegister;

    @BindView(R.id.login_emailedit) EditText signInEmail;
    @BindView(R.id.login_passwordedit) EditText signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //****** These methods are to make the app more appealing. Remove if users complain about performance ******//
        transparentStatusBar();
        slideAnimations();
        //******

        if (!isOnline()){
            isOffline("No network connection detected. Please connect to use Walkers and Talkers");
        }

        mAuth = FirebaseAuth.getInstance();
        mLoginPresenter = new LoginPresenter(this);

        buttonSignin = findViewById(R.id.button_signin);
        buttonRegister = findViewById(R.id.button_register);
        buttonForgotPassword = findViewById(R.id.button_forgotpassword);
        textViewRegister = findViewById(R.id.textViewRegister);

        buttonSignin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonForgotPassword.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);

    }

    // Finish this activity when a user presses the back button. This is the initial activity of the app
    @Override
    public void onBackPressed(){
        finish();
    }

    //Calls the method which checks if the credentials the user entered are correct
    private void initLogin(String email, String password) {
        Log.d("Email123456", email);
        mLoginPresenter.checkCredentials(this, email, password);
    }

    //If the credentials entered are correct it automatically signs in the user and calls a method to check for verification
    @Override
    public void onLoginSuccess(String message) {
        hideLoadingScreen();
        IsEmailVerified();
    }

    //If the credentials are incorrect then it displays a dialog error. The method can be found here
    @Override
    public void onLoginFailure(String message) {
        hideLoadingScreen();
        onError(message);
    }

    //Method called by onLoginSuccess. You may modify the method but do not delete this method as it will cause the app to crash
    private void IsEmailVerified() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()){
            // If the users email has been verified, then it starts the main activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else {
            // If the users email has not been verified, it starts the verification activity
            startActivity(new Intent(LoginActivity.this, VerificationActivity.class));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_signin:
                initLogin(signInEmail.getText().toString(), signInPassword.getText().toString());
                break;
        }
    }
}
