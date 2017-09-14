package cl.anpetrus.prueba3.views.drawers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;

import cl.anpetrus.prueba3.callbacks.ActionEventCallback;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.models.User;
import cl.anpetrus.prueba3.services.EventService;

/**
 * Created by Petrus on 30-08-2017.
 */

public class UploadPhoto {

    private ActionEventCallback callback;
    private Context context;
    private StorageReference storageReference;

    public UploadPhoto(Context context) {
        this.context = context;
        this.callback = (ActionEventCallback)context;
    }

    String url;

    public void toFirebase(String path){

        final CurrentUser currentUser = new CurrentUser();
        String folder = new EmailProcessor().sanitizedEmail(currentUser.email())+"/";
        String photoName = "avatar.jpg";
        String baseUrl = "gs://prueba3-1df0c.appspot.com/users/avatar/";
        String refUrl = baseUrl + folder +photoName;

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "onSuccess PHOTO", Toast.LENGTH_SHORT).show();
                @SuppressWarnings("VisibleForTests")
                String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");

                String url = fullUrl[0];

                String key = new EmailProcessor().sanitizedEmail(currentUser.email());

                User user = new User();
                user.setEmail(currentUser.email());
                user.setName(currentUser.getCurrentUser().getDisplayName());
                user.setPhoto(url);
                user.setUid(key);


                //new Nodes().users().child(key).setValue(user);
                new Nodes().user(key).setValue(user);

            }
        });


    }

    public String toFirebase(final String path,final String pathThumbs, final Event newEvent) {

        final CurrentUser currentUser = new CurrentUser();
        final String userUidEmail = EmailProcessor.sanitizedEmail(currentUser.email());
        final String key = new Nodes().events().push().getKey();

        String folder = userUidEmail + "/";
        String photoName = key + ".jpg";
        String baseUrl = "gs://prueba3-1df0c.appspot.com/events/";
        String baseUrlThumbs = "gs://prueba3-1df0c.appspot.com/events_thumbs/";
        final String refUrl = baseUrl + folder + photoName;
        final String refUrlThumbs = baseUrlThumbs + folder + photoName;


        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");
                url = fullUrl[0];
                newEvent.setImage(url);

                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrlThumbs);
                storageReference.putFile(Uri.parse(pathThumbs)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");
                        url = fullUrl[0];
                        newEvent.setImageThumbnail(url);
                        new EventService().saveEvent(newEvent);
                        callback.loadFinished();
                    }
                });


            }
        });
        return url;
    }


    public String toFirebaseUpdate(final String path, final String pathThumbs, final Event event, boolean  withNewPhoto){


        if(withNewPhoto){
            Log.d("XXX","if "+path);
            final String refUrl = event.getImage();
            final String refUrlThumbs = event.getImageThumbnail();

            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
            storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Log.d("XXX","if url "+url);

                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrlThumbs);
                    storageReference.putFile(Uri.parse(pathThumbs)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d("XXX","if url "+url);
                            new EventService().updateEvent(event);
                            callback.loadFinished();
                        }
                    });
                }
            });
        }else{
            Log.d("XXX","else "+path);
            new EventService().updateEvent(event);
            callback.loadFinished();
        }
        return url;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap = null;
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
