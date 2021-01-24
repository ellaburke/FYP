package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
    List<User> myGroceryList;
    ArrayList<String> ingredients = new ArrayList<>();
    EditText addGroceryItem;
    //TextView dairyTV;

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
        DatabaseReference userRef = rootRef.child(USER);

        mRecyclerView = findViewById(R.id.dairy_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        myGroceryList = new ArrayList<>();
        mRecyclerView.setLayoutManager(mLayoutManager);

        addGroceryItem = (EditText) findViewById(R.id.input_grocery_item);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User groceryItem = postSnapshot.getValue(User.class);
                    if (groceryItem.getEmail().equals(email)) {
                        myGroceryList.add(groceryItem);
                    }
                    mAdapter = new ListAdapter(ShoppingListActivity.this, (ArrayList<User>) myGroceryList);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShoppingListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

//                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot keyId : snapshot.getChildren()) {
//                        if (keyId.child("email").getValue().equals(email)) {
//                            for (DataSnapshot ing : childDataSnapshot.child("usersGroceryList").getChildren()) {
//                                ingredients.add(ing.child("usersGroceryList").getValue(String.class));
//                                //break;
//                            }
//                        }
//                        Log.d(TAG, String.valueOf(ingredients));
//                        //dairyTV.setText( ingredients.toString());
//                        mAdapter = new Adapter(ShoppingListActivity.this, (ArrayList<User>) myGroceryList);
//                        mRecyclerView.setAdapter(mAdapter);
//                    }
//
//
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        });
        addGroceryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addItemToGroceryList();
            }
        });

    }

//    private void addItemToGroceryList() {
//        itemToAdd = addGroceryItem.getText().toString();
//        myGroceryList.add(itemToAdd);
//
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        for (String item : myGroceryList) {
//            updateRef.child(userId).child("usersGroceryList").child(item).setValue(true);
//        }
//    }
}