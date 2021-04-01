package com.example.fyp_1.ShoppingListTab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp_1.Maps.MapToShop;
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.Recipe.ViewFullRecipeActivity;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.example.fyp_1.model.MyShoppingListItem;
import com.example.fyp_1.AllListingsTab.viewListingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyShoppingListActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "MyKitchenIngredients";

    //Firebase Components
    private FirebaseUser user;
    private String userId;
    DatabaseReference mDatabaseRef;


    //RCV Components
    RecyclerView myShoppingListRecyclerView;

    //MyShoppingListItem & List
    MyShoppingListItem myShoppingListItem;
    ArrayList<MyShoppingListItem> myShoppingListItems = new ArrayList<>();
    String itemId;
    String myShoppingListItemEntry;

    //XML Componenets
    FloatingActionButton addToListByDocScan;
    EditText shoppingListItemInput;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping_list);

        //Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("myShoppingListItems");

        //RCV
        myShoppingListRecyclerView = findViewById(R.id.myShoppingListRecyclerView);
        MyShoppingListAdapter myShoppingListAdapter = new MyShoppingListAdapter(myShoppingListItems);
        myShoppingListRecyclerView.setAdapter(myShoppingListAdapter);
        myShoppingListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.MyShoppingListNav);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MyKitchenNav:
                        startActivity(new Intent(getApplicationContext(), MyKitchenIngredients2.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.SearchListingNav:
                        startActivity(new Intent(getApplicationContext(), viewListingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.MyShoppingListNav:
                        return true;
                }
                return false;
            }
        });

        //XML Components
        addToListByDocScan = (FloatingActionButton) findViewById(R.id.fab_scan_doc);
        addToListByDocScan.setTooltipText("Scan to Add To List");
        shoppingListItemInput = (EditText) findViewById(R.id.shopping_list_item_input);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myShoppingListItems.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MyShoppingListItem mSLI = postSnapshot.getValue(MyShoppingListItem.class);
                    if (mSLI.getUserId().equals(userId)) {
                        myShoppingListItems.add(mSLI);
                    }
                }
                myShoppingListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyShoppingListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addToListByDocScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Text Recognition Scan Recipe
            }
        });

        shoppingListItemInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if (!shoppingListItemInput.getText().toString().isEmpty()) {
                        myShoppingListItemEntry = shoppingListItemInput.getText().toString();
                        myShoppingListItem = new MyShoppingListItem(myShoppingListItemEntry, itemId, userId);
                        itemId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(itemId).setValue(myShoppingListItem);
                    }
                    shoppingListItemInput.setText("");
                    return true;
                }
                return false;

            }
        });
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
            Intent profileIntent = new Intent(MyShoppingListActivity.this, MyListingsProfileActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return true;
    }
}