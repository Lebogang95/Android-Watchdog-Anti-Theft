package za.co.lbnkosi.watchdog.ui.MvpView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.ImageView;

public interface Base {
    void transparentStatusBar();

    void slideAnimations();

    void fadeAnimations();

    void navDrawerFontStyle(int view);

    void showToolBar(int view);

    void signOutButtonClick(int view);

    void onProfilePictureClick(int view);

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    void updateUI(int view);

    void setProfilePicture(ImageView imageView);

    void setBackground(ImageView imageView);

    void showLoadingScreen();

    void hideLoadingScreen();

    void onError(String message);

    void onSuccess(String message);

    void blurBackground(int view);
}
