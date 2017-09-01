package cl.anpetrus.prueba3.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Petrus on 30-08-2017.
 */

public class Nodes {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();

    public DatabaseReference users(){
        return root.child("users");
    }

    public DatabaseReference events(){
        return root.child("events");
    }

    public DatabaseReference user(String key){
        return users().child(key);
    }

    public DatabaseReference event(String key){ return events().child(key); }

  //  public Event getEventByKey(String key){    }
}
