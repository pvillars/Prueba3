package cl.anpetrus.prueba3.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.User;

/**
 * Created by Petrus on 31-08-2017.
 */

public class UserService {

    public UserService(){}

    public void saveCurrentUser() {

        final CurrentUser currentUser = new CurrentUser();
        final String key = EmailProcessor.sanitizedEmail(currentUser.email());

        new Nodes().user(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);
                }else{
                    user = new User();
                }
                user.setEmail(currentUser.email());
                user.setName(currentUser.getCurrentUser().getDisplayName());
                user.setUid(key);

                new Nodes().user(key).setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void userPhoto(String key){

        new Nodes().user(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
