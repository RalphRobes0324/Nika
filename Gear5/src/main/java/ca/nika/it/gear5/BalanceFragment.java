// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class BalanceFragment extends Fragment{

    PreferenceManager preferenceManager;
    EditText editTextNumber,editTextEXP,editTextCVV;
    AlertDialog dialog;
    Button pay;
    String num,exp,cvv;

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(getString(R.string.g5_pay_notif),getString(R.string.g5_pay_notif), NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
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
                        .setPositiveButton(R.string.Continue,new DialogInterface.OnClickListener(){
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
                        .setPositiveButton(R.string.Continue,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openDialog();
                            }
                        })
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
                        .setPositiveButton(R.string.Continue,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openDialog();
                            }
                        })
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
                        .setPositiveButton(R.string.Continue,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openDialog();
                            }
                        })
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
                        .setPositiveButton(R.string.Continue,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openDialog();
                            }
                        })
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        return view;
    }

    public void sendNotification(){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext(),getString(R.string.g5_pay_notif));
        notification.setContentTitle(getString(R.string.g5_payment));
        notification.setContentTitle(getString(R.string.payment_processed));
        notification.setSmallIcon(R.drawable.devil_fruit);
        notification.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(1,notification.build());
    }

    public void openDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.payment);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog,null);

        editTextNumber = view.findViewById(R.id.edit_card_num);
        editTextEXP = view.findViewById(R.id.edit_card_exp);
        editTextCVV = view.findViewById(R.id.edit_card_cvv);
        pay = view.findViewById(R.id.pay_button);

        builder.setView(view);

        dialog = builder.create();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                num = editTextNumber.getText().toString();
                exp = editTextEXP.getText().toString();
                cvv = editTextCVV.getText().toString();

                if(num.isEmpty()){
                    editTextNumber.setError(getString(R.string.card_num_req));
                    editTextNumber.requestFocus();
                }

                else if(num.length()!=16){
                    editTextNumber.setError(getString(R.string.card_num_length_req));
                    editTextNumber.requestFocus();
                }

                else if(exp.isEmpty()){
                    editTextEXP.setError(getString(R.string.card_exp_req));
                    editTextEXP.requestFocus();
                }

                else if(exp.length()!=4){
                    editTextEXP.setError(getString(R.string.card_exp_length_req));
                    editTextEXP.requestFocus();
                }

                else if(cvv.isEmpty()){
                    editTextCVV.setError(getString(R.string.card_cvv_req));
                    editTextCVV.requestFocus();
                }

                else if(cvv.length()!=3){
                    editTextCVV.setError(getString(R.string.card_cvv_length_req));
                    editTextCVV.requestFocus();
                }

                else{
                    dialog.dismiss();
                    sendNotification();
                }
            }
        });

        dialog.show();
    }
}