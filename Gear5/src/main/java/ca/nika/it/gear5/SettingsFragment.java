// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsFragment extends Fragment {
    private View v;
    private RadioGroup radioJoystick;
    private RadioButton radioJR;
    private RadioButton radioJY;
    private RadioButton radioJG;

    private RadioGroup radioButton;
    private RadioButton radioBR;
    private RadioButton radioBY;
    private RadioButton radioBG;

    private CheckBox checkbox;
    private CheckBox checkbox2;
    Boolean aBoolean;
    Boolean aBoolean2;

    private Button buttonSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_settings, container, false);

        radioJoystick = v.findViewById(R.id.nikaRG1);
        radioButton = v.findViewById(R.id.nikaRG2);

        this.radioJR = (RadioButton) this.v.findViewById(R.id.nikaRB1);
        this.radioJY = (RadioButton) this.v.findViewById(R.id.nikaRB2);
        this.radioJG = (RadioButton) this.v.findViewById(R.id.nikaRB3);

        this.radioBR = (RadioButton) this.v.findViewById(R.id.nikaRB4);
        this.radioBY = (RadioButton) this.v.findViewById(R.id.nikaRB5);
        this.radioBG = (RadioButton) this.v.findViewById(R.id.nikaRB6);

        this.checkbox = (CheckBox) this.v.findViewById(R.id.nikaCB1);
        this.checkbox2 = (CheckBox) this.v.findViewById(R.id.nikaCB2);

        aBoolean = checkbox.isChecked();
        aBoolean2 = checkbox2.isChecked();

        this.buttonSave = (Button) this.v.findViewById(R.id.button);

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment.this.doSave();

//                Bundle bundle = new Bundle();
//                int radioButtonID = radioJoystick.getCheckedRadioButtonId();
//                View radioButton = radioJoystick.findViewById(radioButtonID);
//                int idx = radioJoystick.indexOfChild(radioButton);
//                bundle.putInt("password",idx);
//                getParentFragmentManager().setFragmentResult("getPassword",bundle);
            }
        });

        this.loadGameSetting();
        return v;
    }

    public void doSave()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences("Settings3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int checkedRadioButtonId = radioJoystick.getCheckedRadioButtonId();
        int checkedRadioButtonId2 = radioButton.getCheckedRadioButtonId();

        Boolean getBoolean = checkbox.isChecked();
        Boolean getBoolean2 = checkbox2.isChecked();

        editor.putInt("checkRadioButtonId", checkedRadioButtonId);
        editor.putInt("checkRadioButtonId2", checkedRadioButtonId2);

        editor.putBoolean("getBooleanId", getBoolean);
        editor.putBoolean("getBooleanId2", getBoolean2);


        editor.apply();

    }

    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences("Settings3", Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            int checkedRadioButtonId = sharedPreferences.getInt("checkRadioButtonId", R.id.nikaRB1);
            int checkedRadioButtonId2 = sharedPreferences.getInt("checkRadioButtonId2", R.id.nikaRB4);
            boolean getBoolean = sharedPreferences.getBoolean("getBooleanId", aBoolean);
            boolean getBoolean2 = sharedPreferences.getBoolean("getBooleanId2", aBoolean2);

            this.radioJoystick.check(checkedRadioButtonId);
            this.radioButton.check(checkedRadioButtonId2);
            this.checkbox.setChecked(getBoolean);
            this.checkbox2.setChecked(getBoolean2);

        } else {
            this.radioJoystick.check(R.id.nikaRB1);
            this.radioButton.check(R.id.nikaRB4);
            this.checkbox.setChecked(aBoolean);
            this.checkbox2.setChecked(aBoolean2);
        }

    }

}