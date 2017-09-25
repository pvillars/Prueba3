package cl.anpetrus.prueba3.views.events;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import cl.anpetrus.prueba3.R;

public class ImageActivity extends AppCompatActivity {

    public static final String KEY_URL = "cl.anpetrus.prueba3.views.events.ImageActivity.KEY_URL";
    public static final String KEY_THUMBS_IMAGE = "cl.anpetrus.prueba3.views.events.ImageActivity.KEY_THUMBS_IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        ImageView imageView = (ImageView) findViewById(R.id.imageBtn);

        Bundle extras = getIntent().getExtras();
        Bitmap bitmap = extras.getParcelable(KEY_THUMBS_IMAGE);


        String url = getIntent().getStringExtra(KEY_URL);

        Picasso.with(this).invalidate(url);
        Picasso.with(this).load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
        Picasso.with(this)
                .load(url)
                .placeholder(new BitmapDrawable(getResources(), bitmap))
                .into(imageView);

        findViewById(R.id.backBnv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}