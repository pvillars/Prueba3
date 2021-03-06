package cl.anpetrus.prueba3.views.login.drawer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Map;

import cl.anpetrus.prueba3.R;
import cl.anpetrus.prueba3.data.CurrentUser;
import cl.anpetrus.prueba3.services.UploadAvatarUser;
import cl.anpetrus.prueba3.services.UploadImageEvent;
import cl.anpetrus.prueba3.validators.EventListValidator;
import cl.anpetrus.prueba3.validators.MenuValidator;
import cl.anpetrus.prueba3.views.drawers.PhotoUserCallback;
import cl.anpetrus.prueba3.views.drawers.PhotoUserValidation;
import cl.anpetrus.prueba3.views.login.LoginActivity;
import cl.anpetrus.prueba3.views.main.ListEventsFragment;
import cl.anpetrus.prueba3.views.main.MainActivity;

import static android.app.Activity.RESULT_OK;
import static cl.anpetrus.prueba3.R.layout.fragment_drawer_menu;

public class DrawerFragment extends Fragment implements PhotoUserCallback {


    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private CircularImageView avatar;
    private int PHOTO_SIZE = 30;
    private TextView takeAvatarTv;

    public DrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(fragment_drawer_menu, container, false);
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
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
            }
        });


        takeAvatarTv = view.findViewById(R.id.takeAvatarTv);
        takeAvatarTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                magicalCamera.takeFragmentPhoto(DrawerFragment.this);
            }
        });
        TextView emailTv = view.findViewById(R.id.emailTv);
        emailTv.setText(new CurrentUser().email());

        final EventListValidator eventListValidator = new EventListValidator(ListEventsFragment.getThis());
        TextView soonEventsTv = view.findViewById(R.id.soonEventsTv);
        soonEventsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
                eventListValidator.showEventList(EventListValidator.TYPE_EVENTS.SOON_EVENTS);
            }
        });

        TextView myEventsTv = view.findViewById(R.id.myEventsTv);
        myEventsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
                eventListValidator.showEventList(EventListValidator.TYPE_EVENTS.MY_EVENTS);
            }
        });

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(getActivity(), PHOTO_SIZE, magicalPermissions);

        avatar = view.findViewById(R.id.avatarCi);

        new PhotoUserValidation(this).validate();
    }

    private void closeMenu() {
        MenuValidator menuValidator = new MenuValidator(MainActivity.getThis());
        menuValidator.closeMenu();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        magicalCamera.resultPhoto(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Toast.makeText(getContext(), "Cargando foto", Toast.LENGTH_SHORT).show();
            takeAvatarTv.setText("Cargando...");
            takeAvatarTv.setClickable(false);

            Bitmap photo = magicalCamera.getPhoto();
            photo = UploadImageEvent.getResizedBitmap(photo, 250);

            new UploadAvatarUser(this).uploadPhotoAvatar(photo);
        }
    }

    private void requestSelfie() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Selfie :)")
                .setMessage("Para completar el registro debes tener una selfie actualizada")
                .setCancelable(true)
                .setPositiveButton("SELFIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        magicalCamera.takeFragmentPhoto(DrawerFragment.this);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void emptyPhoto() {
        requestSelfie();
    }

    @Override
    public void photoAvailable(String url) {
        setPhoto(url);
    }

    @Override
    public void photoUpload(String url) {
        setPhoto(url);
        Toast.makeText(getContext(), "Avatar actualizado", Toast.LENGTH_SHORT).show();
        takeAvatarTv.setText("Cambiar");
        takeAvatarTv.setClickable(true);
    }

    private void setPhoto(String url) {
        Picasso.with(getContext()).invalidate(url);
        Picasso.with(getContext()).load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
        Picasso.with(getContext())
                .load(url)
                .centerCrop()
                .fit()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(avatar);
    }
}