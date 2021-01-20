package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyListings extends AppCompatActivity {

    private static final String TAG = "MyListings";
    private static final String LISTING = "listings";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference rootRef;
    DatabaseReference userRef;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Listing> mListings;
    DatabaseReference mDatabaseRef;
    DatabaseReference mDeleteDatabaseRef;
    SearchView mSearchView;
    private String userId;
    TextView myListingsTitle;
    Button dairyButton;
    Button cerealOrBreadButton;
    Button meatOrPoultryButton;
    Button fishButton;
    Button cupboardButton;
    Button fruitButton;
    Button vegButton;
    Button showAllButton;
    String categorySelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(LISTING);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mListings = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        mDeleteDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = findViewById(R.id.searchView);
        myListingsTitle = findViewById(R.id.myCurrentListingsTitle);
        dairyButton = (Button) findViewById(R.id.filter_btn_dairy);
        cerealOrBreadButton = (Button) findViewById(R.id.filter_btn_cereal);
        fruitButton = (Button) findViewById(R.id.filter_btn_fruit);
        vegButton = (Button) findViewById(R.id.filter_btn_veg);
        fishButton = (Button) findViewById(R.id.filter_btn_fish);
        cupboardButton = (Button) findViewById(R.id.filter_btn_cupboard);
        meatOrPoultryButton = (Button) findViewById(R.id.filter_btn_meat_or_poultry);
        showAllButton = (Button) findViewById(R.id.filter_btn_show_all);


        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Listing listing = postSnapshot.getValue(Listing.class);
                    if (listing.getUserId().equals(userId)) {
                        mListings.add(listing);
                    }
                    mAdapter = new Adapter(MyListings.this, (ArrayList<Listing>) mListings);
                    mRecyclerView.setAdapter(mAdapter);
                    myListingsTitle.setText("My Listings (" + mListings.size() + ")");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyListings.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        dairyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = dairyButton.getText().toString();
                searchByButton();
            }
        });
        cerealOrBreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = cerealOrBreadButton.getText().toString();
                searchByButton();
            }
        });
        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = fishButton.getText().toString();
                searchByButton();
            }
        });
        meatOrPoultryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = meatOrPoultryButton.getText().toString();
                searchByButton();
            }
        });
        cupboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = cupboardButton.getText().toString();
                searchByButton();
            }
        });
        fruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = fruitButton.getText().toString();
                searchByButton();
            }
        });
        vegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelected = vegButton.getText().toString();
                searchByButton();
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


//    @Override
//    public void onListingClick(int position) {
//        Log.d(TAG, "onListingClicked:  clicked");
//        mListings.get(position);
//        listingID = mListings.get(position).getListingId();
//        Log.d(TAG, "THE LISTING ID: " + listingID);
//        //dialog.show();
//        //Intent updateListingIntent = new Intent(this, UpdateMyListingActivity.class);
//        //updateListingIntent.putExtra("selected_listing_to_update", mListings.get(position));
//        //updateListingIntent.putExtra("selected_listing_to_update", listingID);
//        //startActivity(updateListingIntent);
//
//    }


//    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            mListings.remove(viewHolder.getAdapterPosition());
//            mAdapter.notifyDataSetChanged();
//            listingToDelete = listingID;
//            Log.d(TAG, "LISTING TO DELETE 2: " + listingToDelete);
//            userRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Iterable<DataSnapshot> children = snapshot.getChildren();
//                    for (DataSnapshot child : children) {
//                        currentItem = child.getValue(Listing.class);
//                        if (currentItem.getListingId().equals(listingID)) {
//                            mDeleteDatabaseRef.child(listingID).removeValue();
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e(TAG, "onCancelled", error.toException());
//                }
//
//
//            });
//        }
//    };
    }

    public void search(String str) {
        ArrayList<Listing> list = new ArrayList<>();
        for (Listing obj : mListings) {
            if (obj.getName().toLowerCase().contains(str.toLowerCase()) || obj.getLocation().toLowerCase().contains(str.toLowerCase())) {
                list.add(obj);
            }
        }
        Adapter adapterClass = new Adapter(MyListings.this, (ArrayList<Listing>) list);
        mRecyclerView.setAdapter(adapterClass);
    }

    public void searchByButton() {
        ArrayList<Listing> list = new ArrayList<>();
        for (Listing obj2 : mListings) {
            if (obj2.getCategory().toLowerCase().contains(categorySelected.toLowerCase())) {
                list.add(obj2);
            }
        }
        Adapter adapterClass = new Adapter(MyListings.this, (ArrayList<Listing>) list);
        mRecyclerView.setAdapter(adapterClass);
    }
}