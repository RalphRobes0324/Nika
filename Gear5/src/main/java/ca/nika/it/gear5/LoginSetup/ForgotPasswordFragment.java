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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.nika.it.gear5.R;

public class ForgotPasswordFragment extends Fragment {
    Button button;
    EditText emailEditxt, phoneEdittxt;
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
        View view =inflater.inflate(R.layout.fragment_forgot_password, container, false);

        button = (Button) view.findViewById(R.id.nika_btn_forgotPwd_next);
        emailEditxt = (EditText) view.findViewById(R.id.nika_edittxt_email_forgotpwdFrag);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replaceFragment(new UserPhoneValidationFragment());
                String _email = emailEditxt.getText().toString().trim();
                //Toast.makeText(getActivity().getApplicationContext(), "Hello World", Toast.LENGTH_LONG).show();
                validateUserEmail(_email);

            }
        });

        return view;
    }

    private void validateUserEmail(String email) {
        Drawable iconError = AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_baseline_error_24);
        iconError.setBounds(0,0,iconError.getIntrinsicWidth(),iconError.getIntrinsicHeight());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("email");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(email)){
                    replaceFragment(new UserPhoneValidationFragment());
                }
                else{
                    emailEditxt.setError("Email Invalid", iconError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}