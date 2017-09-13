package cl.anpetrus.prueba3.views.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cl.anpetrus.prueba3.R;

public class ImageActivity extends AppCompatActivity {

    public static final String KEY_URL = "cl.anpetrus.prueba3.views.main.ImageActivity.KEY_URL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView imageBtn = (ImageView) findViewById(R.id.imageBtn);
        String url = getIntent().getStringExtra(KEY_URL);

        Picasso.with(this)
                .load(url)
                .into(imageBtn);


    }
}
