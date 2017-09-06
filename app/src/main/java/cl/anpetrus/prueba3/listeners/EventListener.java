package cl.anpetrus.prueba3.listeners;

import cl.anpetrus.prueba3.models.Event;

/**
 * Created by USUARIO on 06-09-2017.
 */

public interface EventListener {
    void clicked(Event event);
    void dataChange();
    void showSoonEvents();
    void showMyEvents();
}
