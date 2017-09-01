package cl.anpetrus.prueba3.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
        String key = reference.push().getKey();
        event.setKey(key);
        new Nodes().event(key).setValue(event);

    }

    public Event getEvent(String key){
        final Event[] event = {new Event()};
        new Nodes().event(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event[0] = (Event) dataSnapshot.getValue(Event.class);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return event[0];
    }
}
