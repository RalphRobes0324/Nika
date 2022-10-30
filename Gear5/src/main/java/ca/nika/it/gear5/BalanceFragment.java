package ca.nika.it.gear5;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BalanceFragment extends Fragment {

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        .setTitle("500 Gears")
                        .setMessage("Purchase 500 Gears?")
                        .setPositiveButton("Confirm",null)
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

        gear1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("1000 Gears")
                        .setMessage("Purchase 1000 Gears?")
                        .setPositiveButton("Confirm",null)
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

        gear2200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("2200 Gears")
                        .setMessage("Purchase 2200 Gears?")
                        .setPositiveButton("Confirm",null)
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

        gear5700.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("5700 Gears")
                        .setMessage("Purchase 5700 Gears?")
                        .setPositiveButton("Confirm",null)
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

        gear11600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("11600 Gears")
                        .setMessage("Purchase 11600 Gears?")
                        .setPositiveButton("Confirm",null)
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

        return view;
    }
}