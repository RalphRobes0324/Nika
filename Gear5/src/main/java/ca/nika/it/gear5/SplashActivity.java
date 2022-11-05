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
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");

        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (checkbox.equals("true")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else if (checkbox.equals("false")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class)); //change to login activity when done
                    finish();
                }
            }
        }, 2000);
    }
}