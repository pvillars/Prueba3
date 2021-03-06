package cl.anpetrus.prueba3.validators;

import cl.anpetrus.prueba3.callbacks.LoginCallBack;
import cl.anpetrus.prueba3.data.CurrentUser;

/**
 * Created by Petrus on 26-08-2017.
 */

public class LoginValidator {

    private LoginCallBack loginCallBack;

    public LoginValidator(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    public void init() {
        if (new CurrentUser().getCurrentUser() != null) {
            loginCallBack.success();
        } else {
            loginCallBack.singIn();
        }
    }
}