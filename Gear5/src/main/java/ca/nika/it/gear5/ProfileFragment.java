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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;

import ca.nika.it.gear5.LoginSetup.LoginActivity;

public class ProfileFragment extends Fragment {

    ImageView mImageView;
    Button mChooseBtn;
    Button btn;
    TextView usernameTextView;
    String userID;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private View view;

    PreferenceManager preferenceManager;
    Intent camera;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImageView.setImageURI(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==118&& resultCode==RESULT_OK){
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
            ByteArrayOutputStream imgByte = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, imgByte);
            byte[] b = imgByte.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            preferenceManager.setString("image_data",encodedImage);

        }
    }

    public void imgMethod() {
            camera=new Intent();
            camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera,118);
    }

    public void loadImage() {
        preferenceManager=PreferenceManager.getInstance(getActivity());


        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {

            String getUserId = sharedPreferences.getString("userProfile", "");

            usernameTextView.setText(getUserId);
            Toast.makeText(getActivity().getApplicationContext(), getUserId, Toast.LENGTH_LONG).show();

        }

        String previouslyEncodedImage = preferenceManager.getString("image_data");

        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            mImageView.setImageBitmap(bitmap);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        preferenceManager=PreferenceManager.getInstance(getActivity());

        mImageView = view.findViewById(R.id.nikaProfileView);
        mChooseBtn = view.findViewById(R.id.nikaImgBtn);
        usernameTextView = (TextView) view.findViewById(R.id.nikaUsername);

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.CAMERA,
                        },123);
                    }
                }
                else{
                    imgMethod();
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

        loadImage();

        return view;
    }

}