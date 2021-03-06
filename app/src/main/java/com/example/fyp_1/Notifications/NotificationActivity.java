package com.example.fyp_1.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.fyp_1.AllListingsTab.viewListingActivity;
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.RatingDialog.RatingDialog;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.example.fyp_1.model.Notification;
import com.example.fyp_1.model.UserReuseTotal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnListingListener {

    private static final String TAG = "NotificationActivity";

    //Clicked ID
    String notificationID;
    String approveDecision = "approve";
    String declineDecision = "decline";
    String decision;

    //Notification
    Notification myNotification;


    //RCV Components
    private List<Notification> mNotifications;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    Button declineBtn;

    //User Reuse total update
    UserReuseTotal myUserReuseTotal;
    int reuseNo1;
    int reuseNo2;
    String theReuseID;

    //Firebase
    DatabaseReference mDatabaseRef;
    DatabaseReference userReuseRef;
    DatabaseReference updateNotificationState;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;

    public static String userIDSent = "";
    String userIDToRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Init RCV
        mRecyclerView = findViewById(R.id.recyclerViewNotification);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mNotifications = new ArrayList<>();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NotificationAdapter(NotificationActivity.this, (ArrayList<Notification>) mNotifications, NotificationActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        declineBtn = (Button) findViewById(R.id.declineBtnNotifcation);

        //Init Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notifications");
        updateNotificationState = FirebaseDatabase.getInstance().getReference("notifications");
        userReuseRef = FirebaseDatabase.getInstance().getReference("userReuses");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();


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
                        Intent emptyIntent = new Intent(NotificationActivity.this, viewListingActivity.class);
                        emptyIntent.putExtra("ingredient_clicked", " ");
                        startActivity(emptyIntent);
                        overridePendingTransition(0, 0);
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
                mNotifications.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Notification not = postSnapshot.getValue(Notification.class);
                    if (not.getRecieverUserID().equals(userId)) {
                        mNotifications.add(not);
                        userIDToRate = not.getSenderUserID();
                        userIDSent = userIDToRate;
                    }

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            Intent profileIntent = new Intent(NotificationActivity.this, MyListingsProfileActivity.class);
            startActivity(profileIntent);
            return true;
        }
        if (id == R.id.notification_menu_icon) {
            Intent profileIntent = new Intent(NotificationActivity.this, NotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }
        if (id == R.id.request_notification_menu_icon) {
            Intent profileIntent = new Intent(NotificationActivity.this, RequestNotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onNotificationClick(int position) {

        Log.d(TAG, "onListingClicked:  clicked");
        mNotifications.get(position);
        String userIDToRate = mNotifications.get(position).getSenderUserID();
        Log.d(TAG, "USER ID TO RATE: " + userIDToRate);

        Intent i = new Intent(NotificationActivity.this, RatingDialog.class);
        i.putExtra("userIDKey", userIDToRate);
        CreateDialogWithRatingBar();

    }

    public void CreateDialogWithRatingBar() {
        // Rating Dialog
        RatingDialog ratingDialog = new RatingDialog(NotificationActivity.this);
        ratingDialog.startRatingDialog();

    }

    public void sendValue(){
        userIDToRate  = userIDToRate;
    }
}