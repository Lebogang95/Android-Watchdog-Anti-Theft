package za.co.lbnkosi.watchdog.firebase.login;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Lebogang Nkosi
 */

public class LoginInteractor implements LoginContract.Intractor {

    private LoginContract.onLoginListener mOnLoginListener;

    LoginInteractor(LoginContract.onLoginListener onLoginListener){
        this.mOnLoginListener = onLoginListener;
    }

    // Method called to login the user
    @Override
    public void performFirebaseLogin(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mOnLoginListener.onSuccess(task.getResult().toString());
                        }
                        else {
                            mOnLoginListener.onFailure(task.getException().toString());
                        }
                    }
                });
    }

    //Method to check if the credentials are correct
    @Override
    public void validateCredentials(Activity activity, String email, String password){
        if (email.equals("")){
            mOnLoginListener.onFailure("Invalid Email");
            return;
        }

        if (!email.contains("@")){
            mOnLoginListener.onFailure("Invalid Email");
            return;
        }

        if (password.equals("")){
            mOnLoginListener.onFailure("Password is empty");
            return;
        }

        if (password.length()<6){
            mOnLoginListener.onFailure("Short password");
            return;
        }

        performFirebaseLogin(activity,email,password);

    }


}
