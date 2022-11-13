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
import android.widget.RadioGroup;

public class SettingsFragment extends Fragment {

    private RadioGroup radioBtnClr;
    private CheckBox muteCB;
    private CheckBox lockCB;
    private CheckBox statsCB;
    private View v;
    Boolean aBoolean1;
    Boolean aBoolean2;
    Boolean aBoolean3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_settings, container, false);


        radioBtnClr = v.findViewById(R.id.nikaRG1);

        this.muteCB = this.v.findViewById(R.id.nikaCB1);
        this.lockCB = this.v.findViewById(R.id.nikaCB2);
        this.statsCB = this.v.findViewById(R.id.nikaCB3);

        aBoolean1 = muteCB.isChecked();
        aBoolean2 = lockCB.isChecked();
        aBoolean3 = statsCB.isChecked();


        Button buttonSave = (Button) this.v.findViewById(R.id.button);
        CheckBox stats = (CheckBox) v.findViewById(R.id.nikaCB3);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment.this.doSave();

                Bundle bundle = new Bundle();
                int radioButtonID = radioBtnClr.getCheckedRadioButtonId();
                View radioButton = radioBtnClr.findViewById(radioButtonID);
                int idx = radioBtnClr.indexOfChild(radioButton);
                bundle.putInt(getString(R.string.bundlePass),idx);
                getParentFragmentManager().setFragmentResult(getString(R.string.getBundlePass),bundle);

            }
        });

        stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.SettingsPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.stats), getString(R.string.checked));
                    editor.apply();

                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.SettingsPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.stats), getString(R.string.unchecked));
                    editor.apply();
                }
            }
        });

        CheckBox audio = (CheckBox) v.findViewById(R.id.nikaCB1);

        audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.SettingsPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.audio), getString(R.string.checked));
                    editor.apply();

                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.SettingsPref), MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.audio), getString(R.string.unchecked));
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

        int checkedRadioButtonId2 = radioBtnClr.getCheckedRadioButtonId();

        boolean getBoolean1 = muteCB.isChecked();
        boolean getBoolean2 = lockCB.isChecked();
        boolean getBoolean3 = statsCB.isChecked();

        Activity a = getActivity();
        if (getBoolean2 == true) {
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        editor.putInt(getString(R.string.checkRadioButtonId2), checkedRadioButtonId2);

        editor.putBoolean(getString(R.string.getBooleanId), getBoolean1);
        editor.putBoolean(getString(R.string.getBooleanId2), getBoolean2);
        editor.putBoolean(getString(R.string.getBooleanId3), getBoolean3);

        editor.apply();

    }

    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            int checkedRadioButtonId2 = sharedPreferences.getInt(getString(R.string.checkRadioButtonId2), R.id.nikaRB4);
            boolean getBoolean = sharedPreferences.getBoolean(getString(R.string.getBooleanId), aBoolean1);
            boolean getBoolean2 = sharedPreferences.getBoolean(getString(R.string.getBooleanId2), aBoolean2);
            boolean getBoolean3 = sharedPreferences.getBoolean(getString(R.string.getBooleanId3), aBoolean3);

            this.radioBtnClr.check(checkedRadioButtonId2);
            this.muteCB.setChecked(getBoolean);
            this.lockCB.setChecked(getBoolean2);
            this.statsCB.setChecked(getBoolean3);

        } else {
            this.radioBtnClr.check(R.id.nikaRB4);
            this.muteCB.setChecked(false);
            this.lockCB.setChecked(false);
            this.statsCB.setChecked(false);
        }

    }





}