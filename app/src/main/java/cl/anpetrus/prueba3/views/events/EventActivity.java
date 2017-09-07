package cl.anpetrus.prueba3.views.events;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cl.anpetrus.prueba3.EventCallback;
import cl.anpetrus.prueba3.EventValidator;
import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.models.Event;

public class EventActivity extends AppCompatActivity implements EventCallback{

    public final static String KEY_EVENT = "cl.anpetrus.prueba3.views.events.EventActivity.KEY_EVENT";

    EventValidator validator;

    ImageView image;
    TextView name,description,dateStart, timeStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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

        image = (ImageView) findViewById(R.id.detailImageIv);
        name = (TextView) findViewById(R.id.detailNameTv);
        description = (TextView) findViewById(R.id.detailDescriptionTv);
        dateStart = (TextView) findViewById(R.id.detailDateStartTv);
        timeStart = (TextView) findViewById(R.id.detailTimeStartTv);

        validator = new EventValidator(this);

        String key = getIntent().getStringExtra(KEY_EVENT);
        validator.loadEvent(key);


    }

    @Override
    public void loadEvent(Event event) {
        Picasso.with(this)
                .load(event.getImage())
                .fit()
                .into(image);
        name.setText(event.getName());
        description.setText(event.getDescription());
        dateStart.setText(event.getStart().toString());
        timeStart.setText(event.getStart().toString());

    }
}
