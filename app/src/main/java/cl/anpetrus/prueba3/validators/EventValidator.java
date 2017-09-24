package cl.anpetrus.prueba3.validators;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cl.anpetrus.prueba3.callbacks.EventCallback;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
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

    public void loadEvent(String keyEvent) {
        new Nodes().event(keyEvent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                callback.loadEvent(event);
                String uidEvent = event.getUidUser();
                String uidCurrentUser = EmailProcessor.sanitizedEmail(new CurrentUser().email());
                if (uidEvent.equals(uidCurrentUser)) {
                    callback.showEdit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}