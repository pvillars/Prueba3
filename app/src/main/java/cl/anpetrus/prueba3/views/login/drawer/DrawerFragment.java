package cl.anpetrus.prueba3.views.login.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import cl.anpetrus.prueba3.EventListValidator;
import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.views.ListEventsFragment;
import cl.anpetrus.prueba3.views.login.LoginActivity;

public class DrawerFragment extends Fragment {


    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView logoutTv = view.findViewById(R.id.logoutTv);
        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
            }
        });

        TextView emailTv = view.findViewById(R.id.emailTv);
        emailTv.setText(new CurrentUser().email());


        final EventListValidator eventListValidator = new EventListValidator(ListEventsFragment.getThis());
        TextView soonEventsTv = view.findViewById(R.id.soonEventsTv);
        soonEventsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventListValidator.showEventList(EventListValidator.TYPE_EVENTS.SOON_EVENTS);
            }
        });

        TextView myEventsTv = view.findViewById(R.id.myEventsTv);
        myEventsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListValidator.showEventList(EventListValidator.TYPE_EVENTS.MY_EVENTS);
            }
        });


    }
}
