package cl.anpetrus.prueba3.services;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.User;

/**
 * Created by Petrus on 31-08-2017.
 */

public class UserService {

    public UserService(){}
    public void saveCurrentUser() {

        final CurrentUser currentUser = new CurrentUser();

        User user = new User();
        user.setEmail(currentUser.email());
        user.setName(currentUser.getCurrentUser().getDisplayName());
        // user.setPhoto(url);
        user.setUid(currentUser.uid());
        String key = currentUser.sanitizedEmail(currentUser.email());

        new Nodes().user(key).setValue(user);

    }
}
