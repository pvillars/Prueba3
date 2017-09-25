package cl.anpetrus.prueba3.views.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.callbacks.EventCallback;
import cl.anpetrus.prueba3.data.MyDate;
import cl.anpetrus.prueba3.models.Event;
import cl.anpetrus.prueba3.services.UploadImageEvent;
import cl.anpetrus.prueba3.validators.EventValidator;
import cl.anpetrus.prueba3.views.main.LoadingFragment;

public class EventActivity extends AppCompatActivity implements EventCallback {

    public final static String KEY_EVENT = "cl.anpetrus.prueba3.views.events.EventActivity.KEY_EVENT";
    public final static String KEY_NAME = "cl.anpetrus.prueba3.views.events.EventActivity.KEY_NAME";

    private EventValidator validator;
    private ImageView image;
    private String imageUrlThums;
    private TextView name, description, dateStart, timeStart;
    private FloatingActionButton editFab;
    private String keyEvent;
    private LoadingFragment loadingFragment;
    private Toolbar toolbar;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String nameAb = getIntent().getStringExtra(KEY_NAME);
        getSupportActionBar().setTitle(nameAb);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingShow();
        editFab = (FloatingActionButton) findViewById(R.id.editFab);
        editFab.setVisibility(View.GONE);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventActivity.this, ActionEventActivity.class);
                intent.putExtra(ActionEventActivity.KEY_EVENT, keyEvent);
                intent.putExtra(ActionEventActivity.ID_ACTION, ActionEventActivity.ID_ACTION_UPDATE);
                startActivity(intent);
            }
        });


        image = (ImageView) findViewById(R.id.detailImageIv);
        name = (TextView) findViewById(R.id.detailNameTv);
        description = (TextView) findViewById(R.id.detailDescriptionTv);
        dateStart = (TextView) findViewById(R.id.detailDateStartTv);
        timeStart = (TextView) findViewById(R.id.detailTimeStartTv);

        validator = new EventValidator(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EventActivity.this, ImageActivity.class);

                intent.putExtra(ImageActivity.KEY_URL, imageUrl);

                image.buildDrawingCache();
                Bitmap imageBitmap = image.getDrawingCache();
                imageBitmap = UploadImageEvent.getResizedBitmap(imageBitmap,100);
                Bundle extras = new Bundle();
                extras.putParcelable(ImageActivity.KEY_THUMBS_IMAGE, imageBitmap);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String key = getIntent().getStringExtra(KEY_EVENT);
        validator.loadEvent(key);
    }

    @Override
    protected void onStop() {
        super.onStop();
        image.setImageBitmap(null);
        name.setText("");
        description.setText("");
        dateStart.setText("");
        timeStart.setText("");
        toolbar.setTitle("");
    }

    @Override
    public void loadEvent(Event event) {

        imageUrlThums = event.getImageThumbnail();
        Picasso.with(this).invalidate(imageUrlThums);
        Picasso.with(this).load(imageUrlThums).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
        Picasso.with(this)
                .load(imageUrlThums)
                .into(image);

        imageUrl = event.getImage();
        String name = event.getName();
        this.name.setText(name);
        if (name.length() > 14)
            name = name.substring(0, 13) + "...";

        getSupportActionBar().setTitle(name);
        description.setText(event.getDescription());
        keyEvent = event.getKey();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(MyDate.toDate(event.getStart()));
        String timeString = new SimpleDateFormat("HH:mm").format(MyDate.toDate(event.getStart())) + " Hrs.";
        dateStart.setText(dateString);
        timeStart.setText(timeString);

        loadingDismiss();
    }

    @Override
    public void showEdit() {
        editFab.setVisibility(View.VISIBLE);
    }

    private void loadingShow() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("loading");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        loadingFragment = LoadingFragment.newInstance();
        loadingFragment.show(ft, "loading");
    }

    private void loadingDismiss() {
        loadingFragment.dismiss();
    }
}