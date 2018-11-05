package za.co.lbnkosi.watchdog.ui.accountDir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.navigationBar.AccountActivity;

public class UpdateAccountActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String name, surname,email,phone;
    private EditText nameEditText, surnameEditText, phoneEditText, emailEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_details);

        mAuth = FirebaseAuth.getInstance();

        ImageView backImageView = findViewById(R.id.backicon);
        backImageView.setOnClickListener(v->{
            startActivity(new Intent(UpdateAccountActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        nameEditText = findViewById(R.id.register_name);
        surnameEditText = findViewById(R.id.register_surname);
        phoneEditText = findViewById(R.id.register_number);
        emailEditText = findViewById(R.id.register_email);

        writetoEdits();

        Button buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(v->{
            writetoFirebase();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateAccountActivity.this, AccountActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void writetoEdits(){

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                db.collection("users").document(Objects.requireNonNull(user.getEmail())).collection("name");
            }

            DocumentReference docRef = db.collection("users").document(Objects.requireNonNull(user.getEmail()));
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                        nameEditText.setText(document.getString("name"));
                        surnameEditText.setText(document.getString("surname"));
                        phoneEditText.setText(document.getString("phoneNumber"));
                        emailEditText.setText(document.getString("email"));
                    } else {
                        Log.d("", "No such document");
                        //Toast.makeText(this, ""+task.getException(),Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            });
        }
    }



    private void writetoFirebase(){
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        Objects.requireNonNull(mAuth.getCurrentUser()).reload();
        final FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", nameEditText.getText().toString());
        credentials.put("surname", surnameEditText.getText().toString());
        credentials.put("email", emailEditText.getText().toString());
        credentials.put("phoneNumber", phoneEditText.getText().toString());

        assert user != null;
        db.collection("users").document(Objects.requireNonNull(user.getEmail()))
                .set(credentials)
                .addOnSuccessListener(aVoid -> {
                    Log.d("", "DocumentSnapshot successfully written!");
                    progressBar.setVisibility(View.INVISIBLE);
                    alertDialog();
                })
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void alertDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Your account information has been updated")
                .setTitle("Success");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_info_black_24dp);
        builder.setPositiveButton("Continue", (dialog, which) -> {
            startActivity(new Intent(UpdateAccountActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}
