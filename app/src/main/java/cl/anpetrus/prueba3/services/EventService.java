package cl.anpetrus.prueba3.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.models.User;

/**
 * Created by Petrus on 31-08-2017.
 */

public class EventService {

    private DatabaseReference reference;

    public EventService() {
        reference = new Nodes().events();
    }

    public void saveEvent(Event event) {
        String key = reference.push().getKey();
        saveOrUpdate(event, key);
    }

    public void updateEvent(Event event) {
        final String key = event.getKey();
        saveOrUpdate(event, key);
    }

    private void saveOrUpdate(final Event event, final String key) {

        event.setName(event.getName().trim());
        if (event.getNameUser() != null)
            event.setNameUser(event.getNameUser().trim());
        event.setDescription(event.getDescription().trim());

        event.setKey(key);
        new Nodes().event(key).setValue(event);
        event.setDescription(null);
        event.setImage(null);

        new Nodes().user(event.getUidUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                event.setNameUser(user.getName());
                new Nodes().eventList(key).setValue(event);
                new Nodes().myEventList(event.getUidUser(), key).setValue(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private Event cleanData(Event event) {
        event.setName(event.getName().trim());
        event.setNameUser(event.getNameUser().trim());
        event.setDescription(event.getDescription().trim());
        return event;
    }
}