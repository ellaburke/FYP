package com.example.fyp_1.UserProfileAndListings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp_1.AllListingsTab.viewListingActivity;
import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.example.fyp_1.homePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    DatabaseReference updateRef;
    StorageReference storageRef;
    StorageReference profilerUpdateRef;
    private Uri selectedImage;
    private String userId;
    private FirebaseUser user;
    ImageView uploadProfileImage;
    ImageView firstNameEditImage, secondNameEditImage, phoneEditImage;
    EditText profileFName;
    EditText profileLName;
    EditText profilePhone;
    EditText profileEmail;
    Button goBackBtn2;
    Button updateBtn;
    String email;
    String fName, lName, pNumber;
    private static final String USER = "user";
    private static final String PP = "profilePictures";
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        Log.d("USERID", userId);
        updateRef = FirebaseDatabase.getInstance().getReference("user");
        storageRef = FirebaseStorage.getInstance().getReference();
        profilerUpdateRef = storageRef.child(user.getUid() + ".jpg");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USER);
        DatabaseReference profilePicRef = rootRef.child(PP);
        Log.v("USERID", userRef.getKey());


        uploadProfileImage = (ImageView) findViewById(R.id.profileImageView);
        firstNameEditImage = (ImageView) findViewById(R.id.firstNameEditImage);
        secondNameEditImage = (ImageView) findViewById(R.id.lastNameEditImage);
        phoneEditImage = (ImageView) findViewById(R.id.phoneEditImage);
        profileFName = (EditText) findViewById(R.id.etFirstName);
        profileLName = (EditText) findViewById(R.id.etLastName);
        profilePhone = (EditText) findViewById(R.id.etPhoneNumber);
        profileEmail = (EditText) findViewById(R.id.etEmail);
        updateBtn = (Button) findViewById(R.id.updateProfileButton);
        goBackBtn2 = (Button) findViewById(R.id.goBackBtn2);

        profileEmail.setEnabled(false);
        profileFName.setEnabled(false);
        profileLName.setEnabled(false);
        profilePhone.setEnabled(false);

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
                        Intent emptyIntent = new Intent(EditProfileActivity.this, viewListingActivity.class);
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

        profilerUpdateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(uploadProfileImage);
            }
        });

        firstNameEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFName.setEnabled(true);
            }
        });
        secondNameEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileLName.setEnabled(true);
            }
        });
        phoneEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePhone.setEnabled(true);
            }
        });


        goBackBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.this.finish();
            }
        });


        uploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        fName = keyId.child("firstName").getValue(String.class);
                        lName = keyId.child("lastName").getValue(String.class);
                        pNumber = keyId.child("phoneNumber").getValue(String.class);
                        break;
                    }
                }
                profileFName.setText(fName);
                profileLName.setText(lName);
                profilePhone.setText(pNumber);
                profileEmail.setText(email);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            selectedImage = data.getData();
            uploadProfileImage.setImageURI(selectedImage);

            uploadProfilePicture();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadProfilePicture() {
        StorageReference imgRef = storageRef.child(user.getUid() + ".jpg");

        imgRef.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditProfileActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void update(View view) {
        if (isFNameChanged() || isLNameChanged() || isPhoneNumberChanged() || selectedImage != null) {
            uploadProfilePicture();
            Toast.makeText(EditProfileActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditProfileActivity.this, homePageActivity.class);
            startActivity(intent);

        } else
            Toast.makeText(EditProfileActivity.this, "Details are the same & Can not be updated!", Toast.LENGTH_LONG).show();
    }

    private boolean isPhoneNumberChanged() {
        if (!pNumber.equals(profilePhone.getText().toString())) {

            updateRef.child(userId).child("phoneNumber").setValue(profilePhone.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isLNameChanged() {
        if (!lName.equals(profileLName.getText().toString())) {

            updateRef.child(userId).child("lastName").setValue(profileLName.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isFNameChanged() {
        if (!fName.equals(profileFName.getText().toString())) {

            updateRef.child(userId).child("firstName").setValue(profileFName.getText().toString());
            return true;
        } else {
            return false;
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