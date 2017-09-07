package cl.anpetrus.prueba3.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.adapters.EventsAdapter;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.listeners.EventListener;
import cl.anpetrus.prueba3.views.events.EventActivity;


public class ListEventsFragment extends Fragment implements EventListener {

    private static ListEventsFragment fragment;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    public static ListEventsFragment getThis(){
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_events, container, false);

         recyclerView = (RecyclerView) view.findViewById(R.id.eventsRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        //DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        //dividerItemDecoration.setDrawable(...);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        adapter = new EventsAdapter(this,getContext(),new Nodes().eventsList());
        recyclerView.setAdapter(adapter);

        fragment = this;
        return view;
    }


    @Override
    public void clicked(String keyEvent) {

        Intent intent = new Intent(getContext(),EventActivity.class);
        intent.putExtra(EventActivity.KEY_EVENT, keyEvent);
        startActivity(intent);

    }

    @Override
    public void dataChange() {

    }

    @Override
    public void showSoonEvents() {
        Toast.makeText(getContext(), "SONN EVENT", Toast.LENGTH_SHORT).show();
        adapter = new EventsAdapter(this,getContext(),new Nodes().eventsList());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMyEvents() {
        Toast.makeText(getContext(), "My EVENT "+new CurrentUser().email(), Toast.LENGTH_SHORT).show();
        adapter = new EventsAdapter(this,getContext(),new Nodes().myEventList(new CurrentUser().sanitizedEmail(new CurrentUser().email())));
        recyclerView.setAdapter(adapter);
    }
}
