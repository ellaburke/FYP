package com.example.fyp_1.UserProfileAndListings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.fyp_1.Maps.MapsActivity;
import com.example.fyp_1.R;
import com.example.fyp_1.model.Listing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditMyListingActivity extends AppCompatActivity {

    //Image
    private static final int RESULT_LOAD_IMAGE = 1;
    //Location
    private static final int RESULT_LOAD_LOCATION = 2;
    //Activity TAG
    private static final String TAG = "EditMyListing";

    //Firebase Components
    private static final String LISTING = "listings";
    private String userId;
    private FirebaseUser user;
    DatabaseReference updateRef;
    DatabaseReference getListingRef;

    //Set value for listing being passed from intent
    String listingToEdit;
    Listing currentItem;

    //UI Components
    ImageView listingImage;
    Spinner listingCategory;
    EditText listingTitle, listingDescription, listingLocation, listingPickUpTimes, listingExpiryDate;
    Button updateDoneBtn;

    //Food categories
    String Category = "Category";
    String Veg = "Veg";
    String Fruit = "Fruit";
    String MeatOrPoultry = "Meat/Poultry";
    String Dairy = "Dairy";
    String Fish = "Fish";
    String Freezer = "Freezer";
    String BreadOrCereal = "Bread/Cereal";
    String Cupboard = "Cupboard";

    String optionSelected;
    String listingTitleEntered, listingDescriptionEntered, listingLocationEntered, listingPickUpTimesEntered, listingExpiryDateEntered,
            listingFoodCategory, listingImageUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_listing);

        //Init Firebase
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(LISTING);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        updateRef = FirebaseDatabase.getInstance().getReference("listings");

        //Get Intent from ViewMyFullListing
        Intent i = getIntent();
        listingToEdit = getIntent().getStringExtra("listing_to_edit");

        //Init UI
        listingTitle = (EditText) findViewById(R.id.Edit_title_et);
        listingDescription = (EditText) findViewById(R.id.Edit_description_et);
        listingLocation = (EditText) findViewById(R.id.Edit_location_et);
        listingPickUpTimes = (EditText) findViewById(R.id.Edit_pickup_times_et);
        listingExpiryDate = (EditText) findViewById(R.id.Edit_expiry_date_et);
        listingCategory = (Spinner) findViewById(R.id.Edit_food_category_spinner);
        listingImage = (ImageView) findViewById(R.id.EdituploadImageView);
        updateDoneBtn = (Button) findViewById(R.id.updateDoneButton);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.food_category_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        listingCategory.setAdapter(staticAdapter);

        listingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        listingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(EditMyListingActivity.this, MapsActivity.class);
                startActivityForResult(mapIntent, RESULT_LOAD_LOCATION);
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    currentItem = child.getValue(Listing.class);
                    Log.d(TAG, "LISTING TO UPDATE 3: " + currentItem.getListingId());
                    Log.d(TAG, "LISTING TO UPDATE 4: " + listingToEdit);
                    if (currentItem.getListingId().equals(listingToEdit)) {
                        listingTitleEntered = currentItem.getName();
                        listingDescriptionEntered = currentItem.getDescription();
                        listingLocationEntered = currentItem.getLocation();
                        listingPickUpTimesEntered = currentItem.getPickUpTime();
                        listingExpiryDateEntered = currentItem.getExpiryDate();
                        listingFoodCategory = currentItem.getCategory();
                        listingImageUpdated = currentItem.getListingImageURL();

                        listingTitle.setText(listingTitleEntered);
                        listingDescription.setText(listingDescriptionEntered);
                        listingLocation.setText(listingLocationEntered);
                        listingPickUpTimes.setText(listingPickUpTimesEntered);
                        listingExpiryDate.setText(listingExpiryDateEntered);

                        if (Category.equals(listingFoodCategory)) {
                            listingCategory.setSelection(0);
                        } else if (Dairy.equals(listingFoodCategory)) {
                            listingCategory.setSelection(1);
                        } else if (MeatOrPoultry.equals(listingFoodCategory)) {
                            listingCategory.setSelection(2);
                        } else if (Fish.equals(listingFoodCategory)) {
                            listingCategory.setSelection(3);
                        } else if (Cupboard.equals(listingFoodCategory)) {
                            listingCategory.setSelection(4);
                        } else if (BreadOrCereal.equals(listingFoodCategory)) {
                            listingCategory.setSelection(5);
                        } else if (Fruit.equals(listingFoodCategory)) {
                            listingCategory.setSelection(6);
                        } else if (Veg.equals(listingFoodCategory)) {
                            listingCategory.setSelection(7);
                        } else if (Freezer.equals(listingFoodCategory)) {
                            listingCategory.setSelection(8);
                        }


                        listingImage.setImageURI(Uri.parse(listingImageUpdated));
                        Picasso.get().load(listingImageUpdated).into(listingImage);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


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