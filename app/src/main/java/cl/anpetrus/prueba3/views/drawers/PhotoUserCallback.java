package cl.anpetrus.prueba3.views.drawers;

/**
 * Created by Petrus on 28-08-2017.
 */

public interface PhotoUserCallback {

    void emptyPhoto();

    void photoAvailable(String url);

    void photoUpload(String url);

}
