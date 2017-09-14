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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import cl.anpetrus.prueba3.callbacks.ActionEventCallback;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.MyDate;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.services.UserService;
import cl.anpetrus.prueba3.validators.ActionEventValidator;
import cl.anpetrus.prueba3.views.drawers.UploadPhoto;
import cl.anpetrus.prueba3.views.main.LoadingFragment;

public class ActionEventActivity extends AppCompatActivity implements ActionEventCallback {

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
    private String pathPhotoThumbails;
    private FloatingActionButton galleryFab;
    private FloatingActionButton photoFab;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int PHOTO_SIZE = 80;
    private String imageUri;

    private Event eventMaster;
    private String actionExtra;

    String dateString, timeString;
    Date date = new Date();
    //ProgressBar progressBar;

    ActionEventValidator actionValidator;
    private LoadingFragment loadingFragment;
    private FloatingActionButton selectPhotoFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        eventMaster = new Event();
        selectPhotoFab = (FloatingActionButton) findViewById(R.id.actionsFab);

        withNewPhoto = false;

        actionValidator = new ActionEventValidator(this);

        nameTv = (EditText) findViewById(R.id.nameNewEt);
        descriptionTv = (EditText) findViewById(R.id.descriptionNewEt);
        imageIv = (ImageView) findViewById(R.id.imageIv);

        dateStartEt = (EditText) findViewById(R.id.dateStartEt);
        timeStartEt = (EditText) findViewById(R.id.timeStartEt);

        actionExtra = getIntent().getStringExtra(ID_ACTION);

        Button saveUpdateBtn = (Button) findViewById(R.id.saveUpdateEventBtn);
        saveUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingShow();
                CurrentUser currentUser = new CurrentUser();
                String userUidEmail = EmailProcessor.sanitizedEmail(currentUser.email());

                String dateString = dateStartEt.getText().toString() + " " + timeStartEt.getText().toString();
                Date startDateTime;
                try {
                    startDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateString);
                    eventMaster.setName(nameTv.getText().toString());
                    eventMaster.setDescription(descriptionTv.getText().toString());
                    eventMaster.setStart(new MyDate(startDateTime).toString());
                    eventMaster.setUidUser(userUidEmail);

                    pathPhoto = imageUri;

                    actionValidator.saveOrUpdate(eventMaster, pathPhoto, actionExtra, withNewPhoto);


                } catch (ParseException e) {
                    Log.e("PARSEEXCPTION", "Error en parsear FechaStart" + e);
                    loadingDismiss();
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
            getSupportActionBar().setTitle("Actualizar Evento");
            imageIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String keyEvent = getIntent().getStringExtra(KEY_EVENT);
            saveUpdateBtn.setText("ACTUALIZAR");
            loadingShow();

            new Nodes().event(keyEvent).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event event = dataSnapshot.getValue(Event.class);
                    eventMaster = event;
                    nameTv.setText(event.getName());
                    descriptionTv.setText(event.getDescription());
                    date = MyDate.toDate(event.getStart());
                    dateString = new SimpleDateFormat("dd-MM-yyyy").format(date);
                    timeString = new SimpleDateFormat("HH:mm:ss").format(date);
                    dateStartEt.setText(dateString);
                    timeStartEt.setText(timeString);
                    setPhoto(event.getImage());
                    Toast.makeText(ActionEventActivity.this, event.getImage(), Toast.LENGTH_LONG).show();
                    Log.d("IOP", event.getImage());
                    loadingDismiss();
                   // progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  //  progressBar.setVisibility(View.INVISIBLE);
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
                magicalCamera.selectedPicture("Seleccione una Imagen");
            }
        });

        selectPhotoFab.setOnClickListener(new View.OnClickListener() {
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

        moveFabs();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
    }

    boolean withNewPhoto;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            Toast.makeText(this, "OKA", Toast.LENGTH_SHORT).show();
            Bitmap photo = magicalCamera.getPhoto();
            pathPhoto = magicalCamera.savePhotoInMemoryDevice(photo, "Imagen", "Eventos", MagicalCamera.JPEG, true);
            pathPhoto = "file://" + pathPhoto;

            magicalCamera.setResizePhoto(20);
            photo = magicalCamera.getPhoto();
            pathPhotoThumbails = magicalCamera.savePhotoInMemoryDevice(photo, "Imagen", "Eventos", MagicalCamera.JPEG, true);
            pathPhotoThumbails = "file://" + pathPhotoThumbails;

            setPhoto(pathPhoto);
            withNewPhoto = true;

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
        Log.d("IMAGE", url);
        ViewGroup.LayoutParams params = imageIv.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        imageIv.setLayoutParams(params);
        imageUri = url;

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
        photoFab.animate().translationY(-getPixels(90)).setDuration(400).start();
        galleryFab.animate().translationY(-getPixels(164)).setDuration(400).start();

        if(withNewPhoto||actionExtra.equals(ID_ACTION_UPDATE)){
            imageIv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    private void imageZoomOut() {
        photoFab.animate().rotation(0).setDuration(400).start();
        galleryFab.animate().rotation(0).setDuration(400).start();
        photoFab.animate().translationY(-getPixels(16)).setDuration(400).start();
        galleryFab.animate().translationY(-getPixels(16)).setDuration(400).start();

        ViewGroup.LayoutParams params = appBar.getLayoutParams();
        params.height = getPixels(180);
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        appBar.setLayoutParams(params);
        if(withNewPhoto||actionExtra.equals(ID_ACTION_UPDATE)){
            imageIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void moveFabs(){
        selectPhotoFab.animate().translationY(-getPixels(16)).start();
        photoFab.animate().translationY(-getPixels(16)).start();
        galleryFab.animate().translationY(-getPixels(16)).start();
    }

    private String[] permissions() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    public void updateEvent(boolean withNewPhoto) {
        //pathPhoto = imageUri;
        new UserService().saveCurrentUser();
        Log.d("XXX", imageUri);
        new UploadPhoto(ActionEventActivity.this).toFirebaseUpdate(imageUri,pathPhotoThumbails, eventMaster, withNewPhoto);
        Toast.makeText(this, "Actualizando Evento", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveEvent() {
        new UserService().saveCurrentUser();
        new UploadPhoto(ActionEventActivity.this).toFirebase(pathPhoto,pathPhotoThumbails, eventMaster);
        Toast.makeText(this, "Agregando Evento", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void loadFinished() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        //loadingDismiss();
       // Toast.makeText(this, "Evento Agregado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        loadingDismiss();
    }

    private void loadingShow(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("loading");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        loadingFragment = LoadingFragment.newInstance();
        loadingFragment.show(ft, "loading");
    }

    private void loadingDismiss(){
        loadingFragment.dismiss();
    }
}





