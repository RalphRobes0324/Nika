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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfileFragment extends Fragment {

    ImageView mImageView;
    ImageButton mChooseBtn;
    Button btn, mSimulateScore;
    TextView usernameTextView, topScoreTextView, currencyTextView;
    String typeOFsignout, globalId, profileUser, profileCur, profileScore;
    EditText inputScore;
    int offlineMode = 0;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_CODE = 1000;
    private View view;

    PreferenceManager preferenceManager;
    Intent camera;

    boolean connected = false;


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

    // This loadImage used to be inside of a big block of code, but after the refactor, it is in its own function
    public void loadImage() {
        preferenceManager=PreferenceManager.getInstance(getActivity());

        String previouslyEncodedImage = preferenceManager.getString(getString(R.string.image_data));

        if( !previouslyEncodedImage.equalsIgnoreCase(getString(R.string.blank)) ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            mImageView.setImageBitmap(bitmap);
        }
    }

    public void loadProfile() {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences!= null) {
            String typeOFLogin = sharedPreferences.getString(getString(R.string.key_typelogin), getString(R.string.blank));
            typeOFsignout = typeOFLogin;
            if(typeOFLogin.equals(getString(R.string.gearaccount))) {
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                globalId = getUserId;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
                Query checkUser = reference.orderByChild(getString(R.string.childRef_username)).equalTo(getUserId);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            int Checker = sharedPreferences.getInt(getString(R.string.checker), offlineMode);
                            if (Checker == 1) {
                                updateDatabase(sharedPreferences, editor);

                            }

                            Integer userTopScore = snapshot.child(getUserId).child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                            Integer userScore2 = snapshot.child(getUserId).child(getString(R.string.key_topScore2)).getValue(Integer.class);
                            Integer userScore3 = snapshot.child(getUserId).child(getString(R.string.key_topScore3)).getValue(Integer.class);
                            Integer userScore4 = snapshot.child(getUserId).child(getString(R.string.key_topScore4)).getValue(Integer.class);
                            Integer userScore5 = snapshot.child(getUserId).child(getString(R.string.key_topScore5)).getValue(Integer.class);
                            Integer userCur = snapshot.child(getUserId).child(getString(R.string.childRef_Currency)).getValue(Integer.class);

                            editor.putString(getString(R.string.profileTopScore), Integer.toString(userTopScore));
                            editor.putString(getString(R.string.profileScore2), Integer.toString(userScore2));
                            editor.putString(getString(R.string.profileScore3), Integer.toString(userScore3));
                            editor.putString(getString(R.string.profileScore4), Integer.toString(userScore4));
                            editor.putString(getString(R.string.profileScore5), Integer.toString(userScore5));
                            editor.putString(getString(R.string.profileCur), Integer.toString(userCur));
                            editor.apply();

                            // When creating this fragment, originally all the code was here, now it used a function for the purpose
                            loadText();

                        } else {
                            Log.d(getString(R.string.TAG_FAILED), getString(R.string.TAG_FAILED));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if(typeOFLogin.equals(getString(R.string.geargoogleaccount))){
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                globalId = getUserId;
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference uidRef = db.child(getString(R.string.users)).child(getUserId);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();

                            int Checker = sharedPreferences.getInt(getString(R.string.checker), offlineMode);
                            if (Checker == 1) {
                                updateDatabase(sharedPreferences, editor);

                            }

                            Integer userTopScore = snapshot.child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                            Integer userScore2 = snapshot.child(getString(R.string.key_topScore2)).getValue(Integer.class);
                            Integer userScore3 = snapshot.child(getString(R.string.key_topScore3)).getValue(Integer.class);
                            Integer userScore4 = snapshot.child(getString(R.string.key_topScore4)).getValue(Integer.class);
                            Integer userScore5 = snapshot.child(getString(R.string.key_topScore5)).getValue(Integer.class);
                            Integer userCur = snapshot.child(getString(R.string.childRef_Currency)).getValue(Integer.class);

                            editor.putString(getString(R.string.profileTopScore), Integer.toString(userTopScore));
                            editor.putString(getString(R.string.profileScore2), Integer.toString(userScore2));
                            editor.putString(getString(R.string.profileScore3), Integer.toString(userScore3));
                            editor.putString(getString(R.string.profileScore4), Integer.toString(userScore4));
                            editor.putString(getString(R.string.profileScore5), Integer.toString(userScore5));
                            editor.putString(getString(R.string.profileCur), Integer.toString(userCur));
                            editor.apply();

                            // When creating this fragment, originally all the code was here, now it used a function for the purpose
                            loadText();
                        }else{
                            Log.d(getString(R.string.TAG_FAILED), getString(R.string.TAG_FAILED));
                        }
                    }
                });

            }
            else{
                Log.d(getString(R.string.TAG_FAILED), getString(R.string.TAG_FAILED));
            }

        }
    }

    public void checkPermission() {
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

    public void loadText() {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            String loadName = sharedPreferences.getString(getString(R.string.profileUsername), profileUser);
            String loadCur = sharedPreferences.getString(getString(R.string.profileCur), profileCur);
            String loadScore = sharedPreferences.getString(getString(R.string.profileTopScore), profileScore);
            usernameTextView.setText(getString(R.string.usernameDisplay) + loadName);
            currencyTextView.setText(getString(R.string.currencyDisplay) + loadCur + getString(R.string.gears));
            topScoreTextView.setText(getString(R.string.scoreDisplay) + loadScore);
        }
    }

    public void logOut(){
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
                        editor.putString(getString(R.string.profileUsername), getString(R.string.blank));
                        editor.putString(getString(R.string.profileTopScore), getString(R.string.blank));
                        editor.putString(getString(R.string.profileScore2), getString(R.string.blank));
                        editor.putString(getString(R.string.profileScore3), getString(R.string.blank));
                        editor.putString(getString(R.string.profileScore4), getString(R.string.blank));
                        editor.putString(getString(R.string.profileScore5), getString(R.string.blank));
                        editor.putString(getString(R.string.profileCur), getString(R.string.blank));
                        editor.apply();

                        if (typeOFsignout.equals(R.string.geargoogleaccount)){
                            FirebaseAuth.getInstance().signOut();
                            getActivity().finish();
                        }else{
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

    public void updateDatabase(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String topScr = sharedPreferences.getString(getString(R.string.profileTopScore), profileScore);
        String topScr2 = sharedPreferences.getString(getString(R.string.profileScore2), profileScore);
        String topScr3 = sharedPreferences.getString(getString(R.string.profileScore3), profileScore);
        String topScr4 = sharedPreferences.getString(getString(R.string.profileScore4), profileScore);
        String topScr5 = sharedPreferences.getString(getString(R.string.profileScore5), profileScore);

        mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore)).setValue(Integer.parseInt(topScr));
        mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore2)).setValue(Integer.parseInt(topScr2));
        mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore3)).setValue(Integer.parseInt(topScr3));
        mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore4)).setValue(Integer.parseInt(topScr4));
        mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore5)).setValue(Integer.parseInt(topScr5));

        offlineMode = 0;
        editor.putInt(getString(R.string.checker), offlineMode);
        editor.apply();
    }

    public void scoreUpdate() {
        if (inputScore.getText().toString().trim().length() > 0) {
            int i = Integer.parseInt(inputScore.getText().toString());

            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
            else {
                connected = false;
            }
            if (connected == true) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference uidRef = db.child(getString(R.string.users)).child(globalId);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            Integer userCurrentCurr = snapshot.child(getString(R.string.key_topScore)).getValue(Integer.class);
                            Integer userScore2 = snapshot.child(getString(R.string.key_topScore2)).getValue(Integer.class);
                            Integer userScore3 = snapshot.child(getString(R.string.key_topScore3)).getValue(Integer.class);
                            Integer userScore4 = snapshot.child(getString(R.string.key_topScore4)).getValue(Integer.class);
                            Integer userScore5 = snapshot.child(getString(R.string.key_topScore5)).getValue(Integer.class);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        if (i > userCurrentCurr.intValue() ){
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore)).setValue(i);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore2)).setValue(userCurrentCurr);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore3)).setValue(userScore2);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore4)).setValue(userScore3);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore5)).setValue(userScore4);
                            topScoreTextView.setText(getString(R.string.scoreDisplay) + i);
                        } else if (i > userScore2.intValue()) {
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore2)).setValue(i);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore3)).setValue(userScore2);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore4)).setValue(userScore3);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore5)).setValue(userScore4);
                        } else if (i > userScore3.intValue()) {
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore3)).setValue(i);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore4)).setValue(userScore3);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore5)).setValue(userScore4);
                        } else if (i > userScore4.intValue()) {
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore4)).setValue(i);
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore4)).setValue(userScore4);
                        } else if (i > userScore5.intValue()) {
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(globalId).child(getString(R.string.key_topScore5)).setValue(i);
                        }

                        } else {
                            Log.d(getString(R.string.TAG_FAILED), getString(R.string.TAG_FAILED));
                        }
                    }
                });
            } else {
                SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                offlineMode = 1;
                String topScr = sharedPreferences.getString(getString(R.string.profileTopScore), profileUser);
                String topScr2 = sharedPreferences.getString(getString(R.string.profileScore2), profileCur);
                String topScr3 = sharedPreferences.getString(getString(R.string.profileScore3), profileScore);
                String topScr4 = sharedPreferences.getString(getString(R.string.profileScore4), profileScore);
                String topScr5 = sharedPreferences.getString(getString(R.string.profileScore5), profileScore);
                editor.putInt(getString(R.string.checker), offlineMode);

                if (i > Integer.parseInt(topScr) ){
                    editor.putString(getString(R.string.profileTopScore), String.valueOf(i));
                    editor.putString(getString(R.string.profileScore2), topScr);
                    editor.putString(getString(R.string.profileScore3), topScr2);
                    editor.putString(getString(R.string.profileScore4), topScr3);
                    editor.putString(getString(R.string.profileScore5), topScr4);
                } else if (i > Integer.parseInt(topScr2) ) {
                    editor.putString(getString(R.string.profileScore2), String.valueOf(i));
                    editor.putString(getString(R.string.profileScore3), topScr2);
                    editor.putString(getString(R.string.profileScore4), topScr3);
                    editor.putString(getString(R.string.profileScore5), topScr4);
                }
                else if (i > Integer.parseInt(topScr3) ) {
                    editor.putString(getString(R.string.profileScore3), String.valueOf(i));
                    editor.putString(getString(R.string.profileScore4), topScr3);
                    editor.putString(getString(R.string.profileScore5), topScr4);
                }
                else if (i > Integer.parseInt(topScr4) ) {
                    editor.putString(getString(R.string.profileScore4), String.valueOf(i));
                    editor.putString(getString(R.string.profileScore5), topScr4);
                }
                else if (i > Integer.parseInt(topScr5) ) {
                    editor.putString(getString(R.string.profileScore5), String.valueOf(i));
                }
                editor.apply();

            }
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
        mSimulateScore = view.findViewById(R.id.nikaSimulateScore);
        usernameTextView = (TextView) view.findViewById(R.id.nikaUsername);
        topScoreTextView = (TextView) view.findViewById(R.id.nikaTopScore);
        currencyTextView = (TextView) view.findViewById(R.id.nikaCurrency);
        inputScore = (EditText) view.findViewById(R.id.nikaInputScore);
        btn = view.findViewById(R.id.nikaLogout);

        loadImage();
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else {
            connected = false;
        }
        if (connected == true) {
            loadProfile();
        } else {
            loadText();
        }

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        mSimulateScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreUpdate();
            }
        });


        return view;
    }

}