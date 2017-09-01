package cl.anpetrus.prueba3.views.drawers;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import cl.anpetrus.prueba3.data.CurrentUser;
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
    public String toFirebase(String path,String name){

        final CurrentUser currentUser = new CurrentUser();
        String folder = currentUser.sanitizedEmail(currentUser.email())+"/";
        String photoName = name + ".jpg";
        String baseUrl = "gs://prueba3-1df0c.appspot.com/events/";
        String refUrl = baseUrl + folder +photoName;


        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");
                url = fullUrl[0];

                Event event = new Event();
                event.setName("hola primero");
                event.setDescription("jksdkflaksdjflas");
                event.setImage(url);
                event.setStart(new Date());
                new EventService().saveEvent(event);
            }
        });
        return url;
    }
}
