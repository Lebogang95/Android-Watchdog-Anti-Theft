package za.co.lbnkosi.watchdog.firebase.register;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class RegistrationInteractor implements RegistrationContract.Intractor {

    private static final String TAG = RegistrationInteractor.class.getSimpleName();
    private RegistrationContract.onRegistrationListener mOnRegistrationListener;

    public RegistrationInteractor(RegistrationContract.onRegistrationListener onRegistrationListener){
        this.mOnRegistrationListener = onRegistrationListener;
    }

    @Override
    public void validateCredentials(Activity activity, String email, String password, String confirmPassword, String name, String surname, String id, String bankAccount, String branchCode, String bankName, String phoneNumber){

        if (name.equals("") || surname.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("") || id.equals("") || bankAccount.equals("") || branchCode.equals("") || bankName.equals("") || phoneNumber.equals("")) {
            mOnRegistrationListener.onFailure("Please Fill In The Empty Fields");
            return;
        }

        if (!email.contains("@")){
            mOnRegistrationListener.onFailure("Invalid Email");
            return;
        }

        // Password validations
        if (password.equals("")){
            mOnRegistrationListener.onFailure("Invalid Password");
            return;
        }

        if (password.length()<6){
            mOnRegistrationListener.onFailure("Short Password");
            return;
        }

        if (!confirmPassword.equals(password)) {
            mOnRegistrationListener.onFailure("Passwords Do Not Match");
            return;
        }

        // ID validations
        if (id.length()<13){
            mOnRegistrationListener.onFailure("ID is too short");
            return;
        }

        storeCredentials(activity,email,password,name,surname,id,phoneNumber);

    }

    @Override
    public void storeCredentials(final Activity activity, final String email, final String password, String name, String surname, String id, String phoneNumber){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", name);
        credentials.put("surname", surname);
        credentials.put("email", email);
        credentials.put("id", "7");
        credentials.put("phoneNumber", phoneNumber);

        db.collection("Users").document(email)
                .set(credentials)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                        performFirebaseRegistration(activity,email,password);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });

        final FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("availablePoints", "0");
        stats.put("totalPoints", "0");
        stats.put("usedPoints", "0");

        db2.collection("Stats").document(email).collection("user_stats").document("new_stats")
                .set(stats)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });

        final FirebaseFirestore db3 = FirebaseFirestore.getInstance();
        Map<String, Object> stats1 = new HashMap<>();
        stats1.put("availablePoints", "0");
        stats1.put("totalPoints", "0");
        stats1.put("usedPoints", "0");

        db3.collection("Stats").document(email).collection("user_stats").document("cached")
                .set(stats1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });

        final FirebaseFirestore db4 = FirebaseFirestore.getInstance();
        Map<String, Object> stats2 = new HashMap<>();
        stats2.put("fingerprint_unlock", "false");
        stats2.put("require_authentication", "false");

        db4.collection("userdata").document("preferences").collection(email).document("security")
                .set(stats2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });

        final FirebaseFirestore db5 = FirebaseFirestore.getInstance();
        Map<String, Object> stats3 = new HashMap<>();
        stats3.put("push_notifications", "false");

        db5.collection("userdata").document("preferences").collection(email).document("notification")
                .set(stats3)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                    }
                });
    }

    @Override
    public void performFirebaseRegistration(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            mOnRegistrationListener.onFailure(task.getException().getMessage());
                        }else{
                            mOnRegistrationListener.onSuccess("");
                        }
                    }
                });
    }

}
