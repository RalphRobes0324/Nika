// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ca.nika.it.gear5.LoginSetup.LoginFragment;


public class ScoreFragment extends Fragment {


    ProgressDialog progress;
    PreferenceManager preferenceManager;
    ImageView nikaSyncTaskImage;

    TextView top1, top2, top3, top4, top5;
    private String userId;
    private String userScore, userScore2, userScore3, userScore4, userScore5;


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

        preferenceManager = PreferenceManager.getInstance(getActivity());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        //Getting data from Share Preferences
        userId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));

        nikaSyncTaskImage = (ImageView) view.findViewById(R.id.nika_aSync);

        top1 = (TextView) view.findViewById(R.id.top1_nike);
        top2 = (TextView) view.findViewById(R.id.top2_nike);
        top3 = (TextView) view.findViewById(R.id.top3_nike);
        top4 = (TextView) view.findViewById(R.id.top4_nike);
        top5 = (TextView) view.findViewById(R.id.top5_nike);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = db.child("users").child(userId);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    userScore = snapshot.child("topScore").getValue(Integer.class).toString();
                    userScore2 = snapshot.child("topScore2").getValue(Integer.class).toString();
                    userScore3 = snapshot.child("topScore3").getValue(Integer.class).toString();
                    userScore4 = snapshot.child("topScore4").getValue(Integer.class).toString();
                    userScore5 = snapshot.child("topScore5").getValue(Integer.class).toString();

                    top1.setText(userScore);
                    top2.setText(userScore2);
                    top3.setText(userScore3);
                    top4.setText(userScore4);
                    top5.setText(userScore5);

                }else{
                    Log.d("FAILED", "FAILED GOOGLE LOAD PROF");
                }
            }
        });











        new LoadImage().execute();

        return view;

    }


}