package za.co.lbnkosi.watchdog.ui.logind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.lbnkosi.watchdog.MainActivity;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.firebase.login.LoginContract;
import za.co.lbnkosi.watchdog.firebase.login.LoginPresenter;
import za.co.lbnkosi.watchdog.ui.registration.RegisterActivity;
import za.co.lbnkosi.watchdog.ui.verification.VerificationActivity;
import za.co.lbnkosi.watchdog.utils.BlurBack;


/**
 * Created by Lebogang Nkosi on 17-06-2018
 */

public class LoginActivity extends LoginBaseActivity implements LoginContract.View, View.OnClickListener {

    private LoginPresenter mLoginPresenter;
    private FirebaseAuth mAuth;
    private Button buttonSignin, buttonRegister, buttonForgotPassword;
    private int BLUR_PRECENTAGE = 100;
    private String IMAGE_URL = "http://behemppy.com/hotelshivasdream.com/astrokathmandu/api/users/system.jpg";
    private StorageReference fileRef;
    private Bitmap bitmap;
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

        ImageView imageView = findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.background5);
        Bitmap blurredBitmap = BlurBack.blur( this, bm );
        //imageView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
        imageView.setImageBitmap(blurredBitmap);

    }

    // Finish this activity when a user presses the back button. This is the initial activity of the app
    @Override
    public void onBackPressed(){
        finish();
    }

    //Calls the method which checks if the credentials the user entered are correct
    private void initLogin(String email, String password) {
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
        final EditText editText = findViewById(R.id.login_emailedit);
        final EditText editText1 = findViewById(R.id.login_passwordedit);
        editText.setBackgroundResource(R.drawable.edittext_background3);
        editText1.setBackgroundResource(R.drawable.edittext_background3);
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

    @Override
    public void onClick(View v) {
        if (v == buttonSignin){
            EditText emailEditText = findViewById(R.id.login_emailedit);
            EditText passwordEditText = findViewById(R.id.login_passwordedit);

            emailEditText.setBackgroundResource(R.drawable.edittext_background3);
            passwordEditText.setBackgroundResource(R.drawable.edittext_background3);

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            showLoadingScreen();
            initLogin(email, password);
        }

        if (v == buttonRegister){
            showLoadingScreen();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }

        if (v == buttonForgotPassword){
            //startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        }

        if (v == textViewRegister){
            showLoadingScreen();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        }
    }

    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
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

}
