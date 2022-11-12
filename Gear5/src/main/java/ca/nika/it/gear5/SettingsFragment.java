// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
    private RadioGroup radioButton;

    private CheckBox checkbox;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    Boolean aBoolean;
    Boolean aBoolean2;
    Boolean aBoolean3;

    private RadioGroup radioJoyClr;
    private RadioGroup radioBtnClr;

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

        this.checkbox = (CheckBox) this.v.findViewById(R.id.nikaCB1);
        this.checkbox2 = (CheckBox) this.v.findViewById(R.id.nikaCB2);
        this.checkbox3 = (CheckBox) this.v.findViewById(R.id.nikaCB3);

        aBoolean = checkbox.isChecked();
        aBoolean2 = checkbox2.isChecked();
        aBoolean3 = checkbox3.isChecked();


        radioJoyClr = v.findViewById(R.id.nikaRG1);
        radioBtnClr = v.findViewById(R.id.nikaRG2);


        Button buttonSave = (Button) this.v.findViewById(R.id.button);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment.this.doSave();

                Bundle bundle = new Bundle();
                int radioButtonID = radioBtnClr.getCheckedRadioButtonId();
                View radioButton = radioBtnClr.findViewById(radioButtonID);
                int idx = radioBtnClr.indexOfChild(radioButton);
                bundle.putInt("bundlePass",idx);

                radioButtonID = radioJoyClr.getCheckedRadioButtonId();
                radioButton = radioJoyClr.findViewById(radioButtonID);
                idx = radioJoyClr.indexOfChild(radioButton);
                bundle.putInt("bundlePass2",idx);
                getParentFragmentManager().setFragmentResult("getBundlePass",bundle);

            }
        });

        CheckBox stats = (CheckBox) v.findViewById(R.id.nikaCB3);

        stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("SettingsPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("stats", getString(R.string.checked));
                    editor.apply();

                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("SettingsPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("stats", getString(R.string.unchecked));
                    editor.apply();
                }
            }
        });

        CheckBox audio = (CheckBox) v.findViewById(R.id.nikaCB1);

        audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("SettingsPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("audio", getString(R.string.checked));
                    editor.apply();

                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("SettingsPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("audio", getString(R.string.unchecked));
                    editor.apply();
                }
            }
        });

        this.loadGameSetting();
        return v;
    }


    public void doSave()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int checkedRadioButtonId = radioJoystick.getCheckedRadioButtonId();
        int checkedRadioButtonId2 = radioButton.getCheckedRadioButtonId();

        Boolean getBoolean = checkbox.isChecked();
        Boolean getBoolean2 = checkbox2.isChecked();
        Boolean getBoolean3 = checkbox3.isChecked();

        Activity a = getActivity();
        if (getBoolean2 == true) {
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        editor.putInt(getString(R.string.checkRadioButtonId), checkedRadioButtonId);
        editor.putInt(getString(R.string.checkRadioButtonId2), checkedRadioButtonId2);

        editor.putBoolean(getString(R.string.getBooleanId), getBoolean);
        editor.putBoolean(getString(R.string.getBooleanId2), getBoolean2);
        editor.putBoolean("getBooleanId3", getBoolean3);

        editor.apply();

    }

    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            int checkedRadioButtonId = sharedPreferences.getInt(getString(R.string.checkRadioButtonId), R.id.nikaRB1);
            int checkedRadioButtonId2 = sharedPreferences.getInt(getString(R.string.checkRadioButtonId2), R.id.nikaRB4);
            boolean getBoolean = sharedPreferences.getBoolean(getString(R.string.getBooleanId), aBoolean);
            boolean getBoolean2 = sharedPreferences.getBoolean(getString(R.string.getBooleanId2), aBoolean2);
            boolean getBoolean3 = sharedPreferences.getBoolean("getBooleanId3", aBoolean3);

            this.radioJoystick.check(checkedRadioButtonId);
            this.radioButton.check(checkedRadioButtonId2);
            this.checkbox.setChecked(getBoolean);
            this.checkbox2.setChecked(getBoolean2);
            this.checkbox3.setChecked(getBoolean3);

        } else {
            this.radioJoystick.check(R.id.nikaRB1);
            this.radioButton.check(R.id.nikaRB4);
            this.checkbox.setChecked(aBoolean);
            this.checkbox2.setChecked(aBoolean2);
            this.checkbox3.setChecked(aBoolean3);
        }

    }





}