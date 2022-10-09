// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoginFragment extends Fragment {
    Button registerBtn, loginBtn;
    private EditText username;
    private EditText password;
    FragmentNavi fragmentNavi;

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

        registerBtn = (Button) view.findViewById(R.id.btn_register);
        loginBtn = (Button) view.findViewById(R.id.btn_login);
        username = (EditText) view.findViewById(R.id.login_username);
        password = (EditText) view.findViewById(R.id.login_password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                        R.drawable.ic_baseline_error_24);
                iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());
                if(username.getText().toString().equals("") && username.getText().length() <= 0){
                    username.setError(getString(R.string.warning_msg_msg_username),iconError);
                }
                else if(password.getText().toString().equals("") && password.getText().length() <= 0){
                    password.setError(getString(R.string.warning_msg_reg_pwd),iconError);
                }
                else{
                    if(username.getText().length() > 1
                            && password.getText().length() > 1){
                        if(username.getText().toString().matches("^[a-z0-9_-]{3,15}$")){
                            if (password.getText().toString().length()>=5) {
                                openMainActivity();
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

            private void openMainActivity() {
                validateForm();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }

            private void validateForm() {


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