package cl.anpetrus.prueba3.services;

import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;

/**
 * Created by Petrus on 31-08-2017.
 */

public class EventService {
    public EventService(){}

    public void saveEvent(Event event) {
        final CurrentUser currentUser = new CurrentUser();

        event.setUidUser(currentUser.uid());

        String key = currentUser.sanitizedEmail(currentUser.email());
        //new Nodes().users().child(key).setValue(user);
        new Nodes().event(key).setValue(event);

    }
}
