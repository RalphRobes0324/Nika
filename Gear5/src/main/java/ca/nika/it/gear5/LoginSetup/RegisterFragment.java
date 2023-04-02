// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5.LoginSetup;

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
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import ca.nika.it.gear5.R;


public class RegisterFragment extends Fragment {
    private Button doneBtn, loginBtn;
    EditText usernameInput, passwordInput, emailInput, confirmPasswordInput, phoneInput, fullnameInput;
    ImageView backButton;


    /*
    DON'T REMOVE HARD STRINGS OR ELSE IT WILL CRASH THE LOGIN/REGIS
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[!@#$%^&+=*])" +     // at least 1 special character
                    "(?=.*[A-Z])" +
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 6 characters
                    "$");


    //Single responsibility as it will replace the current fragment to a new one that accomplishes its single goal/responsibility
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        loginBtn = (Button) view.findViewById(R.id.nika_btn_register_login);
        doneBtn = (Button) view.findViewById(R.id.nika_btn_register_done);
        backButton = (ImageView) view.findViewById(R.id.nika_signup_back_button);



        //User Input
        usernameInput = (EditText) view.findViewById(R.id.nika_username_regFrag);
        passwordInput = (EditText) view.findViewById(R.id.nika_edittxt_pwd_regfrag);
        emailInput = (EditText) view.findViewById(R.id.nika_edittxt_email_regfrag);
        confirmPasswordInput = (EditText) view.findViewById(R.id.nika_edittxt_pwdConfirm_regfrag);
        phoneInput = (EditText) view.findViewById(R.id.nika_edittxt_phone_regfrag);
        fullnameInput = (EditText) view.findViewById(R.id.nika_fullname_regFrag);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new LoginFragment());
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String fullName = fullnameInput.getText().toString().trim();
                int startCurrency = 500;
                int startScore = 0;

                validateUser(username,password, email, confirmPassword ,startCurrency, startScore , phone, fullName);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new GEAR5StartUpFragment());
            }
        });


        return view;
    }



    private void validateUser(String username, String password, String email,
                              String confirmPassword, int startCurrency, int startScore, String phone, String fullName) {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        if(username.isEmpty()) {
            usernameInput.setError(getString(R.string.warning_msg_msg_username), iconError);
        }
        if (fullName.isEmpty()){
            fullnameInput.setError(getString(R.string.warning_msg_fullName), iconError);
        }
        if (password.isEmpty()){
            passwordInput.setError(getString(R.string.warning_msg_reg_pwd), iconError);
        }
        if (confirmPassword.isEmpty()){
            confirmPasswordInput.setError(getString(R.string.warning_msg_reg_confir), iconError);
        }
        if(email.isEmpty()){
            emailInput.setError(getString(R.string.warning_msg_reg_email),iconError);
        }
        if (phone.isEmpty()){
            phoneInput.setError(getString(R.string.enterPhoneNumber), iconError);
        }

        else{
            if (username.matches(getString(R.string.limits_regx_username))){
                if(email.matches(getString(R.string.limits_email_reg))){
                    if(PASSWORD_PATTERN.matcher(password).matches()){
                        if(confirmPassword.matches(password)){
                            if (phone.matches(getString(R.string.phone_regex))) {
                                if (fullName.matches(getString(R.string.email_regex))){
                                    String userId = username;
                                    validateDBFirebaseEmail(userId, username, email, password, startCurrency, startScore, phone, fullName);
                                }
                                else{
                                    fullnameInput.setError(getString(R.string.warning_msg_fullname), iconError);
                                }
                            }
                            else {
                                phoneInput.setError(getString(R.string.invalidPhone), iconError);
                            }
                        }
                        else{
                            confirmPasswordInput.setError(getString(R.string.warning_msg_reg_confir_not_maching), iconError);
                        }
                    }
                    else{
                        passwordInput.setError(getString(R.string.warning_pwd_length_reg),iconError);
                    }
                }
                else{
                    emailInput.setError(getString(R.string.warning_email_reg_email_limits), iconError);
                }
            }
            else{
                usernameInput.setError(getString(R.string.warning_username_length_reg), iconError);
            }
        }



    }


    private void validateDBFirebaseEmail(String userId, String username, String email, String password,
                                         int startCurrency, int startScore, String phone, String fullName) {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userEmailRef = databaseReference.child(getString(R.string.childRef_reg_regFrag));
        Query queryEmails = userEmailRef.orderByChild(getString(R.string.emailRef_reg_regFrag)).equalTo(email);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    validateDBFirebaseUsername(userId, username,email,password,startCurrency, startScore, phone, fullName);
                }
                else{
                    emailInput.setError(getString(R.string.warning_email_used_regFrag), iconError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queryEmails.addListenerForSingleValueEvent(eventListener);

    }

    private void validateDBFirebaseUsername(String userId, String username, String email, String password,
                                            int startCurrency, int startScore, String phone, String fullName) {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = databaseReference.child(getString(R.string.childRef_reg_regFrag));
        Query queryUsernames = userNameRef.orderByChild(getString(R.string.usernameRef_reg_regregFrag)).equalTo(username);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    storeNewUserdata(userId, username,email,password,startCurrency, startScore, phone, fullName);
                }
                else{
                    usernameInput.setError(getString(R.string.warning_username_used_regFrag), iconError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queryUsernames.addListenerForSingleValueEvent(eventListener);

    }


    //command design pattern, stores the information into a class which can later be added in the firebase
    private void storeNewUserdata(String userId, String username, String email,
                                  String password, int startCurrency, int startScore, String phone, String fullName) {
        int topScore2 = 0;
        int topScore3 = 0;
        int topScore4 = 0;
        int topScore5 = 0;

        String audio = "MUTE";
        String status = "OFFLINE";
        String monitor = "OFF";
        String input = "";
        String v_input = "";

        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference(getString(R.string.childRef_reg_regFrag));
        UserClass userClass = new UserClass(username, password, email, startCurrency, startScore,
                phone, fullName, topScore2, topScore3, topScore4, topScore5, audio,
                status, monitor, input, v_input);
        reference.child(userId).setValue(userClass);
        replaceFragment(new LoginFragment());

    }

}