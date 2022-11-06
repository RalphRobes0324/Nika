// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayFragment extends Fragment {
    Button btnA;
    Button btnB;
    private View v;
    RelativeLayout layout_joystick;
    TextView textView1, textView2, textView3, textView4, textView5;
    int num2;

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
                int num2 = result.getInt("bundlePass2");
                editor.putInt("Index", num);
                editor.putInt("Index2", num2);
                editor.apply();
                loadGameSetting();

            }
        });

        loadGameSetting();

        textView1 = (TextView) v.findViewById(R.id.textView1);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        textView4 = (TextView) v.findViewById(R.id.textView4);
        textView5 = (TextView) v.findViewById(R.id.textView5);

        layout_joystick = (RelativeLayout) v.findViewById(R.id.layout_joystick);

        switch (num2) {
            case 0:
                JoystickColor = R.drawable.red_circle;
                break;
            case 1:
                JoystickColor = R.drawable.yellow_circle;
                break;
            case 2:
                JoystickColor = R.drawable.green_circle;
                break;
        }

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

        if(sharedPreferences!= null) {
            int num = sharedPreferences.getInt("Index", R.id.nikaRB4);
            num2 = sharedPreferences.getInt("Index2", R.id.nikaRB1);

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

            num2 = 0;
        }

    }
}