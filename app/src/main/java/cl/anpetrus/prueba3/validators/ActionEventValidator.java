package cl.anpetrus.prueba3.validators;

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

    public void saveOrUpdate(Event event,String imageUri, String action,boolean withNewPhoto){

        if (isValidData(event,imageUri)) {
            if (action.equals(ActionEventActivity.ID_ACTION_NEW)) {
                callback.saveEvent();
            } else {
                callback.updateEvent(withNewPhoto);
            }
        }else{
            callback.errorMessage(errorMessage);
        }
    }

    private boolean isValidData(Event event, String imageUri){
        if (event.getName().trim().length() > 0) {
            if (event.getDescription().trim().length() > 0) {
                if (MyDate.toDate(event.getStart()).after(new Date())) {
                    if (imageUri != null) {
                        return true;
                    } else {
                        errorMessage = "Favor agrega una imagen";
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
