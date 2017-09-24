package cl.anpetrus.prueba3.views.drawers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.User;

/**
 * Created by Petrus on 28-08-2017.
 */

public class PhotoUserValidation {

    private PhotoUserCallback photoCallback;

    public PhotoUserValidation(PhotoUserCallback photoCallback) {
        this.photoCallback = photoCallback;
    }

    public void validate() {
        String key = EmailProcessor.sanitizedEmail(new CurrentUser().email());

        new Nodes().user(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getPhoto() != null) {
                        photoCallback.photoAvailable(user.getPhoto());
                    } else {
                        photoCallback.emptyPhoto();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}