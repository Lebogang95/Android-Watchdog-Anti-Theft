package za.co.lbnkosi.watchdog.ui.accountDir.familyLink;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import za.co.lbnkosi.watchdog.R;
import za.co.lbnkosi.watchdog.ui.accountDir.FamilyLinkActivity;
import za.co.lbnkosi.watchdog.ui.navigationBar.AccountActivity;

public class AddFamilyActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText nameEditText, surnameEditText, emailEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);

        mAuth = FirebaseAuth.getInstance();
        nameEditText = findViewById(R.id.register_name);
        surnameEditText = findViewById(R.id.register_surname);
        emailEditText = findViewById(R.id.register_email);

        Button buttonAddFamily = findViewById(R.id.button_add_family);
        buttonAddFamily.setOnClickListener(v-> alertDialog2());

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddFamilyActivity.this, AccountActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
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

        assert user != null;
        db.collection("trusted_friends").document(Objects.requireNonNull(user.getEmail())).collection(emailEditText.getText().toString()).document("friend_details")
                .set(credentials)
                .addOnSuccessListener(aVoid -> {
                    Log.d("", "DocumentSnapshot successfully written!");
                    progressBar.setVisibility(View.INVISIBLE);
                    alertDialog();
                })
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your account information has been updated")
                .setTitle("Success");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_info_black_24dp);
        builder.setPositiveButton("Continue", (dialog, which) -> {
            startActivity(new Intent(AddFamilyActivity.this, FamilyLinkActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Friends and family have the ability to wipe your phone remotely. Only add trusted accounts")
                .setTitle("Warning");
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Continue", (dialog, which) -> writetoFirebase());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
