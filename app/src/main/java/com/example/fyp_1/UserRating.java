package com.example.fyp_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class UserRating extends AppCompatActivity {

    TextView rateCount, showRating;
    EditText reviewET;
    Button submitBtn;
    RatingBar ratingBar;
    float rateValue; String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating);


        rateCount = findViewById(R.id.rateCount);
        ratingBar = findViewById(R.id.ratingBar);
        reviewET = findViewById(R.id.writeReview);
        submitBtn = findViewById(R.id.submitBtn);
        showRating = findViewById(R.id.showRating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                rateValue = ratingBar.getRating();

                if(rateValue<=1 && rateValue > 0)
                    rateCount.setText("Bad " + rateValue + "/5");
                else if(rateValue<=2 && rateValue > 1)
                    rateCount.setText("Ok " + rateValue + "/5");
                else if(rateValue<=3 && rateValue > 2)
                    rateCount.setText("Good " + rateValue + "/5");
                else if(rateValue<=4 && rateValue > 3)
                    rateCount.setText("Very Good " + rateValue + "/5");
                else if(rateValue<=5 && rateValue > 4)
                    rateCount.setText("Excellent " + rateValue + "/5");
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = rateCount.getText().toString();
                showRating.setText("Your Rating:  \n" + temp + "\n" + reviewET.getText());
                reviewET.setText("");
                ratingBar.setRating(0);
                rateCount.setText("");
            }
        });
    }
}