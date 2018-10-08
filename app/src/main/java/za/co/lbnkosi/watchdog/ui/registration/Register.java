package za.co.lbnkosi.watchdog.ui.registration;

public interface Register {
    void transparentStatusBar();

    void slideAnimations();

    void fadeAnimations();

    void onError(String message);

    void showLoadingScreen();

    void hideLoadingScreen();

    void sendVerification();
}
