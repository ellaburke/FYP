package com.example.fyp_1.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.fyp_1.AllListingsTab.viewListingActivity;
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.example.fyp_1.model.Notification;
import com.example.fyp_1.model.User;
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

public class RequestNotificationActivity extends AppCompatActivity implements RequestNotificationAdapter.OnListingListener{

    private static final String TAG = "RequestNotification";

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
    DatabaseReference userRootRef;
    DatabaseReference updateNotificationState;
    DatabaseReference mDatabaseRequestRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    String notificationIDInList;

    //User
    String fullName, fName, lName, pNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_notification);


        //Init RCV
        mRecyclerView = findViewById(R.id.recyclerViewRequestNotification);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mNotifications = new ArrayList<>();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RequestNotificationAdapter(RequestNotificationActivity.this, (ArrayList<Notification>) mNotifications, RequestNotificationActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        declineBtn = (Button) findViewById(R.id.declineBtnNotifcation);

        //Init Firebase
        mDatabaseRequestRef = FirebaseDatabase.getInstance().getReference("notificationRequests");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notifications");
        updateNotificationState = FirebaseDatabase.getInstance().getReference("notificationRequests");
        userReuseRef = FirebaseDatabase.getInstance().getReference("userReuses");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        //User ref
        userRootRef = FirebaseDatabase.getInstance().getReference("user");


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
                        Intent emptyIntent = new Intent(RequestNotificationActivity.this, viewListingActivity.class);
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

        userRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (postSnapshot.getKey().equals(userId)) {
                        fName = user.getFirstName();
                        lName = user.getLastName();
                        pNumber = user.getPhoneNumber();
                        System.out.println("FN" + fName);
                        break;

                    }
                }
                fullName = fName + " " + lName;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mDatabaseRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mNotifications.clear();
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
        getMenuInflater().inflate(R.menu.request_notification_menu, menu);
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile_icon) {
            Intent profileIntent = new Intent(RequestNotificationActivity.this, MyListingsProfileActivity.class);
            startActivity(profileIntent);
            return true;
        }
        if (id == R.id.notification_menu_icon) {
            Intent profileIntent = new Intent(RequestNotificationActivity.this, NotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }
        if (id == R.id.request_notification_menu_icon) {
            Intent profileIntent = new Intent(RequestNotificationActivity.this, RequestNotificationActivity.class);
            startActivity(profileIntent);
            return true;
        }

        return true;
    }

    @Override
    public void onListingApprovalClick(int position) {
        System.out.println("Approval CLICKEDDDDD");
        AlertDialog.Builder approvalDialog = new AlertDialog.Builder(RequestNotificationActivity.this);
        approvalDialog.setTitle("Approve Request");
        approvalDialog.setMessage("Are you sure you wish to approve request?");

        approvalDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onListingClicked:  clicked");
                mNotifications.get(position);
                notificationIDInList = mNotifications.get(position).getNotificationID();
                Log.d(TAG, "THE LISTING ID: " + notificationID);
                decision = approveDecision;

                String title = mNotifications.get(position).getItemName();
                String imageURL = mNotifications.get(position).getItemURL();
                String listingID = mNotifications.get(position).getListingID();
                String type = "Approved";
                String approvalUserID = userId;
                String UserRequestApprovedID = mNotifications.get(position).getSenderUserID();
                //String notificationID = mNotifications.get(position).getNotificationID();
                String listingState = "Approved";



                String notificationID = mDatabaseRef.push().getKey();
                myNotification = new Notification(title, imageURL, type, approvalUserID, UserRequestApprovedID, notificationID, listingState , fullName, listingID, pNumber);
                mDatabaseRef.child(notificationID).setValue(myNotification);

                //Update listing state
                mDatabaseRequestRef.child(notificationIDInList).child("listingState").setValue(listingState);

                mAdapter.notifyDataSetChanged();


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
                        Toast.makeText(RequestNotificationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                if(reuseNo2 <1) {
                    String reuseID = userReuseRef.push().getKey();
                    myUserReuseTotal = new UserReuseTotal(approvalUserID, 1, reuseID);
                    userReuseRef.child(reuseID).setValue(myUserReuseTotal);
                }else{
                    userReuseRef.child(theReuseID).child("reuseNumber").setValue(reuseNo2);
                }

                mAdapter.notifyDataSetChanged();
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
        AlertDialog.Builder declineDialog = new AlertDialog.Builder(RequestNotificationActivity.this);
        declineDialog.setTitle("Decline Request");
        declineDialog.setMessage("Are you sure you wish to decline request?");

        declineDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onListingClicked:  clicked");
                mNotifications.get(position);
                notificationIDInList = mNotifications.get(position).getNotificationID();
                Log.d(TAG, "THE LISTING ID: " + notificationID);

                String title = mNotifications.get(position).getItemName();
                String imageURL = mNotifications.get(position).getItemURL();
                String listingID = mNotifications.get(position).getListingID();
                String type = "Declined";
                String approvalUserID = userId;
                String UserRequestApprovedID = mNotifications.get(position).getSenderUserID();
                //String notificationID = mNotifications.get(position).getNotificationID();
                String listingState = "Declined";


                String notificationID = mDatabaseRef.push().getKey();
                myNotification = new Notification(title, imageURL, type, approvalUserID, UserRequestApprovedID, notificationID, listingState, fullName, listingID, pNumber);
                mDatabaseRef.child(notificationID).setValue(myNotification);

                //Update listing state
                mDatabaseRequestRef.child(notificationIDInList).child("listingState").setValue(listingState);
                //finish();
                //mNotifications.remove(position);
                //Toast.makeText(NotificationActivity.this, "Request Declined", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();

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