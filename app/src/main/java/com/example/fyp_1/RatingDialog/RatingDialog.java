package com.example.fyp_1.RatingDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fyp_1.Notifications.NotificationActivity;
import com.example.fyp_1.R;
import com.example.fyp_1.model.Notification;
import com.example.fyp_1.model.UserRating;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RatingDialog {

    private Activity activity;
    private AlertDialog dialog;

    //Firebase
    DatabaseReference mDatabaseRef;

    //Rating
    UserRating mUserRating;

    //UI Components
    TextView rateCount;
    Button submitBtn;
    RatingBar ratingBar;
    float rateValue; String temp;
    float rateValue1;

    public RatingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startRatingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.rating_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();

        rateCount = dialog.findViewById(R.id.rateCount);
        ratingBar = dialog.findViewById(R.id.ratingBar);
        submitBtn = dialog.findViewById(R.id.submitBtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                rateValue = ratingBar.getRating();

                if(rateValue<=1 && rateValue > 0) {
                    rateValue1 = rateValue;
                    rateCount.setText("Bad " + rateValue + "/5");
                }
                else if(rateValue<=2 && rateValue > 1) {
                    rateCount.setText("Ok " + rateValue + "/5");
                    rateValue1 = rateValue;
                }
                else if(rateValue<=3 && rateValue > 2) {
                    rateValue1 = rateValue;
                    rateCount.setText("Good " + rateValue + "/5");
                }
                else if(rateValue<=4 && rateValue > 3) {
                    rateValue1 = rateValue;
                    rateCount.setText("Very Good " + rateValue + "/5");
                }
                else if(rateValue<=5 && rateValue > 4) {
                    rateValue1 = rateValue;
                    rateCount.setText("Excellent " + rateValue + "/5");
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = rateCount.getText().toString();
                ratingBar.setRating(0);
                rateCount.setText("");

                //Init Firebase
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("userRating");

                String value = NotificationActivity.userIDSent;
                System.out.println("RATING USER" + value);

                String ratingID = mDatabaseRef.push().getKey();
                mUserRating = new UserRating(value, rateValue1, ratingID);
                mDatabaseRef.child(ratingID).setValue(mUserRating);

                dialog.dismiss();

            }
        });
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

}
