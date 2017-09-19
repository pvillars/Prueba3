package cl.anpetrus.prueba3.callbacks;

/**
 * Created by Petrus on 08-09-2017.
 */

public interface ActionEventCallback {

    void updateEvent(boolean withNewPhoto);
    void saveEvent();
    void loadFinished(boolean withPhoto);
    void errorMessage(String message);

}
