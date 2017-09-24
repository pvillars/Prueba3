package cl.anpetrus.prueba3.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Petrus on 30-08-2017.
 */

public class Nodes {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    public DatabaseReference users() {
        return root.child("users");
    }

    public DatabaseReference events() {
        return root.child("events");
    }

    public DatabaseReference eventsList() {
        return root.child("events_list");
    }

    public DatabaseReference myEventsList() {
        return root.child("my_events_list");
    }

    public DatabaseReference user(String key) {
        return users().child(key);
    }

    public DatabaseReference event(String key) {
        return events().child(key);
    }

    public DatabaseReference eventList(String key) {
        return eventsList().child(key);
    }

    public DatabaseReference myEventList(String uid) {
        return myEventsList().child(uid);
    }

    public DatabaseReference myEventList(String uid, String key) {
        return myEventsList().child(uid).child(key);
    }

}