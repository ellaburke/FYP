package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp_1.model.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewMyFullListing extends AppCompatActivity {

    private static final String TAG = "ViewMyFullListing";

    //Firebase Components
    private static final String LISTING = "listings";
    private String userId;
    private FirebaseUser user;
    DatabaseReference updateRef;
    DatabaseReference getListingRef;

    //Set value for listing being passed from intent
    String listingToDisplay;
    Listing currentItem;

    //UI Components
    TextView listingTitleTV, listingDescriptionTV, listingLocationTV, listingExpiryTV, listingCategoryTV, listingPickUpTimesTV;
    Button fullListingEditBtn, backToProfileListing;
    ImageView fullListingImage;

    //String for setting text in TV
    String FLTitle, FLDescription, FLCategory, FLLocation, FLPickUpTimes, FLExpiry, FLImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_full_listing);

        //Init Firebase
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(LISTING);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        updateRef = FirebaseDatabase.getInstance().getReference("listings");

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        listingToDisplay = getIntent().getStringExtra("selected_listing_to_display");

        //Init UI
        listingTitleTV = (TextView) findViewById(R.id.fullListingDisplayTitle);
        listingDescriptionTV = (TextView) findViewById(R.id.fullListingDisplayDescription);
        listingLocationTV = (TextView) findViewById(R.id.fullListingDisplayLocation);
        listingExpiryTV = (TextView) findViewById(R.id.fullListingDisplayExpiry);
        listingCategoryTV = (TextView) findViewById(R.id.fullListingDisplayCategory);
        listingPickUpTimesTV = (TextView) findViewById(R.id.fullListingDisplayPickUpTimes);
        fullListingEditBtn = (Button) findViewById(R.id.fullListingDisplayEditBtn);
        backToProfileListing = (Button) findViewById(R.id.backToProfileListing);
        fullListingImage = (ImageView) findViewById(R.id.fullListingDisplayImage);

        fullListingEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editMyListingIntent = new Intent(ViewMyFullListing.this, EditMyListingActivity.class);
                editMyListingIntent.putExtra("listing_to_edit", listingToDisplay);
                startActivity(editMyListingIntent);

            }
        });

        backToProfileListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    currentItem = child.getValue(Listing.class);
                    //Log.d(TAG, "LISTING TO UPDATE 3: " + currentItem.getListingId());
                    if (currentItem.getListingId().equals(listingToDisplay)) {
                        FLTitle = currentItem.getName();
                        FLDescription = currentItem.getDescription();
                        FLCategory = currentItem.getCategory();
                        FLLocation = currentItem.getLocation();
                        FLPickUpTimes = currentItem.getPickUpTime();
                        FLExpiry = currentItem.getExpiryDate();
                        FLImage = currentItem.getListingImageURL();

                        listingTitleTV.setText(FLTitle);
                        listingDescriptionTV.setText(FLDescription);
                        listingCategoryTV.setText(FLCategory);
                        listingLocationTV.setText(FLLocation);
                        listingPickUpTimesTV.setText(FLPickUpTimes);
                        listingExpiryTV.setText(FLExpiry);
                        fullListingImage.setImageURI(Uri.parse(FLImage));
                        Picasso.get().load(FLImage).into(fullListingImage);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}