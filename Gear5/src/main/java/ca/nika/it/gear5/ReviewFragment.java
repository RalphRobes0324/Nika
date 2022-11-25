// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewFragment extends Fragment {

    PreferenceManager preferenceManager;
    private String getUserId;
    private RatingBar ratingBar;
    private EditText usernameInput, userPhoneInput, userEmailInput, userCommentInput;
    AlertDialog dialog;

    public ReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        //Share Preferences
        preferenceManager = PreferenceManager.getInstance(getActivity());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.SettingsPref), Context.MODE_PRIVATE);
        //Getting data from Share Preferences
        getUserId = sharedPreferences.getString(getString(R.string.userProfile), getString(R.string.blank));
        //Getting data from XML
        ratingBar = (RatingBar) view.findViewById(R.id.nika_ratingbar_reviewFrag);

        usernameInput = (EditText) view.findViewById(R.id.nika_edittxt_name_reviewFrag);
        userPhoneInput = (EditText) view.findViewById(R.id.nika_edittxt_phone_reviewFrag);
        userEmailInput = (EditText) view.findViewById(R.id.nika_edittxt_email_reviewFrag);
        userCommentInput = (EditText) view.findViewById(R.id.nika_edittxt_comment_reviewFrag);

        String modelPhone = Build.MODEL;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification),getString(R.string.myNotification),NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Button submit=(Button)view.findViewById(R.id.submit_review);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Data from Rating
                int tag = 0;
                int starRating = ratingBar.getNumStars();
                float getRating = ratingBar.getRating();
                String overall = "" + ((getRating/starRating)*100) + getString(R.string.key_precent);
                //Getting Data
                String username = usernameInput.getText().toString().trim();
                String userPhone = userPhoneInput.getText().toString().trim();
                String userEmail = userEmailInput.getText().toString().trim();
                String userComment = userCommentInput.getText().toString().trim();


                //This utilizes KISS as everything is very simplified and easy for anyone to understand
                if (username.isEmpty()){
                    username = null;
                }
                if(userPhone.isEmpty()){
                    userPhone = null;
                }
                if(userEmail.isEmpty()){
                    userEmail = null;
                }
                if(userComment.isEmpty()){
                    userComment = null;
                }
                validateFBUser(overall, getUserId, tag,username, userPhone, userEmail, userComment, modelPhone);
            }

        });

        return view;
    }
    private void validateFBUser(String overall, String getUserId, int tag, String username,
                                String userPhone, String userEmail, String userComment, String modelPhone) {
        int userTag = tag + 1;
        String newUserId = getUserId + getString(R.string.key_GEAR) + userTag;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.childReg_reviews));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(newUserId)){
                    storeDataFB(newUserId, overall, username, userPhone, userEmail, userComment, modelPhone);
                }
                else{
                    validateFBUser(overall, getUserId, userTag,username, userPhone, userEmail, userComment, modelPhone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void storeDataFB(String newUserId, String overall,
                             String username, String userPhone, String userEmail, String userComment, String modelPhone) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference(getString(R.string.childReg_reviews));
        UserReviewClass userReviewClass = new UserReviewClass(overall, username, userPhone, userEmail, userComment, modelPhone);
        reference.child(newUserId).setValue(userReviewClass);

        LoadingDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //Notification
                Toast.makeText(getActivity(), R.string.reviewSubmitted, Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),getString(R.string.notification));
                builder.setContentTitle(getString(R.string.title));
                builder.setContentText(getString(R.string.reviewReply));
                builder.setSmallIcon(R.drawable.devil_fruit);
                builder.setAutoCancel(true);
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity());
                managerCompat.notify(1,builder.build());
            }
        },5000);
    }

    public void LoadingDialog(){

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getLayoutInflater().inflate(R.layout.load_dialog,null);
        builder.setCancelable(false);

        builder.setView(view);
        dialog = builder.create();

        dialog.show();
    }
}
