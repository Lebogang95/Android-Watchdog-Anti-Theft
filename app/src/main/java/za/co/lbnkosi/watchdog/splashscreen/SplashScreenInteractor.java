package za.co.lbnkosi.watchdog.splashscreen;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenInteractor implements SplashScreenContract.Presenter {

    private static final String TAG = SplashScreenInteractor.class.getSimpleName();
    private SplashScreenContract.onPrefCheckListener mOnPrefCheckListener;
    private FirebaseAuth mAuth;
    private SplashScreenPresenter mSplashScreenPresenter;

    SplashScreenInteractor(SplashScreenContract.onPrefCheckListener onPrefCheckListener) {
        this.mOnPrefCheckListener = onPrefCheckListener;
    }

    @Override
    public void prefCheck() {

    }

}
