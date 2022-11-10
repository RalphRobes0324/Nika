package ca.nika.it.gear5.testFile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ca.nika.it.gear5.R;

public class TestMainActivity extends AppCompatActivity {
    TextView name;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        name = findViewById(R.id.textView6);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String _name = account.getDisplayName();
            name.setText(_name);

        }


    }
}