package com.example.fyp_1.AllListingsTab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.MyKitchenItem;
import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
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
    ImageView filterByCategory;

    //FAB
    FloatingActionButton uploadListingFAB;

    //Filter Option4
    String myItemCategory;


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        filterByCategory = (ImageView) findViewById(R.id.filterByCategory);


        //init FAB
        uploadListingFAB = (FloatingActionButton) findViewById(R.id.fab_upload_listing);
        uploadListingFAB.setTooltipText("Upload Listing");

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

//        //If ingredient passed from recipe
//        //Get Intent from ViewMyFullListing
//        Intent i = getIntent();
//        String listingToSearch = getIntent().getStringExtra("ingredient_clicked");
//        listingToSearch = listingToSearch.substring(0, listingToSearch.length() - 1);
//
//            mSearchView.setQuery(listingToSearch, false);



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

        filterByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(viewListingActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.filter_by_category_dialog, null);
                //init variables for dialog
                TextView cancelTV = (TextView) mView.findViewById(R.id.cancel_dialog_option1);
                TextView addTV = (TextView) mView.findViewById(R.id.add_dialog_option1);
                RadioGroup radioGroup = findViewById(R.id.radioGroupCategory);
                final RadioButton[] selectedRadioButton = new RadioButton[1];

                addTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                            selectedRadioButton[0] = findViewById(selectedRadioButtonId);
                            String selectedRbText = selectedRadioButton[0].getText().toString();
                            System.out.println("SELECTED" + selectedRbText);


                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                cancelTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    public void search(String str) {
        ArrayList<Listing> list = new ArrayList<>();
        for (Listing obj : mListings) {
            if (obj.getName().toLowerCase().contains(str.toLowerCase()) ) {
                list.add(obj);
            }
        }
        Adapter adapterClass = new Adapter(viewListingActivity.this, (ArrayList<Listing>) list);
        mRecyclerView.setAdapter(adapterClass);
    }

    public void searchByCategory() {
        ArrayList<Listing> list = new ArrayList<>();
        for (Listing obj2 : mListings) {
            if (obj2.getCategory().toLowerCase().contains(myItemCategory.toLowerCase())) {
                list.add(obj2);
            }
        }
        Adapter adapterClass = new Adapter(viewListingActivity.this, (ArrayList<Listing>) list);
        mRecyclerView.setAdapter(adapterClass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile_icon) {
            Intent profileIntent = new Intent(viewListingActivity.this, MyListingsProfileActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return true;
    }


}