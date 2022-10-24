package ca.nika.it.gear5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RetailStartUpScreenActivity extends AppCompatActivity {
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_start_up_screen);

        loginButton = (Button) findViewById(R.id.nika_retail_login_btn);
        registerButton = (Button) findViewById(R.id.nika_retail_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetailStartUpScreenActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}