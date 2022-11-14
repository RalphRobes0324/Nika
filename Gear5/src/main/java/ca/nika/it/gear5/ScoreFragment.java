// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.nika.it.gear5.LoginSetup.UserClass;

public class ScoreFragment extends Fragment {

    private TextView nikaScore1TextView;
    String nikaScoreDisplay1;
    ArrayList<String> arrayListOfUsers = new ArrayList<String>();


    public ScoreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        nikaScore1TextView = (TextView) view.findViewById(R.id.nikaScore1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    String username = item.child("username").getValue().toString();
                    arrayListOfUsers.add(username);
                    Log.d("MYAPP", username);
                }
                //nikaScore1TextView.setText(arrayListOfUsers.get(0));
                int end = arrayListOfUsers.size();
                int start = 0;
                sortArray(arrayListOfUsers, start, end);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //String user = arrayListOfUsers.get(0);
        //Log.d("MYAPP", user);


        return view;

    }

    private void sortArray(ArrayList<String> arrayListOfUsers, int start, int end) {


    }
}