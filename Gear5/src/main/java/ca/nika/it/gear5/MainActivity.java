// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ca.nika.it.gear5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //replaceFragment(new ProfileFragment());

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