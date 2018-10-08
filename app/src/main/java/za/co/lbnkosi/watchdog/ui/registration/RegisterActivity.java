package za.co.lbnkosi.watchdog.ui.registration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.firebase.register.RegistrationContract;
import za.co.lbnkosi.watchdog.firebase.register.RegistrationPresenter;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.ui.verification.VerificationActivity;
import za.co.lbnkosi.watchdog.utils.BlurBack;

public class RegisterActivity extends RegisterBaseActivity implements  RegistrationContract.View{

    private RegistrationPresenter mRegisterPresenter;

    private String name,surname,email,password,confirm_password,phone_number, id, bank_account,branch_code,bank_name;
    private EditText nameEditText, surnameEditText, emailEditText, passwordEditText, confirmPasswordEditText, numberEditText, idEditText,accountEditText,codeEditText, banknameEditText;
    private FirebaseAuth mAuth;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        slideAnimations();
        transparentStatusBar();

        mRegisterPresenter = new RegistrationPresenter(this);
        Button button1 = findViewById(R.id.button_register);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameEditText = findViewById(R.id.register_name);
                surnameEditText = findViewById(R.id.register_surname);
                emailEditText = findViewById(R.id.register_email);
                passwordEditText = findViewById(R.id.register_password);
                confirmPasswordEditText = findViewById(R.id.register_confirmpassword);
                numberEditText = findViewById(R.id.register_number);
                idEditText = findViewById(R.id.register_id);
                accountEditText = findViewById(R.id.register_bankaccount);
                codeEditText = findViewById(R.id.register_branchcode);
                banknameEditText = findViewById(R.id.register_bankname);

                name = nameEditText.getText().toString();
                surname = surnameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirm_password = confirmPasswordEditText.getText().toString();
                phone_number = numberEditText.getText().toString();
                id = idEditText.getText().toString();
                bank_account = accountEditText.getText().toString();
                branch_code = codeEditText.getText().toString();
                bank_name = banknameEditText.getText().toString();

                showLoadingScreen();
                initRegistration(name,surname,email,password,confirm_password,phone_number,id,bank_name,branch_code,bank_account);
            }
        });

        imageView = findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.background14);
        Bitmap blurredBitmap = BlurBack.blur( this, bm );
        imageView.setImageBitmap(blurredBitmap);

        final EditText editTextCP = findViewById(R.id.register_confirmpassword);
        final EditText editTextEmail = findViewById(R.id.register_email);
        final EditText editTextNumber = findViewById(R.id.register_number);
        final EditText editTextID = findViewById(R.id.register_id);

        editTextCP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                confirm_password = editTextCP.getText().toString();
                final EditText editTextPS = findViewById(R.id.register_password);
                final Drawable drawable1;

                password = editTextPS.getText().toString();

                if (!confirm_password.equals(password)) {
                    editTextCP.setBackgroundResource(R.drawable.edittext_background3);
                }
                else {
                   editTextCP.setBackgroundResource(R.drawable.edittext_background2);
                }

            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editTextEmail.getText().toString();
                if (!email.contains("@") || !email.contains(".")){
                    editTextEmail.setBackgroundResource(R.drawable.edittext_background3);
                }
                else {
                    editTextEmail.setBackgroundResource(R.drawable.edittext_background2);
                }

            }
        });

    }

    @Override
    public void onRegistrationSuccess(String message) {
        hideLoadingScreen();
        startActivity(new Intent(RegisterActivity.this, VerificationActivity.class));
        finish();
    }

    @Override
    public void onRegistrationFailure(String message) {
        hideLoadingScreen();
        onError(message);
    }

    @Override
    public void onBackPressed(){
        showLoadingScreen();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private void validation (){

    }

    private void initRegistration(String name, String surname,String email, String password, String confirmPassword, String phonenumber, String id, String bankname, String branchCode, String bankaccount) {
        mRegisterPresenter.register(this, email, password, confirmPassword, name, surname, id, bankaccount, branchCode, bankname, phonenumber);
    }


}
