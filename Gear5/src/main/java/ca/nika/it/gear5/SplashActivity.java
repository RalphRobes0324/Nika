// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import ca.nika.it.gear5.LoginSetup.LoginActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.checkbox), MODE_PRIVATE);
        String checkbox = preferences.getString(getString(R.string.remember),getString(R.string.blank));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //This utilizes KISS as everything is very simplified and easy for anyone to understand, it checks the current activity and if the checkbox is true to determine where to go
                if (checkbox.equals(getString(R.string.checked))) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}