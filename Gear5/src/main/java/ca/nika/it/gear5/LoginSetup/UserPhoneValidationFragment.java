// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5.LoginSetup;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import ca.nika.it.gear5.R;


public class UserPhoneValidationFragment extends Fragment {

    EditText userPhoneInput;
    Button nextBtn;
    CountryCodePicker countryCodePicker;
    ImageView backButton;

    private void replaceFragment(Fragment fragment, String text) {
        Bundle bundle, bundleEmail;
        //Getting email
        bundleEmail = this.getArguments();
        String catchEmail = bundleEmail.getString(getString(R.string.key_emailid));
        //storing data
        bundle = new Bundle();
        bundle.putString(getString(R.string.key_key),text);
        bundle.putString(getString(R.string.key_key1), catchEmail);
        //animation
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_startup, R.anim.enter_login_from_reg);
        transaction.addToBackStack(null);
        //sending data
        fragment.setArguments(bundle);
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



    private void validatePhoneNo(String text, String userNumber, Context context) {
        if(userNumber.isEmpty()){
            Toast.makeText(context, getString(R.string.warning_msg_num_userPhoneFrag),Toast.LENGTH_SHORT).show();
        }else{
            if(text.matches(getString(R.string.limits_phone_regex))){
                replaceFragment(new VerifyOTPFragment(), text);

            }
            else{
                Toast.makeText(context, getString(R.string.warning_msg_num1_userPhoneFrag),Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_phone_validation, container, false);
        userPhoneInput = (EditText) view.findViewById(R.id.nika_edittxt_phone_forgorFrag);
        nextBtn = (Button) view.findViewById(R.id.nika_btn_forgotPwd_next_phone);
        countryCodePicker = (CountryCodePicker) view.findViewById(R.id.nika_countrypicker_userPhoneFrag);
        backButton = (ImageView) view.findViewById(R.id.nika_userPhone_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reverseReplaceFragment(new ForgotPasswordFragment());
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code =  countryCodePicker.getSelectedCountryCode();
                String userNumber = userPhoneInput.getText().toString();

                Context context = getActivity().getApplicationContext();
                String text = getString(R.string.plus)+code+userNumber;

                validatePhoneNo(text,userNumber,context);



            }
        });


        return view;
    }

}