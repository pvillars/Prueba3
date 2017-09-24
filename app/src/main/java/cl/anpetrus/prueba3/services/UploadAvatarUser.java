package cl.anpetrus.prueba3.services;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.User;
import cl.anpetrus.prueba3.views.drawers.PhotoUserCallback;

/**
 * Created by Petrus on 30-08-2017.
 */

public class UploadAvatarUser {

    private PhotoUserCallback callback;

    public UploadAvatarUser(PhotoUserCallback context) {
        this.callback = context;
    }

    public void uploadPhotoAvatar(Bitmap photo) {

        final CurrentUser currentUser = new CurrentUser();
        String folder = new EmailProcessor().sanitizedEmail(currentUser.email()) + "/avatar/";
        String photoName = "avatar.jpg";
        String baseUrl = "gs://prueba3-1df0c.appspot.com/users/";
        String refUrl = baseUrl + folder + photoName;

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");

                String url = fullUrl[0];
                String key = new EmailProcessor().sanitizedEmail(currentUser.email());

                User user = new User();
                user.setEmail(currentUser.email());
                user.setName(currentUser.getCurrentUser().getDisplayName());
                user.setPhoto(url);
                user.setUid(key);

                new Nodes().user(key).setValue(user);
                callback.photoUpload(url);
            }

        });
    }
}