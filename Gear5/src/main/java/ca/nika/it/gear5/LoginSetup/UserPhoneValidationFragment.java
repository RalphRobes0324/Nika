package ca.nika.it.gear5.LoginSetup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

import ca.nika.it.gear5.R;


public class UserPhoneValidationFragment extends Fragment {

    EditText userPhoneInput;
    Button nextBtn;
    CountryCodePicker countryCodePicker;

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
        View view = inflater.inflate(R.layout.fragment_user_phone_validation, container, false);
        userPhoneInput = (EditText) view.findViewById(R.id.nika_edittxt_phone_forgorFrag);
        nextBtn = (Button) view.findViewById(R.id.nika_btn_forgotPwd_next_phone);
        countryCodePicker = (CountryCodePicker) view.findViewById(R.id.nika_countrypicker_userPhoneFrag);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}