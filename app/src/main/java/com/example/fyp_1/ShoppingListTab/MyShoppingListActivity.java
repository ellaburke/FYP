package com.example.fyp_1.ShoppingListTab;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.MyKitchenItem;
import com.example.fyp_1.Notifications.NotificationActivity;
import com.example.fyp_1.Notifications.RequestNotificationActivity;
import com.example.fyp_1.R;
import com.example.fyp_1.TextRecognition.TextRecognitionActivity;
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

public class MyShoppingListActivity extends AppCompatActivity implements MyShoppingListAdapter.OnListingListener{

    //TAG
    private static final String TAG = "MyKitchenIngredients";

    //Firebase Components
    private FirebaseUser user;
    private String userId;
    DatabaseReference mDatabaseRef;
    DatabaseReference rootRef;
    DatabaseReference deleteRef;
    DatabaseReference mDeleteDatabaseRef;

    MyShoppingListItem currentItem;
    String itemToDeleteID;
    MyKitchenItem myKitchenItem;

    //Clicked ID
    String itemID;


    //RCV Components
    RecyclerView myShoppingListRecyclerView;

    //MyShoppingListItem & List
    MyShoppingListItem myShoppingListItem;
    ArrayList<MyShoppingListItem> myShoppingListItems = new ArrayList<>();
    String itemId;
    String myShoppingListItemEntry;
    MyShoppingListAdapter myShoppingListAdapter;

    //XML Componenets
    FloatingActionButton addToListByDocScan, deleteFromList;
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
        rootRef = FirebaseDatabase.getInstance().getReference();
        deleteRef = rootRef.child("myShoppingListItems");
        mDeleteDatabaseRef = FirebaseDatabase.getInstance().getReference("myShoppingListItems");

        //Retrieve itemsList and saved it to DB
        Intent prevIntent = getIntent();
        if(prevIntent != null){
            String itemList = prevIntent.getStringExtra("addToDB");
            if(itemList !=null && !(itemList.isEmpty())) {
                String[] items = itemList.split("\n");
                for(String item: items) {
                    itemId = mDatabaseRef.push().getKey();
                    myShoppingListItem = new MyShoppingListItem(item, itemId, userId);
                    mDatabaseRef.child(itemId).setValue(myShoppingListItem);
                }
            }
        }

        //RCV
        myShoppingListRecyclerView = findViewById(R.id.myShoppingListRecyclerView);
        myShoppingListAdapter = new MyShoppingListAdapter(myShoppingListItems, MyShoppingListActivity.this);
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
                        Intent emptyIntent = new Intent(MyShoppingListActivity.this, viewListingActivity.class);
                        emptyIntent.putExtra("ingredient_clicked", " ");
                        startActivity(emptyIntent);
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
        //addToListByDocScan.setTooltipText("Scan to Add To List");
        shoppingListItemInput = (EditText) findViewById(R.id.shopping_list_item_input);
//        deleteFromList.setTooltipText("Delete Checked Ingredients");
//        deleteFromList = (FloatingActionButton) findViewById(R.id.fab_bin_ing);

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
                System.out.println("Scanbutton clicked");
                startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                overridePendingTransition(0,0);
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
                        itemId = mDatabaseRef.push().getKey();
                        myShoppingListItem = new MyShoppingListItem(myShoppingListItemEntry, itemId, userId);

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
        getMenuInflater().inflate(R.menu.request_notification_menu, menu);
        getMenuInflater().inflate(R.menu.notification_menu, menu);
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
        if (id == R.id.notification_menu_icon) {
            Intent profileIntent = new Intent(MyShoppingListActivity.this, NotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }
        if (id == R.id.request_notification_menu_icon) {
            Intent profileIntent = new Intent(MyShoppingListActivity.this, RequestNotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onItemToRemoveFromList(int position) {
        Log.d(TAG, "CHECKEDDDD");
        myShoppingListItems.get(position);
        itemID = myShoppingListItems.get(position).getShoppingListItemId();
        itemToDeleteID = itemID;
        System.out.println("Item to delete" + itemID);
        //myShoppingListItems.remove(itemID);
        //myShoppingListAdapter.notifyDataSetChanged();

        deleteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    currentItem = child.getValue(MyShoppingListItem.class);
                    System.out.println("Current item" + currentItem.getShoppingListItemId());
                    if(currentItem.getShoppingListItemId().equals(itemID)){
                        String mSLI = currentItem.getShoppingListItemId();
                        mDeleteDatabaseRef.child(mSLI).removeValue();

                    }
                    myShoppingListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());
            }


        });

    }
}