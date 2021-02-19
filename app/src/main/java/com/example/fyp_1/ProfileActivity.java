package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ProfileActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_profile);

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
                ProfileActivity.this.finish();
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
                        Toast.makeText(ProfileActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void update(View view) {
        if (isFNameChanged() || isLNameChanged() || isPhoneNumberChanged() || selectedImage != null) {
            uploadProfilePicture();
            Toast.makeText(ProfileActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfileActivity.this, homePageActivity.class);
            startActivity(intent);

        } else
            Toast.makeText(ProfileActivity.this, "Details are the same & Can not be updated!", Toast.LENGTH_LONG).show();
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

}