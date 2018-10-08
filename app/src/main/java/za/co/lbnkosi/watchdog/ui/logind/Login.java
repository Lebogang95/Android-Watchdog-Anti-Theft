package za.co.lbnkosi.watchdog.ui.logind;

public interface Login {

    void onError(String message);

    void isOffline(String message);

    void transparentStatusBar();

    void slideAnimations();

    void fadeAnimations();

    void showLoadingScreen();

    void hideLoadingScreen();

}
