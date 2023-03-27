// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlayFragment extends Fragment {
    Button btnA;
    Button btnB;
    private View v;
    RelativeLayout layout_joystick;
    TextView textView1, textView2, textView3, textView4, textView5;
    LinearLayout ll;
    Boolean playAudio;
    JoyStickClass js;
    int JoystickColor;
    String direction = "UP";


    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_play, container, false);

        //Ref to database

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = db.child(getString(R.string.users)).child("admin2");

        this.btnA = (Button) this.v.findViewById(R.id.nikaButtonA);
        this.btnB = (Button) this.v.findViewById(R.id.nikaButtonB);
        final MediaPlayer ButtonClickMP = MediaPlayer.create(getActivity(), R.raw.btn_click_sound);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        getParentFragmentManager().setFragmentResultListener(getString(R.string.getBundlePass), this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int num = result.getInt(getString(R.string.bundlePass));
                editor.putInt(getString(R.string.getIndex), num);
                editor.apply();
                loadGameSetting();
            }
        });

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            this.getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        loadGameSetting();

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playAudio) {
                    ButtonClickMP.start();
                }
            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playAudio) {
                    ButtonClickMP.start();
                }
            }
        });

        textView1 = (TextView) v.findViewById(R.id.textView1);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        textView4 = (TextView) v.findViewById(R.id.textView4);
        textView5 = (TextView) v.findViewById(R.id.textView5);

        layout_joystick = (RelativeLayout) v.findViewById(R.id.layout_joystick);


        JoystickColor = R.drawable.red_circle;

        js = new JoyStickClass(getActivity().getApplicationContext(), layout_joystick, JoystickColor);
        js.setStickSize(270, 270);
        js.setLayoutSize(500, 500);
        js.setOffset(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText(getString(R.string.x_display) + String.valueOf(js.getX()));
                    textView2.setText(getString(R.string.y_display) + String.valueOf(js.getY()));
                    textView3.setText(getString(R.string.angle_display) + String.valueOf(js.getAngle()));
                    textView4.setText(getString(R.string.distance_display) + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
                    if(direction == JoyStickClass.STICK_UP) {
                        textView5.setText(R.string.direction_up);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "UP";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });

                    } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText(R.string.direction_up_right);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "RIGHT";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText(R.string.direction_right);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "RIGHT";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText(R.string.direction_down_right);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "RIGHT";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText(R.string.direction_down);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "DOWN";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText(R.string.direction_down_left);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "LEFT";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText(R.string.direction_left);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "LEFT";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText(R.string.direction_up_left);
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String curDir = "LEFT";
                                    String newUserDirStatus = new String(curDir);
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child(getString(R.string.childRef_reg_regFrag)).child("admin2").child("v_input").setValue(newUserDirStatus);
                                }else{
                                    Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                                }
                            }
                        });
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        textView5.setText(R.string.direction_center);
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText(getString(R.string.x_display));
                    textView2.setText(getString(R.string.y_display));
                    textView3.setText(getString(R.string.angle_display));
                    textView4.setText(getString(R.string.distance_display));
                    textView5.setText(getString(R.string.direction_display));
                }
                return true;
            }
        });
        return v;
    }


    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        String statsCB = sharedPreferences.getString(getString(R.string.stats),getString(R.string.blank));
        String audioCB = sharedPreferences.getString(getString(R.string.audio),getString(R.string.blank));
        ll = (LinearLayout) v.findViewById(R.id.linearLayout1);

        if(sharedPreferences!= null) {
            int num = sharedPreferences.getInt(getString(R.string.getIndex), R.id.nikaRB4);

            if (statsCB.equals(getString(R.string.checked))) {
                ll.setVisibility(ll.VISIBLE);
            } else {
                ll.setVisibility(ll.INVISIBLE);
            }

            if (audioCB.equals(getString(R.string.checked))) {
                playAudio = true;
            } else {
                playAudio = false;
            }

            switch (num) {
                case 0:
                    btnA.setBackgroundColor(btnA.getContext().getResources().getColor(R.color.red));
                    btnB.setBackgroundColor(btnB.getContext().getResources().getColor(R.color.red));
                    break;
                case 1:
                    btnA.setBackgroundColor(btnA.getContext().getResources().getColor(R.color.yellow));
                    btnB.setBackgroundColor(btnB.getContext().getResources().getColor(R.color.yellow));
                    break;
                case 2:
                    btnA.setBackgroundColor(btnA.getContext().getResources().getColor(R.color.green));
                    btnB.setBackgroundColor(btnB.getContext().getResources().getColor(R.color.green));
                    break;
            }

        } else {
            btnA.setBackgroundColor(btnA.getContext().getResources().getColor(R.color.red));
            btnB.setBackgroundColor(btnB.getContext().getResources().getColor(R.color.red));
            ll.setVisibility(ll.INVISIBLE);
            playAudio = false;
        }

    }
}