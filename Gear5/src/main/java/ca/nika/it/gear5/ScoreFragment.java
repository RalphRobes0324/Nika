// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ScoreFragment extends Fragment {

    private TextView nikaScore1TextView, nikaScore2TextView, nikaScore3TextView;

    ArrayList<String> arrayListOfUsers = new ArrayList<String>();
    ArrayList<Integer> arrayListOfUserScores = new ArrayList<Integer>();
    ProgressDialog progress;
    ImageView nikaSyncTaskImage;

    public ScoreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private class LoadImage extends AsyncTask<Void,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap=null;

            try {
                bitmap= BitmapFactory.decodeStream((InputStream)new URL(getString(R.string.onlineLink)).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            //show progress dialog while image is loading
            progress=new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.loadingImage));
            progress.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap!=null) {
                nikaSyncTaskImage.setImageBitmap(bitmap);
                progress.dismiss();
            } else {
                progress.dismiss();
                Toast.makeText(getActivity(), getString(R.string.errorOccured),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        nikaScore1TextView = (TextView) view.findViewById(R.id.nikaScore1);
        nikaScore2TextView = (TextView) view.findViewById(R.id.nikaScore2);
        nikaScore3TextView = (TextView) view.findViewById(R.id.nikaScore3);
        nikaSyncTaskImage = (ImageView) view.findViewById(R.id.nika_aSync);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.childRef_reg_regFrag));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    String username = item.child(getString(R.string.childRef_username)).getValue().toString();
                    Integer userScore = item.child(getString(R.string.childRef_topScore)).getValue(Integer.class);
                    arrayListOfUserScores.add(userScore);
                    arrayListOfUsers.add(username);
                }

                /*
                nikaScore1TextView.setText(getString(R.string.nameDisplay) + arrayListOfUsers.get(0) + getString(R.string.topScore) + arrayListOfUserScores.get(0).intValue());
                nikaScore2TextView.setText(getString(R.string.nameDisplay) + arrayListOfUsers.get(1) + getString(R.string.topScore) + arrayListOfUserScores.get(1).intValue());
                nikaScore3TextView.setText(getString(R.string.nameDisplay) + arrayListOfUsers.get(2) + getString(R.string.topScore) + arrayListOfUserScores.get(2).intValue());
                 */


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        new LoadImage().execute();

        return view;

    }


}