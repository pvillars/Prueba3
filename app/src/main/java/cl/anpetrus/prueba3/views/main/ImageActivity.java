package cl.anpetrus.prueba3.views.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cl.anpetrus.prueba3.R;

public class ImageActivity extends AppCompatActivity {

    public static final String KEY_URL = "cl.anpetrus.prueba3.views.main.ImageActivity.KEY_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);

        findViewById(R.id.backBnv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String url = getIntent().getStringExtra(KEY_URL);
        Picasso.with(this)
                .load(url)
                .into((ImageView) findViewById(R.id.imageBtn));
    }

    @Override
    public void onBackPressed() {
    }
}