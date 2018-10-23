package za.co.lbnkosi.watchdog.firebase.register;

import android.app.Activity;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public void validateCredentials(Activity activity, String email, String password, String confirmPassword, String name, String surname, String phoneNumber){

        if (name.equals("") || surname.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")|| phoneNumber.equals("")) {
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

        storeCredentials(activity,email,password,name,surname,phoneNumber);

    }

    @Override
    public void storeCredentials(final Activity activity, final String email, final String password, String name, String surname, String phoneNumber){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("name", name);
        credentials.put("surname", surname);
        credentials.put("email", email);
        credentials.put("phoneNumber", phoneNumber);

        db.collection("users").document(email)
                .set(credentials)
                .addOnSuccessListener(aVoid -> {
                    Log.d("", "DocumentSnapshot successfully written!");
                    performFirebaseRegistration(activity,email,password);
                })
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));

        writeDeviceDetails(email);
        writeNetworkSettings(email);
        writeSecuritySettings(email);
        writeSimDetails(email);
        writeSocialSettings(email);
        createTrustedFriendsCollection(email);
        createDevicesCollection(email);
        createFamilyLinkCollection(email);

    }

    @Override
    public void performFirebaseRegistration(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        mOnRegistrationListener.onFailure(task.getException().getMessage());
                    }else{
                        mOnRegistrationListener.onSuccess("");
                    }
                });
    }

    private void writeDeviceDetails(String email){

        String serial_number = "1",
                imei_number = "1",
                build_number = "1",
                model = "1",
                android_version = "1",
                android_version_code = "1",
                device_name = "1";


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("serial_number", serial_number);
        stats.put("imei_number", imei_number);
        stats.put("build_number",build_number);
        stats.put("model",model);
        stats.put("android_version",android_version);
        stats.put("android_version_code",android_version_code);
        stats.put("device_name",device_name);

        db.collection("user_settings").document(email).collection("settings").document("device_details")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void writeNetworkSettings(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("detect_wifi_networks", "true");

        db.collection("user_settings").document(email).collection("settings").document("network_settings")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void writeSecuritySettings(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("backup_pin", "0000");
        stats.put("fingerprint_unlock", "no");
        stats.put("keep_me_signed_in", "no");
        stats.put("pin", "0000");
        stats.put("travel_mode", "enabled");

        db.collection("user_settings").document(email).collection("settings").document("security_settings")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void writeSocialSettings(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("friend_link", "disabled");
        stats.put("trusted_friends", "disabled");
        stats.put("family_tracking","true");

        db.collection("user_settings").document(email).collection("settings").document("social_settings")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void writeSimDetails(String email){
        String imei_number = "1234";
        String phone_number = "1234567890";

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("imei_number", imei_number);
        stats.put("phone_number", phone_number);

        db.collection("user_settings").document(email).collection("settings").document("sim_details")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void createTrustedFriendsCollection(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("email", "demo");
        stats.put("phone_number", "demo");
        stats.put("name", "demo");

        db.collection("trusted_friends").document(email).collection("demo1").document("friend_details")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void createFamilyLinkCollection(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("email", "demo");
        stats.put("phone_number", "demo");
        stats.put("name", "demo");

        db.collection("devices").document(email).collection("family_link").document("demo1")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void createDevicesCollection(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("email", "demo");
        stats.put("phone_number", "demo");
        stats.put("name", "demo");

        db.collection("devices").document(email).collection("devices").document("demo1")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

    private void createTrackingCollection(String email){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> stats = new HashMap<>();
        stats.put("email", "demo");
        stats.put("phone_number", "demo");
        stats.put("name", "demo");

        db.collection("tracking").document(email).collection("lebo@applord").document("details")
                .set(stats)
                .addOnSuccessListener(aVoid -> Log.d("", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("", "Error writing document", e));
    }

}
