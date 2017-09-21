package cl.anpetrus.prueba3.validators;

import android.graphics.Bitmap;

import java.util.Date;

import cl.anpetrus.prueba3.callbacks.ActionEventCallback;
import cl.anpetrus.prueba3.data.MyDate;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.views.events.ActionEventActivity;

/**
 * Created by Petrus on 08-09-2017.
 */

public class ActionEventValidator {

    private ActionEventCallback callback;
    private String errorMessage;

    public ActionEventValidator(ActionEventCallback callback) {
        this.callback = callback;
    }

    public void saveOrUpdate(Event event, Bitmap photo, String action, boolean withNewPhoto) {

        if (isValidData(event, photo, withNewPhoto)) {
            if (action.equals(ActionEventActivity.ID_ACTION_NEW)) {
                callback.saveEvent();
            } else {
                callback.updateEvent(withNewPhoto);
            }
        } else {
            callback.errorMessage(errorMessage);
        }
    }


    private boolean isValidData(Event event, Bitmap photo, boolean withNewPhoto) {
        if (event.getName().trim().length() > 0) {
            if (event.getDescription().trim().length() > 0) {
                if (MyDate.toDate(event.getStart()).after(new Date())) {
                    if (withNewPhoto) {
                        if (photo != null) {
                            return true;
                        } else {
                            errorMessage = "Favor agrega una imagen";
                        }
                    }else{
                        return true;
                    }
                } else {
                    errorMessage = "Favor ingresar fecha y hora posterior a la actual";
                }
            } else {
                errorMessage = "Favor ingresar descripción";
            }
        } else {
            errorMessage = "Favor ingresar nombre";
        }
        return false;
    }
}
