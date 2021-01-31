package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class viewListingActivity extends AppCompatActivity {

    private List<Listing> mListings;
    private ProgressBar progressCircle;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabaseRef;
    SearchView mSearchView;
    SeekBar mSeekbar;
    ImageView profileImageView;

    //Profile Pic
//    private String userId;
//    private FirebaseUser user;
//    StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mListings = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        mRecyclerView.setLayoutManager(mLayoutManager);
        progressCircle = findViewById(R.id.progress_circle);
        mSearchView = findViewById(R.id.searchView);
        mSeekbar = findViewById(R.id.locationSeekBar);
        profileImageView = findViewById(R.id.profileImageViewOnListing);

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        userId = user.getUid();
//        StorageReference imgRef = storageRef.child(user.getUid() + ".jpg");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Listing listing = postSnapshot.getValue(Listing.class);
                    mListings.add(listing);
//                    if(listing.getUserId() == userId){
//                        profileImageView.setImageURI();
//                    }
                }
                mAdapter = new Adapter(viewListingActivity.this, (ArrayList<Listing>) mListings);
                mRecyclerView.setAdapter(mAdapter);
                progressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewListingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressCircle.setVisibility(View.INVISIBLE);

            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(mSearchView !=null) {
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

    }

    public void search(String str){
        ArrayList<Listing> list = new ArrayList<>();
        for(Listing obj : mListings){
            if(obj.getName().toLowerCase().contains(str.toLowerCase())){
                list.add(obj);
            }
        }
        Adapter adapterClass = new Adapter(viewListingActivity.this, (ArrayList<Listing>) list);
        mRecyclerView.setAdapter(adapterClass);
    }


}