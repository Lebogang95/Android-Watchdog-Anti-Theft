package za.co.lbnkosi.watchdog.utils.imageUploader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import za.co.lbnkosi.watchdog.R;

/*
 */

public class ImageUploaderInteractor implements ImageUploaderContract.Intractor {

    private ImageUploaderContract.onUploadListener mOnUploadListener;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    String firebaseUser;

    ImageUploaderInteractor(ImageUploaderContract.onUploadListener onUploadListener){
        this.mOnUploadListener = onUploadListener;

    }

    @Override
    public void performImageUpload(Activity activity, String email, String password) {

    }

    @Override
    public void updateProfilePictureOnline(Activity activity, ImageView imageView){

       FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        String userEmail = user.getEmail();

        /*storageReference = FirebaseStorage.getInstance().getReference().child(userEmail+"/"+"proflePicture");
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .signature(new StringSignature(System.currentTimeMillis()+""))
                .into(imageView);*/
        StorageReference fileRef;
        fileRef = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "profilePicture");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String firebaseUser = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        fileRef = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "profilePicture");
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(fileRef)
                .error(R.drawable.ic_account_box_black_24dp)
                .signature(new StringSignature(System.currentTimeMillis()+""))
                .into(imageView);

    }

    @Override
    public void updateProfilePictureOffline(Activity activity, ImageView imageView){

        //checkProfile(activity, imageView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        storageReference = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "proflePicture");
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .error(R.drawable.background14)
                .signature(new StringSignature(System.currentTimeMillis()+""))
                .into(imageView);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initiateFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        storageReference = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "proflePicture");

    }

    private void checkProfile(final Activity activity, final ImageView imageView){
        storageReference.child(firebaseUser+"/"+ "proflePicture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(Uri uri) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                storageReference = FirebaseStorage.getInstance().getReference().child(firebaseUser+"/"+ "proflePicture");
                Glide.with(activity)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .error(R.drawable.background14)
                        .signature(new StringSignature(System.currentTimeMillis()+""))
                        .into(imageView);
            }
        }).addOnFailureListener(exception -> {

        });

    }


}
