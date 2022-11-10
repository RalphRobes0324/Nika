// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static ca.nika.it.gear5.R.string.PermissionDenied;
import static ca.nika.it.gear5.R.string.PermissionGranted;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.OutputStream;

import ca.nika.it.gear5.LoginSetup.LoginActivity;

public class ProfileFragment extends Fragment {

    ImageView mImageVIew;
    TextView usernameTextView;
    Button mChooseBtn;
    Button btn;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();

                    Snackbar.make(getView(), PermissionGranted, Snackbar.LENGTH_SHORT)
                            .show();


                } else {
                    Snackbar.make(getView(), PermissionDenied, Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImageVIew.setImageURI(data.getData());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Grabbing data from Main Activity
        /*usernameTextView = (TextView) view.findViewById(R.id.nikaUsername);
        Bundle data = getArguments();
        String username = data.getString("userProfileData");
        Toast.makeText(getActivity().getApplicationContext(), username, Toast.LENGTH_LONG).show();
        /*usernameTextView.setText(username);*/


        mImageVIew = view.findViewById(R.id.nikaProfileView);
        mChooseBtn = view.findViewById(R.id.nikaImgBtn);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.click_to_add_image), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {

                        pickImageFromGallery();
                        loadImage();

                    }

                } else {
                    pickImageFromGallery();
                    loadImage();
                }
            }
        });

        btn = view.findViewById(R.id.nikaLogout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.ask_logout)
                        .setIcon(R.drawable.devil_fruit)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.checkbox), MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(getString(R.string.remember), getString(R.string.unchecked));
                                editor.apply();

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


            }
        });


        return view;
    }

    private void loadImage() {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.click_to_add_image), Context.MODE_PRIVATE);
    }

    public void doSave() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.click_to_add_image), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {

                    pickImageFromGallery();
                    loadImage();

                }

            }
        editor.apply();

    }
}