package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_1.model.Listing;
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
import java.util.List;

public class MyListingsProfileActivity extends AppCompatActivity implements MyListingProfileAdapter.OnListingListener{

    private static final String LISTING = "listings";
    private static final String USER = "user";

    //Firebase Components
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference userRef;
    DatabaseReference mDatabaseRef;
    DatabaseReference rootRef;
    DatabaseReference userProfileRef;
    StorageReference storageRef;
    StorageReference profilerUpdateRef;
    private String userId;
    String email;
    String fullName;
    //RCV Components
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Listing> mListings;
    String listingID;
    //UI Components
    ImageView uploadProfileImage;
    private Uri selectedImage;
    TextView userNameTV;
    Button editProfileButton;


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
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(LISTING);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        storageRef = FirebaseStorage.getInstance().getReference();
        profilerUpdateRef = storageRef.child(user.getUid() + ".jpg");
        DatabaseReference userProfileRef = rootRef.child(USER);


        //Init UI
        uploadProfileImage = (ImageView) findViewById(R.id.listingProfileImageView);
        uploadProfileImage.setImageURI(selectedImage);
        userNameTV = (TextView) findViewById(R.id.profile_listing_name_display);
        editProfileButton = (Button) findViewById(R.id.editMyProfileBtn);

        //Init RCV
        mRecyclerView = findViewById(R.id.myListingsOnProfileRCV);
        int numberOfColumns = 3;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mListings = new ArrayList<>();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,numberOfColumns));

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
                    mAdapter = new MyListingProfileAdapter(MyListingsProfileActivity.this, (ArrayList<Listing>) mListings, MyListingsProfileActivity.this);
                    mRecyclerView.setAdapter(mAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyListingsProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        String fName = keyId.child("firstName").getValue(String.class);
                        String lName = keyId.child("lastName").getValue(String.class);
                        fullName = fName + " " + lName;

                        break;
                    }
                }
                userNameTV.setText(fullName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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