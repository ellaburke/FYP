package com.example.fyp_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.fyp_1.model.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditMyListingActivity extends AppCompatActivity {

    //Firebase Components
    private static final String LISTING = "listings";
    private String userId;
    private FirebaseUser user;
    DatabaseReference updateRef;
    DatabaseReference getListingRef;

    //Set value for listing being passed from intent
    String listingToEdit;
    Listing currentItem;

    //UI Components
    EditText listingTitle, listingDescription, listingLocation, listingPickUpTimes;

    String optionSelected;
    String listingTitleEntered, listingDescriptionEntered, listingLocationEntered, listingPickUpTimesEntered, listingExpiryDateEntered,
            listingKeepListedFor, listingOption, listingFoodCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_listing);

        //Init Firebase
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(LISTING);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        updateRef = FirebaseDatabase.getInstance().getReference("listings");

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        listingToEdit = getIntent().getStringExtra("listing_to_edit");


    }
}