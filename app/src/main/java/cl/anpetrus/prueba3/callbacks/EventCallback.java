package cl.anpetrus.prueba3.callbacks;

import cl.anpetrus.prueba3.models.Event;

/**
 * Created by Petrus on 06-09-2017.
 */

public interface EventCallback {
    void loadEvent(Event event);
    void showEdit();

}
