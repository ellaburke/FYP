package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.fyp_1.model.Listing;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewListingActivity extends AppCompatActivity {

    private List<Listing> mListings;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabaseRef;
    SearchView mSearchView;

    //FAB
    FloatingActionButton uploadListingFAB;


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
        mSearchView = findViewById(R.id.searchView);

        //init FAB
        uploadListingFAB = (FloatingActionButton) findViewById(R.id.fab_upload_listing);

        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.MyKitchenNav);

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
                        return true;
                    case R.id.MyShoppingListNav:
                        startActivity(new Intent(getApplicationContext(), MyShoppingListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Listing listing = postSnapshot.getValue(Listing.class);
                    mListings.add(listing);
                }
                mAdapter = new Adapter(viewListingActivity.this, (ArrayList<Listing>) mListings);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewListingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        if (mSearchView != null) {
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

        uploadListingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadListingIntent = new Intent(viewListingActivity.this, addListingActivity.class);
                startActivity(uploadListingIntent);
            }
        });

    }

    public void search(String str) {
        ArrayList<Listing> list = new ArrayList<>();
        for (Listing obj : mListings) {
            if (obj.getName().toLowerCase().contains(str.toLowerCase())) {
                list.add(obj);
            }
        }
        Adapter adapterClass = new Adapter(viewListingActivity.this, (ArrayList<Listing>) list);
        mRecyclerView.setAdapter(adapterClass);
    }


}