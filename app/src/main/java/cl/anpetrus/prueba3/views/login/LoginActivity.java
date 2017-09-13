package cl.anpetrus.prueba3.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;

import java.util.Arrays;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.callbacks.LoginCallBack;
import cl.anpetrus.prueba3.validators.LoginValidator;
import cl.anpetrus.prueba3.views.main.MainActivity;

public class LoginActivity extends AppCompatActivity  implements LoginCallBack{

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        new LoginValidator(this).init();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "requestCode "+requestCode, Toast.LENGTH_SHORT).show();
        if (RC_SIGN_IN == requestCode) {
            Toast.makeText(this, "resultCode "+resultCode, Toast.LENGTH_SHORT).show();
            if (ResultCodes.OK == resultCode) {
               success();
            }

        }
    }

    @Override
    public void success() {
        Toast.makeText(this, "LOGGED", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void singIn() {
        Toast.makeText(this, "SIGNIN", Toast.LENGTH_SHORT).show();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()/*,
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()/*,
                                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()*/))
                       // .setTheme(R.style.LoginTheme)
                       // .setLogo(R.mipmap.logo)
                        .build(),
                RC_SIGN_IN);
    }
}
