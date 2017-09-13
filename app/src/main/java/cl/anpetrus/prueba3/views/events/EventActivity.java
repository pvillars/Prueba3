package cl.anpetrus.prueba3.views.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.callbacks.EventCallback;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.validators.EventValidator;
import cl.anpetrus.prueba3.views.main.ImageActivity;

public class EventActivity extends AppCompatActivity implements EventCallback{

    public final static String KEY_EVENT = "cl.anpetrus.prueba3.views.events.EventActivity.KEY_EVENT";

    private EventValidator validator;

    private ImageView image;
    private String imageUrl;
    private TextView name,description,dateStart, timeStart;

    private AppBarLayout appBar;

    private FloatingActionButton editFab;

    boolean imageZoom;
    private String keyEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editFab = (FloatingActionButton) findViewById(R.id.editFab);
        editFab.setVisibility(View.GONE);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this,ActionEventActivity.class);
                intent.putExtra(ActionEventActivity.KEY_EVENT, keyEvent);
                intent.putExtra(ActionEventActivity.ID_ACTION,ActionEventActivity.ID_ACTION_UPDATE);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        image = (ImageView) findViewById(R.id.detailImageIv);
        name = (TextView) findViewById(R.id.detailNameTv);
        description = (TextView) findViewById(R.id.detailDescriptionTv);
        dateStart = (TextView) findViewById(R.id.detailDateStartTv);
        timeStart = (TextView) findViewById(R.id.detailTimeStartTv);

        validator = new EventValidator(this);

       // String key = getIntent().getStringExtra(KEY_EVENT);
       // validator.loadEvent(key);

        appBar = (AppBarLayout) findViewById(R.id.app_bar);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EventActivity.this, ImageActivity.class);
                intent.putExtra(ImageActivity.KEY_URL,imageUrl);
                startActivity(intent);
                /*
                if(imageZoom) {
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);
                   // fabZoomFake.setImageResource(R.mipmap.ic_zoom_out_white_24dp);
                    imageZoom=false;
                }else{
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int pixels = (int) (200 * metrics.density + 0.5f);
                    ViewGroup.LayoutParams params = appBar.getLayoutParams();
                    params.height = pixels;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    appBar.setLayoutParams(params);
                    //image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //fabZoomFake.setImageResource(R.mipmap.ic_zoom_in_white_24dp);
                    imageZoom = true;
                }*/
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String key = getIntent().getStringExtra(KEY_EVENT);
        validator.loadEvent(key);
        Toast.makeText(this, "HHH", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadEvent(Event event) {
        Picasso.with(this)
                .load(event.getImage())
                //.fit()
                .into(image);
        imageUrl = event.getImage();

        name.setText(event.getName());
        description.setText(event.getDescription());
        keyEvent = event.getKey();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(event.getStart());
        String timeString = new SimpleDateFormat("HH:mm").format(event.getStart()) + " Hrs.";
        dateStart.setText(dateString);
        timeStart.setText(timeString);

    }

    @Override
    public void showEdit() {
        editFab.setVisibility(View.VISIBLE);
    }
}
