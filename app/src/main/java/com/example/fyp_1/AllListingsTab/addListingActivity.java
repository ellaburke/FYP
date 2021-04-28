package com.example.fyp_1.AllListingsTab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.HomePage.homePageActivity;
import com.example.fyp_1.model.Listing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private Uri selectedImage;
    private String userId;
    private FirebaseUser user;
    ImageView uploadImage;
    Spinner categorySpinner;
    EditText expiryDate;
    Button doneButton;
    EditText listingTitle;
    EditText listingDescription;
    EditText listingLocation;
    EditText listingPickUpTimes;
    String listingTitleEntered;
    String listingDescriptionEntered;
    String listingLocationEntered;
    String listingPickUpTimesEntered;
    String listingExpiryDateEntered;
    String listingFoodCategory;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        //listingID = pushedListingRef.getKey();
        mStorageRef = FirebaseStorage.getInstance().getReference("listings");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listings");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        categorySpinner = (Spinner) findViewById(R.id.food_category_spinner);
        expiryDate = (EditText) findViewById(R.id.expiryDate_et);
        uploadImage = (ImageView) findViewById(R.id.uploadImageView);
        doneButton = (Button) findViewById(R.id.doneButton);
        listingDescription = (EditText) findViewById(R.id.description_et);
        listingLocation = (EditText) findViewById(R.id.location_et);
        listingTitle = (EditText) findViewById(R.id.title_et);
        listingPickUpTimes = (EditText) findViewById(R.id.pickup_times_et);

        //Init btm nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.SearchListingNav);

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
                        Intent emptyIntent = new Intent(addListingActivity.this, viewListingActivity.class);
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


        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(this, R.array.food_category_array,
                android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
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
        listingFoodCategory = categorySpinner.getSelectedItem().toString();
        int requestTotal = 0;

        if (selectedImage != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(selectedImage));

            fileReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(addListingActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    String listingID = mDatabaseRef.push().getKey();

                    myListing = new Listing(listingID,listingTitleEntered, listingDescriptionEntered,listingFoodCategory, listingExpiryDateEntered, listingLocationEntered, listingPickUpTimesEntered,
                             downloadUrl.toString(), userId, requestTotal);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_back_icon) {
            finish();
            return true;
        }

        return true;
    }

}
