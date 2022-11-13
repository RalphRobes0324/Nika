// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BalanceFragment extends Fragment{

    PreferenceManager preferenceManager;

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void loadImage() {
        preferenceManager = PreferenceManager.getInstance(getActivity());


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if (sharedPreferences != null) {

            String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        ImageButton gear500=(ImageButton) view.findViewById(R.id.gear5_coin_500);
        ImageButton gear1000=(ImageButton) view.findViewById(R.id.gear5_coin_1000);
        ImageButton gear2200=(ImageButton) view.findViewById(R.id.gear5_coin_2200);
        ImageButton gear5700=(ImageButton) view.findViewById(R.id.gear5_coin_5700);
        ImageButton gear11600=(ImageButton) view.findViewById(R.id.gear5_coin_11600);

        //check for which was pressed
        gear500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.gear500title)
                        .setMessage(R.string.gear500msg)
                        .setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openDialog();
                            }
                        })
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        gear1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.gear1000title)
                        .setMessage(R.string.gear1100msg)
                        .setPositiveButton(R.string.confirm,null)
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        gear2200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.gear2200title)
                        .setMessage(R.string.gear2200msg)
                        .setPositiveButton(R.string.confirm,null)
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        gear5700.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.gear5700title)
                        .setMessage(R.string.gear5700msg)
                        .setPositiveButton(R.string.confirm,null)
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        gear11600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.gear11600title)
                        .setMessage(R.string.gear11600msg)
                        .setPositiveButton(R.string.confirm,null)
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        return view;
    }

    public void openDialog() {
        PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.show(getFragmentManager(),"payment dialog");
    }

}