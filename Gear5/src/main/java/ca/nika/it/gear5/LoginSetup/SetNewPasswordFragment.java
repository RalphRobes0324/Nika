package ca.nika.it.gear5.LoginSetup;
// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ca.nika.it.gear5.R;

public class SetNewPasswordFragment extends Fragment {
    EditText newPasswordEditText, confirmationEditText;
    Button okBtn;
    ImageView backBtn;

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
        View view =  inflater.inflate(R.layout.fragment_set_new_password, container, false);

        Bundle bundle = this.getArguments();
        String _emailNo= bundle.getString(getString(R.string.key_final_setnewpwdFrag));


        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());

        backBtn = (ImageView) view.findViewById(R.id.nika_newPwd_back_button);
        okBtn = (Button) view.findViewById(R.id.nika_btn_ok_newpwdFrag);
        newPasswordEditText = (EditText) view.findViewById(R.id.nika_edittext_newpwd_newpwdFrag);
        confirmationEditText = (EditText) view.findViewById(R.id.nika_edittext_confnewpwd_newpwdFrag);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _newpwdNo = newPasswordEditText.getText().toString().trim();
                String _confpwdNo = confirmationEditText.getText().toString().trim();
                validatePasswords(_newpwdNo,_confpwdNo,_emailNo, iconError);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reverseReplaceFragment(new UserPhoneValidationFragment());
            }
        });
        return view;
    }

    private void validatePasswords(String newpwdNo, String confpwdNo, String emailNo, Drawable iconError) {
        if (newpwdNo.isEmpty()){
            newPasswordEditText.setError(getString(R.string.warning_msg_reg_pwd), iconError);
        }
        if(confpwdNo.isEmpty()){
           confirmationEditText.setError(getString(R.string.warning_msg_reg_confir), iconError);
        }
        else{
            if(newpwdNo.matches(getString(R.string.limits))){
                if (confpwdNo.matches(newpwdNo)){
                    getUserDataFB(newpwdNo, emailNo);
                }
                else{
                    confirmationEditText.setError(getString(R.string.warning_msg_reg_confir_not_maching), iconError);
                }

            }else{
                newPasswordEditText.setError(getString(R.string.warning_pwd_length_reg),iconError);
            }
        }

    }

    private void getUserDataFB(String newpwdNo, String emailNo) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(getString(R.string.childRef_reg_regFrag));
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String userId = dataSnapshot.child(getString(R.string.childRef_username)).getValue().toString();
                    String userEmailFound = snapshot.child(userId).child(getString(R.string.childReg_email)).getValue(String.class);
                    if(userEmailFound.matches(emailNo)){
                        storeNewPwd(userId, newpwdNo);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void storeNewPwd(String userId, String newpwdNo) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(getString(R.string.childRef_reg_regFrag)).child(userId).child(getString(R.string.childRef_password)).setValue(newpwdNo);
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.complate_newpwd), Toast.LENGTH_SHORT).show();
        replaceFragment(new LoginFragment());

    }


}