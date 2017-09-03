package cl.anpetrus.prueba3.views.drawers;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.services.EventService;

/**
 * Created by Petrus on 30-08-2017.
 */

public class UploadPhoto {

    private Context context;
    public UploadPhoto(Context context) {
        this.context = context;
    }
    String url;
    public String toFirebase(String path, final Event newEvent){

        final CurrentUser currentUser = new CurrentUser();
        final String userUidEmail = currentUser.sanitizedEmail(currentUser.email());
        final String key = new Nodes().events().push().getKey();

        String folder = userUidEmail +"/";
        String photoName = key + ".jpg";
        String baseUrl = "gs://prueba3-1df0c.appspot.com/events/";
        String refUrl = baseUrl + folder + photoName;


        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");
                url = fullUrl[0];
                newEvent.setImage(url);

                new EventService().saveEvent(newEvent);
            }
        });
        return url;
    }

    public String toFirebaseUpdate(String path, final Event event, boolean upDatePhoto){

        final CurrentUser currentUser = new CurrentUser();
        final String userUidEmail = currentUser.sanitizedEmail(currentUser.email());

        if(upDatePhoto) {
            String folder = userUidEmail + "/";
            String photoName = event.getKey() + ".jpg";
            String baseUrl = "gs://prueba3-1df0c.appspot.com/events/";
            String refUrl = baseUrl + folder + photoName;


            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
            storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");
                    url = fullUrl[0];

                    new EventService().saveEvent(event);
                }
            });
        }else{
            new EventService().saveEvent(event);
        }
        return url;
    }

}
