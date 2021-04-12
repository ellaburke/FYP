package com.example.fyp_1.AllListingsTab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp_1.R;
import com.example.fyp_1.model.Listing;
import com.example.fyp_1.model.Notification;
import com.example.fyp_1.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private DatabaseReference mDatabaseRef;
    DatabaseReference userRootRef;

    //Listing ID
    String IDListing;

    //User Details
    String fullName, fName, lName;

    //Notification
    Notification myNotification;


    //Set value for listing being passed from intent
    String listingToDisplay;
    Listing currentItem;

    //Set Username value
    User currentUser;

    //UI Components
    TextView listingTitleTV, listingDescriptionTV, listingLocationTV, listingExpiryTV, listingCategoryTV, listingPickUpTimesTV, listingProfileUsername;
    Button requestListingBtn, chatUserOfListingBtn;
    ImageView fullListingImage;
    ImageView profilePicIV;

    //String for setting text in TV
    String FLTitle, FLDescription, FLCategory, FLLocation, FLPickUpTimes, FLExpiry, FLImage, FLProfileImage, FLProfileUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_listing);

        //Init Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        getListingRef = FirebaseDatabase.getInstance().getReference("listings");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(LISTING);
        //Profile Pic ref
        storageRef = FirebaseStorage.getInstance().getReference();
        //Username
        getUserNameRef = FirebaseDatabase.getInstance().getReference("user");
        //User ref
        userRootRef = FirebaseDatabase.getInstance().getReference("user");
        //Notification
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notificationRequests");

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
        requestListingBtn = (Button) findViewById(R.id.requestListingBtn);
        fullListingImage = (ImageView) findViewById(R.id.fullListingDisplayImage1);
        chatUserOfListingBtn = (Button) findViewById(R.id.chatUserOfListingBtn);
        profilePicIV = (ImageView) findViewById(R.id.profilePicOnListing);
        listingProfileUsername = (TextView) findViewById(R.id.profileUsernameOnListing);


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

                        listingTitleTV.setText(FLTitle);
                        listingDescriptionTV.setText(FLDescription);
                        listingCategoryTV.setText(FLCategory);
                        listingLocationTV.setText(FLLocation);
                        listingPickUpTimesTV.setText(FLPickUpTimes);
                        listingExpiryTV.setText(FLExpiry);
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

        userRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (postSnapshot.getKey().equals(userId)) {
                        fName = user.getFirstName();
                        lName = user.getLastName();
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
                //Title
                //FLTitle
                //Image
                //profilePicIV
                String type = "Request";
                //User ID
                //FLProfileUserName
                String listingState = "Requested";

                String notificationID = mDatabaseRef.push().getKey();


                myNotification = new Notification(FLTitle, FLImage, type, userId, FLProfileUserName, notificationID, listingState, fullName);
                mDatabaseRef.child(notificationID).setValue(myNotification);
                finish();

            }
        });


//        getUserNameRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterable<DataSnapshot> children = snapshot.getChildren();
//                for (DataSnapshot child : children) {
//                    System.out.println("CURRENT USER" + currentUser);
//                    System.out.println("USER ID" + FLProfileUserName);
//                    if (currentUser.equals(FLProfileUserName)) {
//                        String userFirstName = currentUser.getFirstName();
//                        String userLastName = currentUser.getLastName();
//                        String theUserName = userFirstName + " " + userLastName;
//
//                        listingProfileUsername.setText(theUserName);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
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