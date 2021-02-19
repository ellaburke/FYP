package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class GroceryListActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    RecyclerView mFruitRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //Variables & Constants
    private static final String USER = "user";
    private static final String TAG = "ShoppingListActivity";
    private String userId;
    private FirebaseUser user;
    private DatabaseReference mDatabaseRef;
    DatabaseReference updateRef;
    String itemToAdd;
    String email;
    String item;
    List<String> myGroceryList;
    EditText addGroceryItem;
    TextView dairyTV;
    GroceryList myGroceryListItem;
    ArrayList<GroceryList> userGroceryList = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("groceryListItems");
        updateRef = FirebaseDatabase.getInstance().getReference("user");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USER).child(userId).child("usersGroceryList");
        //DatabaseReference accessListRef = FirebaseDatabase.getInstance().getReference("groceryListItems").child(userId);

        //Veg RCV
        mRecyclerView = findViewById(R.id.vegtables_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        //mAdapter = new MyKitchenIngredientsAdapter(GroceryListActivity.this, ids);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Fruit RCV
        mFruitRecyclerView = findViewById(R.id.fruit_recycler_view);

        myGroceryList = new ArrayList<>();
        dairyTV = (TextView) findViewById(R.id.dairyFill);
        addGroceryItem = (EditText) findViewById(R.id.input_grocery_item);

//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d(TAG, "Working 1" );
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    String listItem = postSnapshot.getKey();
//                    Log.d(TAG, "Working 2 " + listItem );
//                    //userGroceryList.add(listItem);
//                    mAdapter.notifyItemInserted(userGroceryList.size() - 1);
//                }
//
//
//            }

//             mDatabaseRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.d(TAG, "Working 1" );
//                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        GroceryList listItem = postSnapshot.getValue(GroceryList.class);
//                        Log.d(TAG, "Working 2 " + listItem );
//                        //if (mDatabaseRef.child(userId).equals(userId)) {
//                            userGroceryList.add(listItem);
//
//                        mAdapter = new ListAdapter(GroceryListActivity.this,  userGroceryList);
//                        mRecyclerView.setAdapter(mAdapter);
//                        //mAdapter.notifyItemInserted(userGroceryList.size() - 1);
//                    }
//                }

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    ids.add(childSnapshot.getValue().toString());

                }
                Log.d(TAG, "Working 2 " + ids );
                mAdapter.notifyItemInserted(ids.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroceryListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read value.", error.toException());
            }


        });

        addGroceryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToGroceryList();
                addGroceryItem.setText("");
            }
        });

    }

    private void addItemToGroceryList() {
        itemToAdd = addGroceryItem.getText().toString();
        myGroceryList.add(itemToAdd);
        myGroceryListItem = new GroceryList(itemToAdd,userId);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        for (String item : myGroceryList) {
            //mDatabaseRef.child(userId).child("usersGroceryList").child(item).setValue(true);
            mDatabaseRef.child(userId).child(item).setValue(true);
        }
    }
}