package com.example.fyp_1.AllListingsTab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.fyp_1.Notifications.NotificationActivity;
import com.example.fyp_1.Notifications.RequestNotificationActivity;
import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.UserProfileAndListings.MyListingProfileAdapter;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.example.fyp_1.UserProfileAndListings.ViewMyFullListing;
import com.example.fyp_1.model.Listing;
import com.example.fyp_1.model.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewListingActivity extends AppCompatActivity implements Adapter.OnListingListener {

    private static final String TAG = "viewListingActivity";

    private List<Listing> mListings;
    private List<String> mNotifications;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference mDatabaseRef, mDatabaseNotificationStateRef;
    SearchView mSearchView;
    Button filterByCategory;

    //FAB
    FloatingActionButton uploadListingFAB;

    //Filter Option4
    String myItemCategory;

    //Clicked ID
    String listingID;

    //Alert Dialog
    AlertDialog alertDialogWithRadioButtons;
    String optionSelectedForFilter;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mListings = new ArrayList<>();
        mNotifications = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        mDatabaseNotificationStateRef = FirebaseDatabase.getInstance().getReference("notificationRequests");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = findViewById(R.id.searchView);
        //mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);
        filterByCategory = (Button) findViewById(R.id.filterByCategory);


        //init FAB
        uploadListingFAB = (FloatingActionButton) findViewById(R.id.fab_upload_listing);
//        uploadListingFAB.setTooltipText("Upload Listing");

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

        Intent i = getIntent();
        String listingToSearch2 = getIntent().getStringExtra("ingredient_clicked");
        String listingToSearch = "";
//        if(!listingToSearch2.equals(" ")) {
//             listingToSearch = listingToSearch2.substring(0, listingToSearch2.length() - 1);
//        }

        //Get notification request state and compare to listing
        mDatabaseNotificationStateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Notification not = postSnapshot.getValue(Notification.class);
                    //assert not != null;
                    if (not.getListingState().equals("Approved")) {
                        mNotifications.add(not.getListingID());
                        System.out.println("NOTIFICATIONS" + mNotifications);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewListingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        final String searchListing = listingToSearch2;
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Listing listing = postSnapshot.getValue(Listing.class);
                    if (!mNotifications.contains(listing.getListingId())) {
                        mListings.add(listing);
                    }
                }
                mAdapter = new Adapter(viewListingActivity.this, (ArrayList<Listing>) mListings, viewListingActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                if (!searchListing.isEmpty() && !searchListing.equals(" ")) {
                    search(searchListing);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewListingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        if (listingToSearch2.equals(" ")) {
            mSearchView.setFocusedByDefault(false);
        } else {
            mSearchView.setQuery(listingToSearch2, true);
            mSearchView.setFocusedByDefault(true);
        }


        if (mSearchView.getQuery() != null) {
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    search(query);
                    return true;
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

                CreateDialogWithRadioButtons();
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

        Adapter adapterClass = new Adapter(viewListingActivity.this, (ArrayList<Listing>) list, viewListingActivity.this);
        mRecyclerView.setAdapter(adapterClass);
    }

    public void searchByCategory(String optionSelectedForFilter) {
        ArrayList<Listing> list = new ArrayList<>();
        for (Listing obj2 : mListings) {
            if (optionSelectedForFilter.equals("All")) {
                    list.add(obj2);
            }
            else if (obj2.getCategory().toLowerCase().contains(optionSelectedForFilter.toLowerCase())) {
                list.add(obj2);
            }
        }
        Adapter adapterClass = new Adapter(viewListingActivity.this, (ArrayList<Listing>) list, viewListingActivity.this);
        mRecyclerView.setAdapter(adapterClass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request_notification_menu, menu);
        getMenuInflater().inflate(R.menu.notification_menu, menu);
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
        if (id == R.id.notification_menu_icon) {
            Intent profileIntent = new Intent(viewListingActivity.this, NotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }
        if (id == R.id.request_notification_menu_icon) {
            Intent profileIntent = new Intent(viewListingActivity.this, RequestNotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onListingClick(int position) {
        Log.d(TAG, "onListingClicked:  clicked");
        mListings.get(position);
        listingID = mListings.get(position).getListingId();
        Log.d(TAG, "THE LISTING ID: " + listingID);
        Intent viewFullListingIntent = new Intent(this, ViewFullListingActivity.class);
        viewFullListingIntent.putExtra("selected_listing_to_display", listingID);
        startActivity(viewFullListingIntent);

    }

    public void CreateDialogWithRadioButtons(){
        CharSequence[] value = {"All", "Fruit", "Veg", "Bread/Cereal", "Cupboard", "Dairy", "Meat/Poultry", "Fish", "Freezer"};

        AlertDialog.Builder builder = new AlertDialog.Builder(viewListingActivity.this);
        builder.setTitle("Filter By Category");

        builder.setSingleChoiceItems(value, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0 :
                        System.out.println("All Clicked");
                        optionSelectedForFilter = "All";
                        break;
                    case 1 :
                        System.out.println("Fruit Clicked");
                        optionSelectedForFilter = "Fruit";
                        break;
                    case 2 :
                        System.out.println("Veg Clicked");
                        optionSelectedForFilter = "Veg";
                        break;
                    case 3 :
                        System.out.println("Bread/Cereal Clicked");
                        optionSelectedForFilter = "Bread/Cereal";
                        break;
                    case 4 :
                        System.out.println("Cupboard");
                        optionSelectedForFilter = "Cupboard";
                        break;
                    case 5 :
                        System.out.println("Dairy");
                        optionSelectedForFilter = "Dairy";
                        break;
                    case 6 :
                        System.out.println("Meat/Poultry");
                        optionSelectedForFilter = "Meat/Poultry";
                        break;
                    case 7 :
                        System.out.println("Fish");
                        optionSelectedForFilter = "Fish";
                        break;
                    case 8 :
                        System.out.println("Freezer");
                        optionSelectedForFilter = "Freezer";
                        break;
                }
                alertDialogWithRadioButtons.dismiss();
                searchByCategory(optionSelectedForFilter);

            }
        });

        alertDialogWithRadioButtons = builder.create();
        alertDialogWithRadioButtons.show();

    }


}