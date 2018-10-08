package za.co.lbnkosi.watchdog.splashscreen;

/**
 *
 */

public interface SplashScreenContract {
    interface View{
        void onPrefCheckSuccess(String message);
        void onPrefCheckFailure(String message);
    }

    interface Presenter{
        void prefCheck();
    }

    interface onPrefCheckListener{
        void onSuccess(String message);
        void onFailure(String message);
    }
}
