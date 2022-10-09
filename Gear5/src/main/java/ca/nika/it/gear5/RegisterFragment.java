package ca.nika.it.gear5;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class RegisterFragment extends Fragment {
    Button loginBtn, regBtn;
    LinearLayout linearLayout;
    private EditText username;
    private EditText password;
    private EditText confirmation;

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

        loginBtn = (Button) view.findViewById(R.id.btn_login_reg);
        regBtn = (Button) view.findViewById(R.id.btn_register_reg);
        username = (EditText) view.findViewById(R.id.reg_username);
        password = (EditText) view.findViewById(R.id.reg_password);
        confirmation = (EditText) view.findViewById(R.id.reg_confirmation_pwd);
        linearLayout = (LinearLayout) view.findViewById(R.id.test);




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
        if(username.getText().toString().equals("") && username.getText().length() <= 0){
            username.setError(getString(R.string.warning_msg_msg_username),iconError);
        }
        else if(password.getText().toString().equals("") && password.getText().length() <= 0){
            password.setError(getString(R.string.warning_msg_reg_pwd),iconError);
        }
        else if(confirmation.getText().toString().equals("") && confirmation.getText().length() <= 0){
            confirmation.setError(getString(R.string.warning_msg_reg_confir),iconError);
        }



    }

}