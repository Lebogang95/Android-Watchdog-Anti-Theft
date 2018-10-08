package za.co.lbnkosi.watchdog.utils.imageUploader;

import android.app.Activity;
import android.widget.ImageView;

public interface ImageUploaderContract {

    interface View{
        void onUploadSuccess(String message);
        void onUploadFailure(String message);
    }

    interface Presenter{
        void upload(Activity activity, String email, String password);
        void performUpdateProfileOnline(Activity activity, ImageView imageView);
        void performUpdateProfileOffline(Activity activity, ImageView imageView);
    }

    interface Intractor{
        void performImageUpload(Activity activity, String email, String password);

        void updateProfilePictureOnline(Activity activity, ImageView imageView);

        void updateProfilePictureOffline(Activity activity, ImageView imageView);
    }

    interface onUploadListener{
        void onSuccess(String message);
        void onFailure(String message);
    }

}
