package cl.anpetrus.prueba3;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;

/**
 * Created by Petrus on 06-09-2017.
 */

public class EventValidator {

    private EventCallback callback;

    public EventValidator(EventCallback callback) {
        this.callback = callback;
    }

    public void loadEvent(String keyEvent){
        new Nodes().event(keyEvent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                callback.loadEvent(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
