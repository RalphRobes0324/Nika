// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.nika.it.gear5.LoginSetup.LoginActivity;
import ca.nika.it.gear5.databinding.ActivityMainBinding;

public class PlayFragment extends Fragment {
    Button btnA;
    Button btnB;
    private View v;
    RelativeLayout layout_joystick;
    TextView textView1, textView2, textView3, textView4, textView5;
    LinearLayout ll;

    JoyStickClass js;
    int JoystickColor;

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

        this.btnA = (Button) this.v.findViewById(R.id.nikaButtonA);
        this.btnB = (Button) this.v.findViewById(R.id.nikaButtonB);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        getParentFragmentManager().setFragmentResultListener("getBundlePass", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int num = result.getInt("bundlePass");
                editor.putInt("Index", num);
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
        } else {

        }

        loadGameSetting();

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
//        js.setLayoutAlpha(150);
//        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));
                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
                    if(direction == JoyStickClass.STICK_UP) {
                        textView5.setText("Direction : Up");
                    } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                    } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                    } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                    } else if(direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("Direction : Center");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");
                    textView5.setText("Direction :");
                }
                return true;
            }
        });
        return v;
    }


    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        String checkbox = sharedPreferences.getString("stats",getString(R.string.blank));
        ll = (LinearLayout) v.findViewById(R.id.linearLayout1);

        if(sharedPreferences!= null) {
            int num = sharedPreferences.getInt("Index", R.id.nikaRB4);

            if (checkbox.equals(getString(R.string.checked))) {
                ll.setVisibility(ll.VISIBLE);
            } else {
                ll.setVisibility(ll.INVISIBLE);
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

        }

    }
}