package cl.anpetrus.prueba3.services;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.User;

/**
 * Created by Petrus on 31-08-2017.
 */

public class UserService {

    public UserService(){}

    public void saveCurrentUser(String urlPhoto) {

        final CurrentUser currentUser = new CurrentUser();

        String key = EmailProcessor.sanitizedEmail(currentUser.email());
        User user = new User();
        user.setEmail(currentUser.email());
        user.setName(currentUser.getCurrentUser().getDisplayName());
        if(urlPhoto!=null)
            user.setPhoto(urlPhoto);
        user.setUid(key);

        new Nodes().user(key).setValue(user);

    }

    public void userPhoto(){

    }

}
