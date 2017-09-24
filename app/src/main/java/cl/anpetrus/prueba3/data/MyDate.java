package cl.anpetrus.prueba3.data;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Petrus on 13-09-2017.
 */

public class MyDate {


    private Date date;

    public MyDate() {
        date = new Date();
    }

    public MyDate(Date date) {
        this.date = date;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date toDate(String date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ParseException", e.toString());
            return null;
        }
    }

}