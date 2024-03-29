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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BalanceFragment extends Fragment{

    PreferenceManager preferenceManager;

    EditText editTextNumber,editTextEXP,editTextCVV;
    AlertDialog dialog;
    Button pay;
    String num,exp,cvv, getUserID, typeOFsignout;;
    int amount;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        //Share Preferences
        preferenceManager = PreferenceManager.getInstance(getActivity());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        //Getting data from Share Preferences
        getUserID = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));

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
                                amount = 500;
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
                                amount = 1000;
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
                                amount = 2200;
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
                                amount = 5700;
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
                                amount = 11600;
                                openDialog();
                            }
                        })
                        .setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });

        return view;
    }

    private void updateUserCurrFirebase() {

        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            String typeOFLogin = sharedPreferences.getString(getString(R.string.key_typelogin), getString(R.string.blank));
            typeOFsignout = typeOFLogin;
            if(typeOFLogin.equals(getString(R.string.gearaccount))) {
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
                Query checkUser = reference.orderByChild(getString(R.string.childRef_username)).equalTo(getUserId);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Integer userCurrentCurr = snapshot.child(getUserID).child(getString(R.string.childRef_Currency)).getValue(Integer.class);
                            int sum = userCurrentCurr.intValue() + amount;
                            Integer newSum = new Integer(sum);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(getUserID).child(getString(R.string.childRef_Currency)).setValue(newSum);

                        } else {
                            Log.d(getString(R.string.FAILED), getString(R.string.FAILED));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if(typeOFLogin.equals(getString(R.string.geargoogleaccount))){
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference uidRef = db.child(getString(R.string.users)).child(getUserId);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();
                            Integer userCurrentCurr = snapshot.child(getString(R.string.childRef_Currency)).getValue(Integer.class);
                            int sum = userCurrentCurr.intValue() + amount;
                            Integer newSum = new Integer(sum);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(getUserID).child(getString(R.string.childRef_Currency)).setValue(newSum);
                        }else{
                            Log.d(getString(R.string.TAG_FAILED), getString(R.string.ERROR_MSG_BALAFRAG));
                        }
                    }
                });

            }
            else{
                Log.d(getString(R.string.TAG_FAILED), getString(R.string.TAG_FAILED));
            }

        }
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

    //builder design pattern, alertdialog.builder that user can interact with
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

                //This utilizes KISS as everything is very simplified and easy for anyone to understand
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
                    updateUserCurrFirebase();
                    dialog.dismiss();
                    sendNotification();
                }
            }
        });

        dialog.show();
    }
}