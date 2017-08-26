package cl.anpetrus.prueba3;

import cl.anpetrus.prueba3.data.CurrentUser;

/**
 * Created by Petrus on 26-08-2017.
 */

public class LoginValidator {

    LoginCallBack loginCallBack;

    public LoginValidator(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    public void init(){
        if (new CurrentUser().getCurrentUser() != null) {
            loginCallBack.success();
        } else {
            loginCallBack.singIn();
        }
    }
}


