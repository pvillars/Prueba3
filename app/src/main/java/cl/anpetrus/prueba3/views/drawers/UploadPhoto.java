package cl.anpetrus.prueba3.views.drawers;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.User;

/**
 * Created by Petrus on 30-08-2017.
 */

public class UploadPhoto {

    private Context context;

    public UploadPhoto(Context context) {
        this.context = context;
    }

    public void toFirebase(String path,String name){

        final CurrentUser currentUser = new CurrentUser();
        String folder = currentUser.sanitizedEmail(currentUser.email())+"/";
        String photoName = name + ".jpg";
        String baseUrl = "gs://prueba3-1df0c.appspot.com/events/";
        String refUrl = baseUrl + folder +photoName;

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(refUrl);
        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "onSuccess PHOTO", Toast.LENGTH_SHORT).show();
                @SuppressWarnings("VisibleForTests")
                String[] fullUrl = taskSnapshot.getDownloadUrl().toString().split("&token");

                String url = fullUrl[0];

            //    new PhotoPreference(context).photoSave(url);

                User user = new User();
                user.setEmail(currentUser.email());
                user.setName(currentUser.getCurrentUser().getDisplayName());
                user.setPhoto(url);
                user.setUid(currentUser.uid());

                String key = currentUser.sanitizedEmail(currentUser.email());
                //new Nodes().users().child(key).setValue(user);
                new Nodes().user(key).setValue(user);
            }
        });


    }
}
