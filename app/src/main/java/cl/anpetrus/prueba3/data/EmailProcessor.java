package cl.anpetrus.prueba3.data;

/**
 * Created by Petrus on 04-09-2017.
 */

public class EmailProcessor {

    public static String sanitizedEmail(String email) {
        return email.replace("@", "AT").replace(".", "AT");
    }

}