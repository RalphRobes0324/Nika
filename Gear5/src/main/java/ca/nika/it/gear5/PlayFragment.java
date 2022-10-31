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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PlayFragment extends Fragment {
    Button btnA;
    Button btnB;
    private View v;

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

        loadGameSetting();
        return v;
    }

    public void doSave() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        getParentFragmentManager().setFragmentResultListener("getBundlePass", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int num = result.getInt("bundlePass");
                editor.putInt("Index", num);

            }
        });
        editor.apply();

    }

    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            int num = sharedPreferences.getInt("Index", R.id.nikaRB4);

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
        }

    }
}