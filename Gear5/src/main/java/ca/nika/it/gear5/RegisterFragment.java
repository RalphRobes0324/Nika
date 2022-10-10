// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {
    Button loginBtn, regBtn;
    LinearLayout linearLayout;
    private String name,pwd,id;
    private int currency, topscore;
    private EditText username;
    private EditText password;
    private EditText confirmation;
    DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase rootNode;
    DatabaseReference reference;

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        loginBtn = (Button) view.findViewById(R.id.nika_btn_login_reg);
        regBtn = (Button) view.findViewById(R.id.nika_btn_register_reg);
        username = (EditText) view.findViewById(R.id.nika_reg_username);
        password = (EditText) view.findViewById(R.id.nika_reg_password);
        confirmation = (EditText) view.findViewById(R.id.nika_reg_confirmation_pwd);
        //firebaseAuth = FirebaseAuth.auth;

        //linearLayout = (LinearLayout) view.findViewById(R.id.test);



        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmptyForm();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new LoginFragment());
            }
        });

        return view;
    }

    private void validateEmptyForm() {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());
        if(username.getText().toString().equals(getString(R.string.blank)) && username.getText().length() <= 0){
            username.setError(getString(R.string.warning_msg_msg_username),iconError);
        }
        else if(password.getText().toString().equals(getString(R.string.blank)) && password.getText().length() <= 0){
            password.setError(getString(R.string.warning_msg_reg_pwd),iconError);
        }
        else if(confirmation.getText().toString().equals(getString(R.string.blank)) && confirmation.getText().length() <= 0){
            confirmation.setError(getString(R.string.warning_msg_reg_confir),iconError);
        }

        if(username.getText().length() >= 1
                && password.getText().length() >= 1
                && confirmation.getText().length() >= 1){

            if(username.getText().toString().matches(getString(R.string.limits))){
                if (password.getText().toString().length()>=5) {
                    if(confirmation.getText().toString().equals(password.getText().toString())){
                        name = username.getText().toString();
                        pwd = password.getText().toString();
                        id = name + pwd;
                        currency = 1000;
                        topscore = 0;
                        validateFromDB(id,name,pwd,iconError, currency, topscore);
                        //returnLoginFrag();
                    }
                    else{
                        confirmation.setError(getString(R.string.warning_msg_reg_confir_not_maching),iconError);
                    }
                }
                else{
                    password.setError(getString(R.string.warning_msg_reg_pwd_not_maching),iconError);
                }
            }else{
                username.setError(getString(R.string.warning_msg_reg_username_not_maching),iconError);
            }

        }

    }

    private void validateFromDB(String checkUserDB_ID,String userEnter, String pwdEnter, Drawable icon, int currency, int userScore) {
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.users));
        databaseReference.child(checkUserDB_ID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        username.setError(getString(R.string.warning_msg_reg_account_exist),icon);
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.success_msg_reg),Toast.LENGTH_SHORT).show();
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference(getString(R.string.users));
                        UserClass helperClass = new UserClass(userEnter,pwdEnter,currency,userScore);
                        reference.child(checkUserDB_ID).setValue(helperClass);
                        returnLoginFrag();
                    }
                }
                else{
                    username.setError(getString(R.string.error_msg_login_read),icon);
                }

            }

            private void returnLoginFrag() {
                replaceFragment(new LoginFragment());
            }
        });
    }



}