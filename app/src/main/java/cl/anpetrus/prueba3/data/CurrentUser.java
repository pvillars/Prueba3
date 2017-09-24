package cl.anpetrus.prueba3.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Petrus on 26-08-2017.
 */

public class CurrentUser {

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public String email() {
        return getCurrentUser().getEmail();
    }

    public String name() {
        return "@" + getCurrentUser().getDisplayName();
    }

    public String uid() {
        return currentUser.getUid();
    }

}