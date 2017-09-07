package cl.anpetrus.prueba3.views.events;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.services.UserService;
import cl.anpetrus.prueba3.views.drawers.UploadPhoto;

public class ActionEventActivity extends AppCompatActivity {

    public final static String ID_ACTION = "cl.anpetrus.prueba3.views.events.ActionEventActivity.ID_ACTION";
    public final static String KEY_EVENT = "cl.anpetrus.prueba3.views.events.ActionEventActivity.KEY_EVENT";
    public final static String ID_ACTION_NEW = "cl.anpetrus.prueba3.views.events.ActionEventActivity.ID_ACTION_NEW";
    public final static String ID_ACTION_UPDATE = "cl.anpetrus.prueba3.views.events.ActionEventActivity.ID_ACTION_UPDATE";

    private EditText dateStartEt, timeStartEt, nameTv, descriptionTv;
    private ImageView imageIv;
    boolean imageZoom;
    private LinearLayout addEventLl;
    private AppBarLayout appBar;
    private String pathPhoto;
    private FloatingActionButton galleryFab;
    private FloatingActionButton photoFab;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int PHOTO_SIZE = 80;
    private String imageUri;

    private Event event;
    private String actionExtra;

    String dateString, timeString;
    Date date = new Date();
    ProgressBar progressBar;
    boolean photoUdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        event = new Event();
        FloatingActionButton actionFab = (FloatingActionButton) findViewById(R.id.actionsFab);

        progressBar = (ProgressBar) findViewById(R.id.loadingBar);

        progressBar.setVisibility(View.INVISIBLE);

        actionExtra = getIntent().getStringExtra(ID_ACTION);

        nameTv = (EditText) findViewById(R.id.nameNewEt);
        descriptionTv = (EditText) findViewById(R.id.descriptionNewEt);
        imageIv = (ImageView) findViewById(R.id.imageIv);


        dateStartEt = (EditText) findViewById(R.id.dateStartEt);
        timeStartEt = (EditText) findViewById(R.id.timeStartEt);

        Button saveUpdateBtn = (Button) findViewById(R.id.saveUpdateEventBtn);
        saveUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentUser currentUser = new CurrentUser();
                String userUidEmail = currentUser.sanitizedEmail(currentUser.email());

                String dateString = dateStartEt.getText().toString() + " " + timeStartEt.getText().toString();
                Date startDateTime;
                try {
                    startDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateString);
                    event.setName(nameTv.getText().toString());
                    event.setDescription(descriptionTv.getText().toString());
                    event.setStart(startDateTime);
                    event.setUidUser(userUidEmail);
                    event.setImage(imageUri);

                    if (isValidData(event)) {
                        if (actionExtra.equals(ID_ACTION_UPDATE)) {
                            event.setKey("-KsxpyqcsHAlM2CV6hdf");

                            pathPhoto = imageUri;
                            new UserService().saveCurrentUser();
                            new UploadPhoto(ActionEventActivity.this).toFirebaseUpdate(pathPhoto, event, photoUdate);
                            Toast.makeText(ActionEventActivity.this, "Evento actualizado exitozamente", Toast.LENGTH_SHORT).show();


                        } else {
                            new UserService().saveCurrentUser();
                            new UploadPhoto(ActionEventActivity.this).toFirebase(pathPhoto, event);
                            Toast.makeText(ActionEventActivity.this, "Evento agregado exitozamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ParseException e) {
                    Log.e("PARSEEXCPTION", "Error en parsear FechaStart" + e);
                }


            }
        });


        if (actionExtra.equals(ID_ACTION_NEW)) {
            getSupportActionBar().setTitle("Nuevo Evento");
            saveUpdateBtn.setText("AGREGAR");
            dateString = new SimpleDateFormat("dd-MM-yyyy").format(date);
            timeString = new SimpleDateFormat("HH:mm:ss").format(date);
            dateStartEt.setText(dateString);
            timeStartEt.setText(timeString);
        } else if (actionExtra.equals(ID_ACTION_UPDATE)) {

            progressBar.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Actualizar Evento");
            String keyEvent = "-KsxpyqcsHAlM2CV6hdf";//getIntent().getStringExtra(KEY_EVENT);
            saveUpdateBtn.setText("ACTUALIZAR");
            new Nodes().event(keyEvent).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event event = dataSnapshot.getValue(Event.class);
                    nameTv.setText(event.getName());
                    descriptionTv.setText(event.getDescription());
                    date = event.getStart();
                    dateString = new SimpleDateFormat("dd-MM-yyyy").format(date);
                    timeString = new SimpleDateFormat("HH:mm:ss").format(date);
                    dateStartEt.setText(dateString);
                    timeStartEt.setText(timeString);
                    setPhoto(event.getImage());
                    Toast.makeText(ActionEventActivity.this, event.getImage(), Toast.LENGTH_LONG).show();
                    Log.d("IOP", event.getImage());
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            getSupportActionBar().setTitle("Evento");
        }


        photoFab = (FloatingActionButton) findViewById(R.id.photoFab);
        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                magicalPermissions = new MagicalPermissions(ActionEventActivity.this, permissions());
                magicalCamera = new MagicalCamera(ActionEventActivity.this, PHOTO_SIZE, magicalPermissions);
                requestPhoto();
            }
        });

        galleryFab = (FloatingActionButton) findViewById(R.id.galleryFab);
        galleryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                magicalPermissions = new MagicalPermissions(ActionEventActivity.this, permissions());
                magicalCamera = new MagicalCamera(ActionEventActivity.this, PHOTO_SIZE, magicalPermissions);
                magicalCamera.selectedPicture("my_header_name");
            }
        });

        actionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageZoom(view);
            }
        });

        appBar = (AppBarLayout) findViewById(R.id.app_bar);


        nameTv.setHint("NOMBRE");
        descriptionTv.setHint("DESCRIPCIÓN");




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

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActionEventActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActionEventActivity.this,
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
        addEventLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard(view);
            }
        });

        imageIv = (ImageView) findViewById(R.id.imageIv);
        imageZoom = true;
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageZoom(view);
            }
        });


    }

    private int getPixels(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (dp * metrics.density + 0.5f);
    }

    private void hideKeyBoard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private boolean isValidData(Event event) {
        if (event.getName().trim().length() > 0) {
            if (event.getDescription().trim().length() > 0) {
                if (event.getStart().after(new Date())) {
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
            pathPhoto = magicalCamera.savePhotoInMemoryDevice(photo, "Avatar", "Flash", MagicalCamera.JPEG, true);
            Log.d("PATH", pathPhoto);
            pathPhoto = "file://" + pathPhoto;
            setPhoto(pathPhoto);
            photoUdate = true;
            // new UploadPhoto(this).toFirebase(path, "");
        } else {
            // requestPhoto();
            // error con camara
        }
    }


    private void requestPhoto() {
        magicalCamera.takePhoto();
    }

    private void setPhoto(String url) {
        Picasso.with(this)
                .load(url)
                .error(R.mipmap.ic_insert_photo_white_36dp)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageIv);
        Log.d("IMAGE",url);
        ViewGroup.LayoutParams params = imageIv.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        imageIv.setLayoutParams(params);
        imageUri = url;
        photoUdate = false;
    }

    private void imageZoom(View view) {
        hideKeyBoard(view);
        if (imageZoom) {
            imageZoom = false;
            imageZoomIn();
        } else {
            imageZoomOut();
            imageZoom = true;
        }
    }

    private void imageZoomIn() {
        ViewGroup.LayoutParams params = appBar.getLayoutParams();
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        appBar.setLayoutParams(params);

        photoFab.animate().rotation(360).setDuration(400).start();
        galleryFab.animate().rotation(360).setDuration(400).start();
        photoFab.animate().translationY(-getPixels(80)).setDuration(400).start();
        galleryFab.animate().translationY(-getPixels(160)).setDuration(400).start();
    }

    private void imageZoomOut() {
        photoFab.animate().rotation(0).setDuration(400).start();
        galleryFab.animate().rotation(0).setDuration(400).start();
        photoFab.animate().translationY(0).setDuration(400).start();
        galleryFab.animate().translationY(0).setDuration(400).start();

        ViewGroup.LayoutParams params = appBar.getLayoutParams();
        params.height = getPixels(180);
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        appBar.setLayoutParams(params);
    }

    private String[] permissions() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }
}





