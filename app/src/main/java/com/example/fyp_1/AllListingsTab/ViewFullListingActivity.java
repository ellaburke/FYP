package com.example.fyp_1.AllListingsTab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.Recipe.ViewFullRecipeActivity;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.model.Listing;
import com.example.fyp_1.model.Notification;
import com.example.fyp_1.model.User;
import com.example.fyp_1.model.UserRating;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewFullListingActivity extends AppCompatActivity {

    //Firebase Components
    private static final String LISTING = "listings";
    private static final String TAG = "ViewFullListing";
    private String userId;
    private FirebaseUser user;
    DatabaseReference getListingRef;
    DatabaseReference getUserNameRef;
    StorageReference storageRef;
    StorageReference profilerUpdateRef;
    private DatabaseReference mDatabaseRef, mDatabaseRequestTotalRef, mDatabaseRatingRef;
    DatabaseReference userRootRef;

    //Listing ID
    String IDListing;

    //User Details
    String fullName, fName, lName, pNumber;

    //User Listing Name
    String finalFullUserName;

    //Notification
    Notification myNotification;

    //Rating Bar
    RatingBar userRatingBar;
    private List<Float> mRatings;
    Float floatValue;
    Float sum;


    //Set value for listing being passed from intent
    String listingToDisplay;
    Listing currentItem;

    //Set Username value
    User currentUser;

    //UI Components
    TextView listingTitleTV, listingDescriptionTV, listingLocationTV, listingExpiryTV, listingCategoryTV, listingPickUpTimesTV, listingProfileUsername, listingReuseTotal;
    Button requestListingBtn;
    ImageView fullListingImage;
    ImageView profilePicIV;

    //String for setting text in TV
    String FLTitle, FLDescription, FLCategory, FLLocation, FLPickUpTimes, FLExpiry, FLImage, FLProfileImage, FLProfileUserName;
    int reuse1;
    private String userFrom;
    private String userTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_listing);

        //Init Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        userFrom = user.getEmail();
        getListingRef = FirebaseDatabase.getInstance().getReference("listings");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(LISTING);
        mDatabaseRequestTotalRef = FirebaseDatabase.getInstance().getReference("listings");
        //Profile Pic ref
        storageRef = FirebaseStorage.getInstance().getReference();
        //Username
        getUserNameRef = FirebaseDatabase.getInstance().getReference("user");
        //User ref
        userRootRef = FirebaseDatabase.getInstance().getReference("user");
        //Notification
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notificationRequests");
        //Rating ref
        mDatabaseRatingRef = FirebaseDatabase.getInstance().getReference("userRating");

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        listingToDisplay = getIntent().getStringExtra("selected_listing_to_display");


        //Init UI
        listingTitleTV = (TextView) findViewById(R.id.fullListingDisplayTitle1);
        listingDescriptionTV = (TextView) findViewById(R.id.fullListingDisplayDescription1);
        listingLocationTV = (TextView) findViewById(R.id.fullListingDisplayLocation1);
        listingExpiryTV = (TextView) findViewById(R.id.fullListingDisplayExpiry1);
        listingCategoryTV = (TextView) findViewById(R.id.fullListingDisplayCategory1);
        listingPickUpTimesTV = (TextView) findViewById(R.id.fullListingDisplayPickUpTimes1);
        listingReuseTotal = (TextView) findViewById(R.id.fullListingRequestTotal);
        requestListingBtn = (Button) findViewById(R.id.requestListingBtn);
        fullListingImage = (ImageView) findViewById(R.id.fullListingDisplayImage1);
        profilePicIV = (ImageView) findViewById(R.id.profilePicOnListing);
        listingProfileUsername = (TextView) findViewById(R.id.profileUsernameOnListing);
        userRatingBar = (RatingBar) findViewById(R.id.ratingBarPerUser);

        //rating array
        mRatings = new ArrayList<>();

        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.SearchListingNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.MyKitchenNav:
                        startActivity(new Intent(getApplicationContext(), MyKitchenIngredients2.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.SearchListingNav:
                        Intent emptyIntent = new Intent(ViewFullListingActivity.this, viewListingActivity.class);
                        emptyIntent.putExtra("ingredient_clicked", " ");
                        startActivity(emptyIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.MyShoppingListNav:
                        startActivity(new Intent(getApplicationContext(), MyShoppingListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
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
                        IDListing = currentItem.getListingId();
                        FLProfileImage = currentItem.getUserId();
                        FLProfileUserName = currentItem.getUserId();
                        reuse1 = currentItem.getRequestTotal();

                        listingTitleTV.setText(FLTitle);
                        listingDescriptionTV.setText(FLDescription);
                        listingCategoryTV.setText(FLCategory);
                        listingLocationTV.setText(FLLocation);
                        listingPickUpTimesTV.setText(FLPickUpTimes);
                        listingExpiryTV.setText(FLExpiry);
                        listingReuseTotal.setText("(" + reuse1 + ")");
                        fullListingImage.setImageURI(Uri.parse(FLImage));
                        Picasso.get().load(FLImage).into(fullListingImage);

                        profilerUpdateRef = storageRef.child(FLProfileImage + ".jpg");
                        profilePicIV.setImageURI(Uri.parse(FLProfileImage));

                        profilerUpdateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(profilePicIV);
                            }
                        });


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        getUserNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (postSnapshot.getKey().equals(FLProfileUserName)) {
                        String finalUserName = user.getFirstName();
                        userTo = user.getEmail();
                        String finalUserLastName = user.getLastName();
                        System.out.println("FINAL N" + finalUserName);
                        System.out.println("FINAL L N" + finalUserLastName);
                        finalFullUserName = finalUserName + " " + finalUserLastName;
                    }
                }
                listingProfileUsername.setText(finalFullUserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabaseRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UserRating UR = postSnapshot.getValue(UserRating.class);
                    if (UR.getUserID().equals(FLProfileImage)) {
                        Float rateNo = UR.getUserRating();
                        mRatings.add(rateNo);
                        System.out.println("RATING IS" + mRatings);
                    }
                }
                sum = 0.0f;
                if(!mRatings.isEmpty()) {
                    for (Float mark : mRatings) {
                        sum += mark;
                    }
                    floatValue = sum.floatValue() / mRatings.size();
                    System.out.println("AVERAGE" + floatValue);
                    userRatingBar.setRating(floatValue);
                }else{
                    userRatingBar.setRating(0f);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (postSnapshot.getKey().equals(userId)) {
                        fName = user.getFirstName();
                        lName = user.getLastName();
                        pNumber = user.getPhoneNumber();
                        System.out.println("FN" + fName);
                        break;

                    }
                }
                fullName = fName + " " + lName;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        requestListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "Request";
                String listingState = "Requested";

                String notificationID = mDatabaseRef.push().getKey();

                myNotification = new Notification(FLTitle, FLImage, type, userId, FLProfileUserName, notificationID, listingState, fullName, listingToDisplay, pNumber);
                mDatabaseRef.child(notificationID).setValue(myNotification);

                //Update request total
                int reuseNo2 = reuse1 + 1;
                mDatabaseRequestTotalRef.child(listingToDisplay).child("requestTotal").setValue(reuseNo2);

                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_back_icon) {
            finish();
            return true;
        }

        return true;
    }
}