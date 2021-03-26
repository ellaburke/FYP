package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp_1.model.MyShoppingListAdapter;
import com.example.fyp_1.model.MyShoppingListItem;
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
        bottomNavigationView.setSelectedItemId(R.id.MyKitchenNav);

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
                //Intent textRecognitionIntent = new Intent(MyShoppingListActivity.this, TextRecognitionActivity.class);
                //startActivity(textRecognitionIntent);
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
}