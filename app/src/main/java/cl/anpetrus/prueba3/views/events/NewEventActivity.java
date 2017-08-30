package cl.anpetrus.prueba3.views.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cl.anpetrus.prueba3.R;

public class NewEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_CODE = 100;
    private EditText dateStartEt, timeStartEt, nameTv, descriptionTv;
    private ImageView imageIv;
    private Uri imageUri;
    boolean imageZoom;
    LinearLayout addEventLl;
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        appBar = (AppBarLayout) findViewById(R.id.app_bar);

        nameTv = (EditText) findViewById(R.id.nameNewEt);
        descriptionTv = (EditText) findViewById(R.id.descriptionNewEt);
        imageIv = (ImageView) findViewById(R.id.imageIv);

        nameTv.setHint("NOMBRE");
        descriptionTv.setHint("DESCRIPCIÓN");

        dateStartEt = (EditText) findViewById(R.id.dateStartEt);
        timeStartEt = (EditText) findViewById(R.id.timeStartEt);
        Date dateNow = new Date();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(dateNow);
        String timeString = new SimpleDateFormat("HH:mm:ss").format(dateNow);

        dateStartEt.setText(dateString);
        timeStartEt.setText(timeString);

        dateStartEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyBoard(view);

                int year, month, day;
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateStartEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        timeStartEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyBoard(view);

                int hour, minute;
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                timeStartEt.setText(hourOfDay + ":" + minute + ":00");
                            }

                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        addEventLl = (LinearLayout) findViewById(R.id.addEventLl);

        imageIv = (ImageView) findViewById(R.id.imageIv);
        imageZoom = true;
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageZoom) {
                    imageZoom=false;
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    //appBar.animate().scaleY(600).setDuration(1000).start();
                    appBar.setLayoutParams(params);
                }else{
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = R.dimen.app_bar_height;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);
                    imageZoom=true;
                }
            }
        });

    }
    private void hideKeyBoard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private boolean isValidData(String name, String description, Date date) {
        if (name.trim().length() > 0) {
            if (description.trim().length() > 0) {
                if (date.after(new Date())) {
                    if (imageUri != null) {
                        return true;
                    } else {
                        Toast.makeText(this, "Favor agrega una imagen", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Favor ingresar fecha y hora posterior a la actual", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Favor ingresar descripción", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Favor ingresar nombre", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}
