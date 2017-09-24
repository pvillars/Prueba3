package cl.anpetrus.prueba3.validators;

import cl.anpetrus.prueba3.listeners.EventListener;

/**
 * Created by Petrus on 26-08-2017.
 */

public class EventListValidator {

    private EventListener listener;

    public enum TYPE_EVENTS {MY_EVENTS, SOON_EVENTS}

    public EventListValidator(EventListener listener) {
        this.listener = listener;
    }

    public void showEventList(TYPE_EVENTS type_events) {
        if (type_events == TYPE_EVENTS.MY_EVENTS) {
            listener.showMyEvents();
        } else {
            listener.showSoonEvents();
        }
    }
}