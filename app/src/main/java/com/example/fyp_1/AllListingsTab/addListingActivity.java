package com.example.fyp_1.AllListingsTab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fyp_1.Maps.MapsActivity;
import com.example.fyp_1.R;
import com.example.fyp_1.homePageActivity;
import com.example.fyp_1.model.Listing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class addListingActivity extends AppCompatActivity{

    private static final String TAG = "AddListingActivity";

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_LOCATION = 2;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    Listing myListing;
    //String listingID;
    private Uri selectedImage;
    private String userId;
    private FirebaseUser user;
    ImageView uploadImage;
    Spinner keepListedForSpinner;
    Spinner categorySpinner;
    EditText expiryDate;
    Button offeringButton;
    Button wantButton;
    Button doneButton;
    EditText listingTitle;
    EditText listingDescription;
    EditText listingLocation;
    EditText listingPickUpTimes;
    String optionSelected;
    String listingTitleEntered;
    String listingDescriptionEntered;
    String listingLocationEntered;
    String listingPickUpTimesEntered;
    String listingExpiryDateEntered;
    String listingKeepListedFor;
    String listingOption;
    String listingFoodCategory;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        //listingID = pushedListingRef.getKey();
        mStorageRef = FirebaseStorage.getInstance().getReference("listings");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

//        /*---------------------Hooks------------------------*/
//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        toolbar = findViewById(R.id.toolbar);
//
//        /*---------------------Tool Bar------------------------*/
//        setSupportActionBar(toolbar);
//
//        /*---------------------Navigation Drawer Menu------------------------*/
//        //navigationView.bringToFront();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(addListingActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);

        keepListedForSpinner = (Spinner) findViewById(R.id.keep_listed_for_spinner);
        categorySpinner = (Spinner) findViewById(R.id.food_category_spinner);
        expiryDate = (EditText) findViewById(R.id.expiryDate_et);
        uploadImage = (ImageView) findViewById(R.id.uploadImageView);
        offeringButton = (Button) findViewById(R.id.offering_button);
        wantButton = (Button) findViewById(R.id.want_button);
        doneButton = (Button) findViewById(R.id.doneButton);
        listingDescription = (EditText) findViewById(R.id.description_et);
        listingLocation = (EditText) findViewById(R.id.location_et);
        listingTitle = (EditText) findViewById(R.id.title_et);
        listingPickUpTimes = (EditText) findViewById(R.id.pickup_times_et);


        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.keep_listed_for_array,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(this, R.array.food_category_array,
                android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        keepListedForSpinner.setAdapter(staticAdapter);
        categorySpinner.setAdapter(staticAdapter2);

        listingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getLocationIntent = new Intent(addListingActivity.this, MapsActivity.class);
                startActivityForResult(getLocationIntent, RESULT_LOAD_LOCATION);
            }
        });

        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        addListingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                expiryDate.setText(date);
            }
        };

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        offeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wantButton.setTextColor(Color.GRAY);
                offeringButton.setTextColor(Color.parseColor("#EA3975"));
                wantButton.setSelected(false);
                optionSelected = offeringButton.getText().toString();


            }
        });

        wantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offeringButton.setTextColor(Color.GRAY);
                wantButton.setTextColor(Color.parseColor("#EA3975"));
                offeringButton.setSelected(false);
                optionSelected = wantButton.getText().toString();

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                Intent listingViewIntent = new Intent(addListingActivity.this, homePageActivity.class);
                startActivity(listingViewIntent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RESULT_LOAD_IMAGE: {
                    selectedImage = data.getData();
                    Picasso.get().load(selectedImage).into(uploadImage);
                }break;
                case RESULT_LOAD_LOCATION:{
                    String strEditText = data.getStringExtra("submit_address");
                    listingLocation.setText(strEditText);
                }break;
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        listingTitleEntered = listingTitle.getText().toString();
        listingDescriptionEntered = listingDescription.getText().toString();
        listingLocationEntered = listingLocation.getText().toString();
        listingPickUpTimesEntered = listingPickUpTimes.getText().toString();
        listingExpiryDateEntered = expiryDate.getText().toString();
        listingKeepListedFor = keepListedForSpinner.getSelectedItem().toString();
        listingFoodCategory = categorySpinner.getSelectedItem().toString();
        listingOption = optionSelected;

        if (selectedImage != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(selectedImage));

            fileReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(addListingActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
//                    myListing = new Listing(listingTitleEntered, listingDescriptionEntered, listingExpiryDateEntered, listingLocationEntered, listingPickUpTimesEntered, listingKeepListedFor,
//                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(), listingOption, userId);
//                    String uploadId = mDatabaseRef.push().getKey();
//                    mDatabaseRef.child(uploadId).setValue(myListing);
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    String listingID = mDatabaseRef.push().getKey();

                    //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                    myListing = new Listing(listingID,listingTitleEntered, listingDescriptionEntered,listingFoodCategory, listingExpiryDateEntered, listingLocationEntered, listingPickUpTimesEntered, listingKeepListedFor,
                             downloadUrl.toString(),listingOption, userId);

                    //String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(listingID).setValue(myListing);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addListingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//        switch (menuItem.getItemId()) {
//            case R.id.nav_home:
//                Intent intent0 = new Intent(addListingActivity.this, homePageActivity.class);
//                startActivity(intent0);
//                break;
//            case R.id.nav_location:
//                Intent intent = new Intent(addListingActivity.this, MapsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.nav_view_listing:
//                Intent intent2 = new Intent(addListingActivity.this, viewListingActivity.class);
//                startActivity(intent2);
//                break;
//            case R.id.nav_upload_listing:
//                Intent intent3 = new Intent(addListingActivity.this, addListingActivity.class);
//                startActivity(intent3);
//                break;
//            case R.id.nav_profile:
//                Intent intent4 = new Intent(addListingActivity.this, ProfileActivity.class);
//                intent4.putExtra("email", user.getEmail());
//                startActivity(intent4);
//                break;
//            case R.id.nav_view_my_listing:
//                Intent intent5 = new Intent(addListingActivity.this, MyListings.class);
//                startActivity(intent5);
//                break;
//            case R.id.user_rating:
//                Intent intent6 = new Intent(addListingActivity.this, UserRating.class);
//                startActivity(intent6);
//                break;
////            case R.id.nav_scan:
////                Intent intent7 = new Intent(addListingActivity.this, ScanActivity.class);
////                startActivity(intent7);
////                break;
//
//        }
//        //drawerLayout.closeDrawer(Gravity.START);
//        //drawerLayout.closeDrawer(GravityCompat.START);
//        //drawerLayout.closeDrawers();
//        return true;
//    }
//
//    public void onBackPressed() {
//
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
}