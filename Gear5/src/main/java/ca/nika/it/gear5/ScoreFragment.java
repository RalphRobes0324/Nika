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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import ca.nika.it.gear5.LoginSetup.LoginFragment;


public class ScoreFragment extends Fragment {

    private TextView nikaScore1TextView, nikaScore2TextView, nikaScore3TextView;

    JSONArray arrayFirebase=new JSONArray();
    JSONArray sortedArray=new JSONArray();
    List<JSONObject> sortValues = new ArrayList<JSONObject>();

    ListView listView;


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

        listView = (ListView) view.findViewById(R.id.nika_userList);

        nikaSyncTaskImage = (ImageView) view.findViewById(R.id.nika_aSync);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.childRef_reg_regFrag));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    String username = item.child(getString(R.string.childRef_username)).getValue().toString();
                    String userScore = item.child(getString(R.string.childRef_topScore)).getValue(Integer.class).toString();
                    try {
                        arrayFirebase.put(new JSONObject().put("Username", username).put("UserScore", userScore));
                    } catch (JSONException e) {
                        Log.d("Failed", "STORING");
                    }
                }

                for (int i = 0; i < arrayFirebase.length(); i++){
                    try {
                        sortValues.add(arrayFirebase.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                Collections.sort(sortValues, new Comparator<JSONObject>() {
                    private static final String KEY_NAME = "UserScore";
                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String str1 = new String();
                        String str2 = new String();

                        try {
                            str1 = (String) a.get(KEY_NAME);
                            str2 = (String) b.get(KEY_NAME);
                        } catch (JSONException e) {
                            Log.d("FAILED", "NULL SORT");
                        }
                        return -str1.compareTo(str2);
                    }
                });



                for(int i = 0; i < arrayFirebase.length(); i++) {
                    sortedArray.put(sortValues.get(i));
                }

                try {
                    DisplayTopUsers(sortedArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        new LoadImage().execute();

        return view;

    }

    private void DisplayTopUsers(JSONArray sortedArray) throws JSONException {
        ArrayList<String> top15Array = new ArrayList<String>();

        if (sortedArray.length() < 9) {
            for (int pos = 0; pos < sortedArray.length(); pos++) {
                String jsonStr = sortedArray.getString(pos);
                JSONObject objectData = new JSONObject(jsonStr);
                String Score = (String) objectData.get("UserScore");
                String Username = (String) objectData.get("Username");
                String combinedData = Username + " : " + Score;
                top15Array.add(combinedData);
            }
        }else{
            for (int pos = 0; pos < 9; pos++) {
                String jsonStr = sortedArray.getString(pos);
                JSONObject objectData = new JSONObject(jsonStr);
                String Score = (String) objectData.get("UserScore");
                String Username = (String) objectData.get("Username");
                String combinedData = Username + " score: " + Score;
                top15Array.add(combinedData);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_view, top15Array);

        listView.setAdapter(adapter);

    }


}