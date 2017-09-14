package cl.anpetrus.prueba3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.data.MyDate;
import cl.anpetrus.prueba3.listeners.EventListener;
import cl.anpetrus.prueba3.models.Event;

/**
 * Created by USUARIO on 06-09-2017.
 */

public class EventsAdapter extends FirebaseRecyclerAdapter<Event,EventsAdapter.EventHolder> {

    private EventListener listener;
    private Context context;


    public EventsAdapter(EventListener listener, Context context, Query reference) {
        super(Event.class, R.layout.list_item_card_event, EventHolder.class, reference);
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(final EventHolder viewHolder, final Event model, int position) {

            viewHolder.name.setText(model.getName());
            String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(MyDate.toDate(model.getStart()));
            viewHolder.dateStart.setText(dateString);
            viewHolder.nameAuthor.setText(model.getUidUser());
            Picasso.with(context)
                    .load(model.getImageThumbnail())
                    .fit()
                    .into(viewHolder.image);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.clicked(model.getKey());
                }
            });

    }

    static public class EventHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name, dateStart, nameAuthor;


        public EventHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageListIv);
            name = itemView.findViewById(R.id.nameListTv);
            dateStart = itemView.findViewById(R.id.dateStartListTv);
            nameAuthor = itemView.findViewById(R.id.authorNameListTv);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
    }
}
