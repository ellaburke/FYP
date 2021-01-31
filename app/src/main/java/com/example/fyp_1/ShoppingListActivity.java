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

public class ShoppingListActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
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
    ArrayList<String> ingredients = new ArrayList<>();
    EditText addGroceryItem;
    TextView dairyTV;
    ArrayList<User> userGroceryList = new ArrayList<User>();

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

        mRecyclerView = findViewById(R.id.vegtables_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListAdapter(ShoppingListActivity.this, userGroceryList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        myGroceryList = new ArrayList<String>();
        dairyTV = (TextView) findViewById(R.id.dairyFill);
        addGroceryItem = (EditText) findViewById(R.id.input_grocery_item);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Working 1" );
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String listItem = postSnapshot.getKey();
                    Log.d(TAG, "Working 2 " + listItem );
                    //userGroceryList.add(listItem);
                    mAdapter.notifyItemInserted(userGroceryList.size() - 1);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShoppingListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        for (String item : myGroceryList) {
            updateRef.child(userId).child("usersGroceryList").child(item).setValue(true);
        }
    }
}