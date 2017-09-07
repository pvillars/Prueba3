package cl.anpetrus.prueba3;

/**
 * Created by USUARIO on 07-09-2017.
 */

public class MenuValidator {
    private MenuCallback callback;

    public MenuValidator(MenuCallback callback) {
        this.callback = callback;
    }

    public void closeMenu(){
        callback.closeMenu();
    }
}
