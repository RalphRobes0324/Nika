// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import ca.nika.it.gear5.LoginSetup.LoginActivity;

public class ProfileFragment extends Fragment {

    ImageView mImageView;
    ImageButton mChooseBtn;
    Button btn;
    TextView usernameTextView, topScoreTextView, currencyTextView;
    String typeOFsignout;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_CODE = 1000;
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
            Bitmap photo= (Bitmap) data.getExtras().get(getString(R.string.data));
            mImageView.setImageBitmap(photo);
            ByteArrayOutputStream imgByte = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, imgByte);
            byte[] b = imgByte.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            preferenceManager.setString(getString(R.string.image_data),encodedImage);

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
            String typeOFLogin = sharedPreferences.getString("typeLogin", getString(R.string.blank));
            typeOFsignout = typeOFLogin;
            if(typeOFLogin.equals("GearAccount")) {
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
                Query checkUser = reference.orderByChild(getString(R.string.childRef_username)).equalTo(getUserId);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String userName = snapshot.child(getUserId).child(getString(R.string.childRef_username)).getValue(String.class);
                            Integer userScore = snapshot.child(getUserId).child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                            Integer userCur = snapshot.child(getUserId).child(getString(R.string.childRef_Currency)).getValue(Integer.class);
                            usernameTextView.setText(getString(R.string.usernameDisplay) + userName);
                            topScoreTextView.setText(getString(R.string.scoreDisplay) + userScore);
                            currencyTextView.setText(getString(R.string.currencyDisplay) + userCur + getString(R.string.gears));

                        } else {
                            Log.d(getString(R.string.FAILED), "FAILED GEAR");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if(typeOFLogin.equals("GearGoogleAccount")){
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference uidRef = db.child("users").child(getUserId);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();
                            String userName = snapshot.child(getString(R.string.childRef_username)).getValue(String.class);
                            Integer userScore = snapshot.child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                            Integer userCur = snapshot.child(getString(R.string.childRef_Currency)).getValue(Integer.class);
                            usernameTextView.setText(getString(R.string.usernameDisplay) + userName);
                            topScoreTextView.setText(getString(R.string.scoreDisplay) + userScore);
                            currencyTextView.setText(getString(R.string.currencyDisplay) + userCur + getString(R.string.gears));
                        }else{
                            Log.d(getString(R.string.FAILED), "FAILED GOOGLE LOAD PROF");
                        }
                    }
                });

            }
            else{
                Log.d(getString(R.string.FAILED), getString(R.string.FAILED));
            }

        }

        String previouslyEncodedImage = preferenceManager.getString(getString(R.string.image_data));

        if( !previouslyEncodedImage.equalsIgnoreCase(getString(R.string.blank)) ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            mImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        getString(R.string.PermissionGranted), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        getString(R.string.PermissionDenied), Snackbar.LENGTH_LONG).show();
            }
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
        topScoreTextView = (TextView) view.findViewById(R.id.nikaTopScore);
        currencyTextView = (TextView) view.findViewById(R.id.nikaCurrency);



        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        }
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

                                if (typeOFsignout.equals("GearGoogleAccount")){
                                    FirebaseAuth.getInstance().signOut();
                                    Log.d("LOGOUT", "GOOGLE");
                                    getActivity().finish();
                                }else{
                                    Log.d("LOGOUT", "NORM");
                                    getActivity().finish();
                                }


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