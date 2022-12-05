// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5.LoginSetup;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import ca.nika.it.gear5.MainActivity;
import ca.nika.it.gear5.R;
import ca.nika.it.gear5.SignInFile.GoogleSignInActivity;


public class LoginFragment extends Fragment {

    private Button registerBtn, loginBtn, forgortpwdBtn;
    private CheckBox remember;
    private EditText usernameInput, passwordInput;
    private LinearLayout googleBtn;
    private ImageView backButton;

    /*
    DONT REMOVE HARD STRINGS OR ELSE IT WILL CRASH THE LOGIN/REGIS
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[!@#$%^&+=*])"+     // at least 1 special character
                    "(?=.*[A-Z])" +
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 6 characters
                    "$");




    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_reg, R.anim.enter_login_from_reg);
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void reverseReplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //createRequest();

        //
        registerBtn = (Button) view.findViewById(R.id.nika_btn_login_reg);
        forgortpwdBtn = (Button) view.findViewById(R.id.nika_btn_forgotPwd_loginFrag);
        loginBtn = (Button) view.findViewById(R.id.nika_btn_login_login);
        remember = (CheckBox) view.findViewById(R.id.rememberMe);
        backButton = (ImageView) view.findViewById(R.id.nika_login_back_button);

        usernameInput = (EditText) view.findViewById(R.id.nika_edittext_username_loginFrag);
        passwordInput = (EditText) view.findViewById(R.id.nika_edittext_pwd_loginFrag);

        googleBtn = (LinearLayout) view.findViewById(R.id.google_signIn_btn_logiBtn);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.exit_startup, R.anim.enter_login_from_startup);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reverseReplaceFragment(new GEAR5StartUpFragment());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _username = usernameInput.getText().toString().trim();
                String _pwd = passwordInput.getText().toString().trim();
                validateUserAndPwd(_username, _pwd);

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RegisterFragment());
            }
        });

        forgortpwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ForgotPasswordFragment());
            }
        });

        return view;
    }

    private void validateUserAndPwd(String username, String password) {

        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        if(username.isEmpty()){
            usernameInput.setError(getString(R.string.warning_msg_msg_username), iconError);
        }
        if (password.isEmpty()){
            passwordInput.setError(getString(R.string.warning_msg_reg_pwd), iconError);
        }
        else{
            if (username.matches(getString(R.string.limits_regx_username))){

                if(PASSWORD_PATTERN.matcher(password).matches()){
                    validateUserFireBase(username, password);
                }
                else{
                    passwordInput.setError(getString(R.string.warning_pwd_length_reg),iconError);
                }
            }
            else if(username.matches(getString(R.string.limits_email_reg))){
                if(PASSWORD_PATTERN.matcher(password).matches()){
                    validateUserFireBaseEmail(username, password);

                }
                else{
                    passwordInput.setError(getString(R.string.warning_pwd_length_reg),iconError);
                }

            }
            else{
                usernameInput.setError(getString(R.string.warning_emil_username_msg), iconError);
            }
        }

    }

    //With Email
    private void validateUserFireBaseEmail(String email, String password) {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
        Query checkEmail = databaseReference.orderByChild(getString(R.string.emailRef_reg_regFrag)).equalTo(email);
        checkEmail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String userId = dataSnapshot.child(getString(R.string.childRef_username)).getValue().toString();
                        String userEmailFound = snapshot.child(userId).child(getString(R.string.childReg_email)).getValue(String.class);
                        if(userEmailFound.matches(email)){
                            String passwordFromDB = snapshot.child(userId).child(getString(R.string.childRef_password)).getValue(String.class);
                            if(passwordFromDB.equals(password)){
                                moveToMainActivity(userId);
                            }
                            else{
                                passwordInput.setError(getString(R.string.warning_msg_pwd_loginFrag),iconError);
                            }
                            break;
                        }
                    }
                }
                else{
                    usernameInput.setError(getString(R.string.warning_email_reg_email_limits), iconError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //With username
    private void validateUserFireBase(String username, String password) {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
        Query checkUser = databaseReference.orderByChild(getString(R.string.childRef_username)).equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passwordFromDB = snapshot.child(username).child(getString(R.string.childRef_password)).getValue(String.class);
                    if(passwordFromDB.equals(password)){
                        moveToMainActivity(username);
                    }
                    else{
                        passwordInput.setError(getString(R.string.warning_msg_pwd_loginFrag),iconError);
                    }
                }
                else{
                    usernameInput.setError(getString(R.string.warning_msg_username_loginFrag),iconError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void doSave(String userId)  {
        SharedPreferences sharedPreferences= this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String typeLogin = getString(R.string.gearaccount);
        editor.putString(getString(R.string.key_typelogin), typeLogin);
        editor.putString(getString(R.string.userProfile), userId);
        editor.apply();
    }

    private void moveToMainActivity(String userId){
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.checkbox), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (remember.isChecked()){
            editor.putString(getString(R.string.remember), getString(R.string.checked));
            editor.apply();
        } else {
            editor.putString(getString(R.string.remember), getString(R.string.unchecked));
            editor.apply();
        }

        doSave(userId);

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.exit_startup, R.anim.enter_login_from_startup);

    }

}
