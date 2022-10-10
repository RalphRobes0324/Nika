// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

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
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;


public class LoginFragment extends Fragment {
    Button registerBtn, loginBtn;
    private EditText username;
    private EditText password;
    DatabaseReference databaseReference;

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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        registerBtn = (Button) view.findViewById(R.id.nika_btn_register);
        loginBtn = (Button) view.findViewById(R.id.nika_btn_login);
        username = (EditText) view.findViewById(R.id.nika_login_username);
        password = (EditText) view.findViewById(R.id.nika_login_password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                        R.drawable.ic_baseline_error_24);
                iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());
                if(username.getText().toString().equals(getString(R.string.blank)) && username.getText().length() <= 0){
                    username.setError(getString(R.string.warning_msg_msg_username),iconError);
                }
                else if(password.getText().toString().equals(getString(R.string.blank)) && password.getText().length() <= 0){
                    password.setError(getString(R.string.warning_msg_reg_pwd),iconError);
                }
                else{
                    if(username.getText().length() > 1
                            && password.getText().length() > 1){
                        if(username.getText().toString().matches(getString(R.string.limits))){
                            if (password.getText().toString().length()>=5) {
                                String userCheck = username.getText().toString().trim();
                                String pwdCheck = password.getText().toString().trim();
                                String id = userCheck + pwdCheck;
                                validateDBForm(id, iconError);

                            }
                            else{
                                password.setError(getString(R.string.warning_msg_reg_pwd_not_maching),iconError);
                            }
                        }
                        else{
                            username.setError(getString(R.string.warning_msg_reg_username_not_maching),iconError);
                        }

                    }
                }
            }

            private void validateDBForm(String id, Drawable icon) {
                databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.users));
                databaseReference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().exists()){
                                DataSnapshot dataSnapshot = task.getResult();
                                String usernameDB = String.valueOf(dataSnapshot.child(getString(R.string.username2)).getValue(String.class));
                                int curr = Integer.valueOf(dataSnapshot.child(getString(R.string.currency2)).getValue(Integer.class));
                                int topScore = Integer.valueOf(dataSnapshot.child(getString(R.string.topCurrency)).getValue(Integer.class));
                                Bundle result = new Bundle();
                                result.putString(getString(R.string.df),usernameDB);
                                result.putInt(getString(R.string.df_curr),curr);
                                result.putInt(getString(R.string.df_tScore),topScore);
                                getParentFragmentManager().setFragmentResult(getString(R.string.dataForm),result);


                                openMainActivity();
                            }else{
                                username.setError(getString(R.string.error_msg_login),icon);
                            }
                        }
                        else{
                            username.setError(getString(R.string.error_msg_login_read),icon);
                        }

                    }
                });

            }
            private void openMainActivity() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new RegisterFragment());
            }
        });

        return view;
    }
}