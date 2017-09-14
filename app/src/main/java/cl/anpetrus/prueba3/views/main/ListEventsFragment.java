package cl.anpetrus.prueba3.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.adapters.EventsAdapter;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.data.EmailProcessor;
import cl.anpetrus.prueba3.data.MyDate;
import cl.anpetrus.prueba3.data.Nodes;
import cl.anpetrus.prueba3.listeners.EventListener;
import cl.anpetrus.prueba3.views.events.EventActivity;


public class ListEventsFragment extends Fragment implements EventListener {

    private static ListEventsFragment fragment;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private ShowEvents showEvents;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ListEventsFragment getThis() {
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_events, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.reloadSr);

        recyclerView = (RecyclerView) view.findViewById(R.id.eventsRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        //DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        //dividerItemDecoration.setDrawable(...);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        showEvents = ShowEvents.SOON;
        fragment = this;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadEvents();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void clicked(String keyEvent) {

        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra(EventActivity.KEY_EVENT, keyEvent);
        startActivity(intent);

    }

    @Override
    public void dataChange() {

    }

    @Override
    public void showSoonEvents() {
        getActivity().setTitle("Proxímos Eventos");
        Query dbR = new Nodes()
                .eventsList()
                .orderByChild("start")
                .startAt(new MyDate().toString());
        adapter = new EventsAdapter(this, getContext(),dbR);

        recyclerView.setAdapter(adapter);
        showEvents = ShowEvents.SOON;
    }

    @Override
    public void showMyEvents() {
        getActivity().setTitle("Mis Eventos");
        Query dbR = new Nodes()
                .myEventList(EmailProcessor.sanitizedEmail(new CurrentUser().email()))
                .orderByChild("start");

        adapter = new EventsAdapter(this, getContext(), dbR);
        recyclerView.setAdapter(adapter);
        showEvents = ShowEvents.MY;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.cleanup();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadEvents();

    }

    private void reloadEvents(){
        switch (showEvents) {
            case MY:
                showMyEvents();
                break;
            case SOON:
                showSoonEvents();
                break;
        }
    }

    private enum ShowEvents {
        MY, SOON
    }
}
