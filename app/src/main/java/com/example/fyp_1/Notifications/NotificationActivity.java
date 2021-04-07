package com.example.fyp_1.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.fyp_1.AllListingsTab.Adapter;
import com.example.fyp_1.AllListingsTab.ViewFullListingActivity;
import com.example.fyp_1.AllListingsTab.viewListingActivity;
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.example.fyp_1.model.Listing;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Init RCV
        mRecyclerView = findViewById(R.id.recyclerViewNotification);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
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
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Notification not = postSnapshot.getValue(Notification.class);
                    if (not.getRecieverUserID().equals(userId)) {
                        mNotifications.add(not);
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

        return true;
    }

    @Override
    public void onListingApprovalClick(int position) {
        System.out.println("Approval CLICKEDDDDD");
        AlertDialog.Builder approvalDialog = new AlertDialog.Builder(NotificationActivity.this);
        approvalDialog.setTitle("Approve Request");
        approvalDialog.setMessage("Are you sure you wish to approve request?");

        approvalDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onListingClicked:  clicked");
                mNotifications.get(position);
                notificationID = mNotifications.get(position).getNotificationID();
                Log.d(TAG, "THE LISTING ID: " + notificationID);
                decision = approveDecision;

                String title = mNotifications.get(position).getItemName();
                String imageURL = mNotifications.get(position).getItemURL();
                String type = "Approved";
                String approvalUserID = userId;
                String UserRequestApprovedID = mNotifications.get(position).getSenderUserID();
                //String notificationID = mNotifications.get(position).getNotificationID();


                String notificationID = mDatabaseRef.push().getKey();
                myNotification = new Notification(title, imageURL, type, approvalUserID, UserRequestApprovedID, notificationID);
                mDatabaseRef.child(notificationID).setValue(myNotification);



                userReuseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            UserReuseTotal reuseRef = postSnapshot.getValue(UserReuseTotal.class);
                            if (postSnapshot.child("userID").getValue().equals(userId)) {
                                reuseNo1 = 0 + reuseRef.getReuseNumber();
                                System.out.println("REUSE NO" + reuseNo1);
                                reuseNo2 = reuseNo1 + 1;
                                System.out.println("REUSE NO" + reuseNo2);
                                theReuseID = reuseRef.getReuseID();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(NotificationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                if(reuseNo2 <1) {
                    String reuseID = userReuseRef.push().getKey();
                    myUserReuseTotal = new UserReuseTotal(approvalUserID, 1, reuseID);
                    userReuseRef.child(reuseID).setValue(myUserReuseTotal);
                }else{
                    userReuseRef.child(theReuseID).child("reuseNumber").setValue(reuseNo2);
                }

            }

        });
        approvalDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //builder.finish();
            }

        });
        //Create dialog box
        AlertDialog alert = approvalDialog.create();
        approvalDialog.show();

    }

    @Override
    public void onListingDeclineClick(int position) {
        System.out.println("Decline CLICKEDDDDD");
        AlertDialog.Builder declineDialog = new AlertDialog.Builder(NotificationActivity.this);
        declineDialog.setTitle("Decline Request");
        declineDialog.setMessage("Are you sure you wish to decline request?");

        declineDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onListingClicked:  clicked");
                mNotifications.get(position);
                notificationID = mNotifications.get(position).getNotificationID();
                Log.d(TAG, "THE LISTING ID: " + notificationID);

                String title = mNotifications.get(position).getItemName();
                String imageURL = mNotifications.get(position).getItemURL();
                String type = "Declined";
                String approvalUserID = userId;
                String UserRequestApprovedID = mNotifications.get(position).getSenderUserID();
                //String notificationID = mNotifications.get(position).getNotificationID();


                String notificationID = mDatabaseRef.push().getKey();
                myNotification = new Notification(title, imageURL, type, approvalUserID, UserRequestApprovedID, notificationID);
                mDatabaseRef.child(notificationID).setValue(myNotification);
                //finish();
                //mNotifications.remove(position);
                //Toast.makeText(NotificationActivity.this, "Request Declined", Toast.LENGTH_SHORT).show();
                //mAdapter.notifyDataSetChanged();

            }

        });
        declineDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //builder.finish();
            }

        });
        //Create dialog box
        AlertDialog alert = declineDialog.create();
        declineDialog.show();
    }

}