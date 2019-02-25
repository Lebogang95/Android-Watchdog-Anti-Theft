package za.co.lbnkosi.watchdog.ui.MvpView;

import android.widget.ImageView;

public interface Base {

    void transparentStatusBar();

    void slideAnimations();

    void fadeAnimations();

    void setProfilePicture(ImageView imageView);

    void showLoadingScreen();

    void hideLoadingScreen();

    void onError(String message);

    void onSuccess(String message);

    void createMainMenu(int view, String[] title, String[] description, int[] icon);

    void serviceChecker(int view);

    void uploadProfilePicture();

    void createAccountMenu(int view, String[] title, String[] description, int[] icon);
}
