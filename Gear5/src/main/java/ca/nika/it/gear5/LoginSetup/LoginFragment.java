// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5.LoginSetup;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.nika.it.gear5.MainActivity;
import ca.nika.it.gear5.R;


public class LoginFragment extends Fragment {
    private Button registerBtn, loginBtn, forgortpwdBtn;
    private CheckBox remember;
    private EditText usernameInput, passwordInput;

    DatabaseReference databaseReference;

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_reg, R.anim.enter_login_from_reg);
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        registerBtn = (Button) view.findViewById(R.id.nika_btn_login_reg);
        forgortpwdBtn = (Button) view.findViewById(R.id.nika_btn_forgotPwd_loginFrag);
        loginBtn = (Button) view.findViewById(R.id.nika_btn_login_login);

        usernameInput = (EditText) view.findViewById(R.id.nika_edittext_username_loginFrag);
        passwordInput = (EditText) view.findViewById(R.id.nika_edittext_pwd_loginFrag);

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
            if (username.matches(getString(R.string.limits))){
                if(password.matches(getString(R.string.limits))){
                    validateUserFireBase(username, password);
                }
                else{
                    passwordInput.setError(getString(R.string.warning_pwd_length_reg),iconError);
                }
            }
            else{
                usernameInput.setError(getString(R.string.warning_username_length_reg), iconError);
            }
        }

    }

    private void validateUserFireBase(String username, String password) {
        String userId = username + password;
        Context context = getActivity().getApplicationContext();
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userId)){
                    //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.exit_startup, R.anim.enter_login_from_startup);
                }
                else{
                    usernameInput.setError(getString(R.string.warning_msg_username_loginFrag),iconError);
                    passwordInput.setError(getString(R.string.warning_msg_pwd_loginFrag),iconError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
