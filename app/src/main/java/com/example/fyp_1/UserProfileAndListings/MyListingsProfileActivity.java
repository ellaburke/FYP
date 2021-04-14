package com.example.fyp_1.UserProfileAndListings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_1.AllListingsTab.viewListingActivity;
import com.example.fyp_1.LoginAndRegister.LoginActivity;
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.MyKitchenItem;
import com.example.fyp_1.R;
import com.example.fyp_1.Recipe.ViewFullRecipeActivity;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.model.Listing;
import com.example.fyp_1.model.UserRating;
import com.example.fyp_1.model.UserReuseTotal;
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

public class MyListingsProfileActivity extends AppCompatActivity implements MyListingProfileAdapter.OnListingListener {

    private static final String LISTING = "listings";
    private static final String USER = "user";
    private static final String TAG = "ProfileListings";

    //Firebase Components
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference userRef;
    DatabaseReference mDatabaseRef, mDatabaseRatingRef;
    DatabaseReference rootRef;
    DatabaseReference userProfileRef;
    StorageReference storageRef;
    StorageReference profilerUpdateRef;
    DatabaseReference userRootRef;
    DatabaseReference userReuseRef;
    DatabaseReference userPRef;
    private String userId;
    String email;
    String userEmail;
    String fullName = "";
    String fName, lName;
    //RCV Components
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Listing> mListings;
    String listingID;
    //UI Components
    ImageView uploadProfileImage;
    private Uri selectedImage;
    TextView userNameTV, reuseTotalTV;
    Button editProfileButton, logOutProfileButton;
    UserReuseTotal currentItem;
    int noOfReuse;
    String numberOfReuse;

    //Rating Bar
    RatingBar userRatingBar;
    private List<Float> mRatings;
    Float floatValue;
    Float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings_profile);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        //Init firebase
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        //Listing ref
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(LISTING);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        //User ref
        userRootRef = FirebaseDatabase.getInstance().getReference("user");
        userPRef = userRootRef.child(USER);
        userEmail = user.getEmail();
        //Profile Pic ref
        storageRef = FirebaseStorage.getInstance().getReference();
        profilerUpdateRef = storageRef.child(user.getUid() + ".jpg");
        //Reuse ref
        userReuseRef = FirebaseDatabase.getInstance().getReference("userReuses");
        //Rating ref
        mDatabaseRatingRef = FirebaseDatabase.getInstance().getReference("userRating");

        //Init UI
        uploadProfileImage = (ImageView) findViewById(R.id.listingProfileImageView);
        uploadProfileImage.setImageURI(selectedImage);
        userNameTV = (TextView) findViewById(R.id.profile_listing_name_display);
        editProfileButton = (Button) findViewById(R.id.editMyProfileBtn);
        logOutProfileButton = (Button) findViewById(R.id.logOutProfileBtn);
        reuseTotalTV = (TextView) findViewById(R.id.profile_listing_reuseTV);
        userRatingBar = (RatingBar) findViewById(R.id.profile_listing_ratingBar);
        //rating array
        mRatings = new ArrayList<>();

        //Init RCV
        mRecyclerView = findViewById(R.id.myListingsOnProfileRCV);
        int numberOfColumns = 3;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mListings = new ArrayList<>();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mAdapter = new MyListingProfileAdapter(MyListingsProfileActivity.this, (ArrayList<Listing>) mListings, MyListingsProfileActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        logOutProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logoutIntent = new Intent(MyListingsProfileActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
            }
        });

        profilerUpdateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(uploadProfileImage);
            }
        });


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Listing listing = postSnapshot.getValue(Listing.class);
                    if (listing.getUserId().equals(userId)) {
                        mListings.add(listing);
                    }

                   mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyListingsProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        mDatabaseRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UserRating UR = postSnapshot.getValue(UserRating.class);
                    if (UR.getUserID().equals(userId)) {
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
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(userEmail)) {
                        fName = keyId.child("firstName").getValue(String.class);
                        lName = keyId.child("lastName").getValue(String.class);
                        System.out.println("FN" + fName);
                        break;
                    }
                }
                fullName = fName + " " + lName;
                System.out.println("FN2" + fName);
                userNameTV.setText(fullName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        userReuseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    currentItem = child.getValue(UserReuseTotal.class);
                    System.out.println("Current item" + currentItem.getReuseID());
                    if(currentItem.getUserID().equals(userId)){
                        noOfReuse = currentItem.getReuseNumber();
                        numberOfReuse = String.valueOf(noOfReuse);
                    }

                } reuseTotalTV.setText(numberOfReuse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());
            }


        });




        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(MyListingsProfileActivity.this, EditProfileActivity.class);
                editProfileIntent.putExtra("email", user.getEmail());
                startActivity(editProfileIntent);
            }
        });

        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        //bottomNavigationView.setSelectedItemId(R.id.MyKitchenNav);

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
                        Intent emptyIntent = new Intent(MyListingsProfileActivity.this, viewListingActivity.class);
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
            Intent backToProfileIntent = new Intent(MyListingsProfileActivity.this, MyKitchenIngredients2.class);
            startActivity(backToProfileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onListingClick(int position) {
        Log.d(LISTING, "onListingClicked:  clicked");
        mListings.get(position);
        listingID = mListings.get(position).getListingId();
        Log.d(LISTING, "THE LISTING ID: " + listingID);
        Intent viewFullListingIntent = new Intent(this, ViewMyFullListing.class);
        viewFullListingIntent.putExtra("selected_listing_to_display", listingID);
        startActivity(viewFullListingIntent);

    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
}