package cl.anpetrus.prueba3.services;

import com.google.firebase.database.DatabaseReference;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;

/**
 * Created by Petrus on 31-08-2017.
 */

public class EventService {

    private DatabaseReference reference;
    private CurrentUser currentUser;

    public EventService() {
        reference = new Nodes().events();
        currentUser = new CurrentUser();
    }


    public void saveEvent(Event event) {
        //event.setUidUser(currentUser.uid());
        if (event.getKey() == null) {
            String key = reference.push().getKey();
            event.setKey(key);
        }
        new Nodes().event(event.getKey()).setValue(event);

    }

}
