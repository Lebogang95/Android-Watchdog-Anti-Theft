package za.co.lbnkosi.watchdog.utils.imageUploader;

import android.app.Activity;
import android.widget.ImageView;

/**
 *
 */

public class ImageUploaderPresenter implements ImageUploaderContract.Presenter, ImageUploaderContract.onUploadListener {
    private ImageUploaderContract.View mUploadView;
    private ImageUploaderInteractor mUploadInteractor, mUpdateProfilePictureOnline, mUpdateProfilePictureOffline;

    public ImageUploaderPresenter(ImageUploaderContract.View mUploadView){
        this.mUploadView = mUploadView;
        mUploadInteractor = new ImageUploaderInteractor(this);
        mUpdateProfilePictureOnline = new ImageUploaderInteractor(this);
        mUpdateProfilePictureOffline = new ImageUploaderInteractor(this);

    }

    @Override
    public void upload(Activity activity, String email, String password) {
        mUploadInteractor.performImageUpload(activity, email, password);

    }

    @Override
    public void performUpdateProfileOnline (Activity activity, ImageView imageView){
        mUpdateProfilePictureOnline.updateProfilePictureOnline(activity, imageView);
    }

    @Override
    public void performUpdateProfileOffline(Activity activity, ImageView imageView){
        mUpdateProfilePictureOffline.updateProfilePictureOffline(activity, imageView);
    }

    @Override
    public void onSuccess(String message) {
        mUploadView.onUploadSuccess(message);

    }

    @Override
    public void onFailure(String message) {
        mUploadView.onUploadFailure(message);

    }
}
