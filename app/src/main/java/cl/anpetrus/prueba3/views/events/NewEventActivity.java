package cl.anpetrus.prueba3.views.events;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.views.drawers.UploadPhoto;

public class NewEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_CODE = 100;
    private EditText dateStartEt, timeStartEt, nameTv, descriptionTv;
    private ImageView imageIv;
    private Uri imageUri;
    boolean imageZoom;
    LinearLayout addEventLl;
    AppBarLayout appBar;

    FloatingActionButton galleryFab;
    FloatingActionButton photoFab;

    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int PHOTO_SIZE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton actionFab = (FloatingActionButton) findViewById(R.id.actionsFab);

        photoFab = (FloatingActionButton) findViewById(R.id.photoFab);

        galleryFab = (FloatingActionButton) findViewById(R.id.galleryFab);

        actionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageZoom) {
                    imageZoom = false;
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);


                    photoFab.animate().rotation(360).setDuration(400).start();
                    galleryFab.animate().rotation(360).setDuration(400).start();
                    photoFab.animate().translationY(-250).setDuration(400).start();
                    galleryFab.animate().translationY(-500).setDuration(400).start();
                }else{
                    photoFab.animate().rotation(0).setDuration(400).start();
                    galleryFab.animate().rotation(0).setDuration(400).start();
                    photoFab.animate().translationY(0).setDuration(400).start();
                    galleryFab.animate().translationY(0).setDuration(400).start();

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int pixels = (int) (180 * metrics.density + 0.5f);
                    imageIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = pixels;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);
                    imageZoom = true;
                }
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

                if (imageZoom) {
                    imageZoom = false;
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);

                    photoFab.animate().rotation(360).setDuration(400).start();
                    galleryFab.animate().rotation(360).setDuration(400).start();
                    photoFab.animate().translationY(-250).setDuration(400).start();
                    galleryFab.animate().translationY(-500).setDuration(400).start();
                  /*  String[] permissions = new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    magicalPermissions = new MagicalPermissions(NewEventActivity.this, permissions);
                    magicalCamera = new MagicalCamera(NewEventActivity.this, PHOTO_SIZE, magicalPermissions);
*/

                } else {
                    photoFab.animate().rotation(0).setDuration(400).start();
                    galleryFab.animate().rotation(0).setDuration(400).start();
                    photoFab.animate().translationY(0).setDuration(400).start();
                    galleryFab.animate().translationY(0).setDuration(400).start();

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int pixels = (int) (180 * metrics.density + 0.5f);
                    imageIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = pixels;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);
                    imageZoom = true;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            Toast.makeText(this, "OKA", Toast.LENGTH_SHORT).show();
            Bitmap photo = magicalCamera.getPhoto();
            String path = magicalCamera.savePhotoInMemoryDevice(photo, "Avatar", "Flash", MagicalCamera.JPEG, true);
            Log.d("PATH", path);
            path = "file://" + path;
            setPhoto(path);
            new UploadPhoto(this).toFirebase(path, "");
        } else {
            requestSelfie();
        }
    }


    private void requestSelfie() {
        Toast.makeText(this, "XAXA", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setTitle("Selfie :)")
                .setMessage("para completar el registro debes tener una selfie actualizada")
                .setCancelable(false)
                .setPositiveButton("SELFIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        magicalCamera.takePhoto();
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void setPhoto(String url) {

        Picasso.with(this)
                .load(url)
                .fit()
                .into(imageIv);

    }
}





