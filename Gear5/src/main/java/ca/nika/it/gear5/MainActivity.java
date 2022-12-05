// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.nika.it.gear5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    String typeOFsignout, globalId;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        if (connected) {
            loadDataIntoSharedPreference();
        }

            binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.nikaBottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.play:
                    replaceFragment(new PlayFragment());
                    break;
                case R.id.score:
                    this.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    replaceFragment(new ScoreFragment());
                    break;
                case R.id.profile:
                    this.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nikaFrameLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);


        menu.findItem(R.id.nikaSettings).getActionView().setOnClickListener(view -> {
            this.replaceFragment(new SettingsFragment());

        });

        return true;
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.Gear5)
                .setMessage(R.string.ExitApp)
                .setIcon(R.drawable.devil_fruit)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void loadDataIntoSharedPreference() {
        SharedPreferences sharedPreferences= this.getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences!= null) {
            String typeOFLogin = sharedPreferences.getString("typeLogin", getString(R.string.blank));
            typeOFsignout = typeOFLogin;
            if(typeOFLogin.equals("GearAccount")) {
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                globalId = getUserId;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
                Query checkUser = reference.orderByChild(getString(R.string.childRef_username)).equalTo(getUserId);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String userName = snapshot.child(getUserId).child(getString(R.string.childRef_username)).getValue(String.class);
                            Integer userTopScore = snapshot.child(getUserId).child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                            Integer userScore2 = snapshot.child(getUserId).child("topScore2").getValue(Integer.class);
                            Integer userScore3 = snapshot.child(getUserId).child("topScore3").getValue(Integer.class);
                            Integer userScore4 = snapshot.child(getUserId).child("topScore4").getValue(Integer.class);
                            Integer userScore5 = snapshot.child(getUserId).child("topScore5").getValue(Integer.class);
                            Integer userCur = snapshot.child(getUserId).child(getString(R.string.childRef_Currency)).getValue(Integer.class);


                            editor.putString("profileUsername", userName);
                            editor.putString("profileTopScore", Integer.toString(userTopScore));
                            editor.putString("profileScore2", Integer.toString(userScore2));
                            editor.putString("profileScore3", Integer.toString(userScore3));
                            editor.putString("profileScore4", Integer.toString(userScore4));
                            editor.putString("profileScore5", Integer.toString(userScore5));
                            editor.putString("profileCur", Integer.toString(userCur));
                            editor.apply();

                        } else {
                            Log.d("FAILED", "FAILED GEAR");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if(typeOFLogin.equals("GearGoogleAccount")){
                String getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
                globalId = getUserId;
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference uidRef = db.child("users").child(getUserId);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();
                            String userName = snapshot.child(getString(R.string.childRef_username)).getValue(String.class);
                            Integer userTopScore = snapshot.child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                            Integer userScore2 = snapshot.child("topScore2").getValue(Integer.class);
                            Integer userScore3 = snapshot.child("topScore3").getValue(Integer.class);
                            Integer userScore4 = snapshot.child("topScore4").getValue(Integer.class);
                            Integer userScore5 = snapshot.child("topScore5").getValue(Integer.class);
                            Integer userCur = snapshot.child(getString(R.string.childRef_Currency)).getValue(Integer.class);

                            editor.putString("profileUsername", userName);
                            editor.putString("profileTopScore", Integer.toString(userTopScore));
                            editor.putString("profileScore2", Integer.toString(userScore2));
                            editor.putString("profileScore3", Integer.toString(userScore3));
                            editor.putString("profileScore4", Integer.toString(userScore4));
                            editor.putString("profileScore5", Integer.toString(userScore5));
                            editor.putString("profileCur", Integer.toString(userCur));
                            editor.apply();
                        }else{
                            Log.d("FAILED", "FAILED GOOGLE LOAD PROF");
                        }
                    }
                });

            }
            else{
                Log.d("FAILED", "FAILED");
            }

        }
    }



    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nikaSettings) {
            replaceFragment(new SettingsFragment());
            return true;
        }
        if (id == R.id.nikaReview) {
            replaceFragment(new ReviewFragment());
            return true;
        }
        if (id == R.id.nikaBalance) {
            replaceFragment(new BalanceFragment());
            return true;
        }
        if (id == R.id.nikaConnect) {
            replaceFragment(new BluetoothFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}