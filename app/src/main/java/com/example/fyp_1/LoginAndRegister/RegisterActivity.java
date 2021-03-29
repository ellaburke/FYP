package com.example.fyp_1.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fyp_1.R;
import com.example.fyp_1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    //Create password pattern
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USER = "user";
    private static final String TAG = "RegisterActivity";
    private User user;
    EditText firstNameET;
    EditText lastNameET;
    EditText emailET;
    EditText phoneNumberET;
    EditText passwordET;
    EditText confirmPasswordET;
    Button registerButton;
    Button backToHomeButton;

    //String values
    String email;
    String password;
    String password2;
    String firstName;
    String lastName;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        firstNameET = (EditText) findViewById(R.id.firstNameET);
        lastNameET = (EditText) findViewById(R.id.lastNameET);
        emailET = (EditText) findViewById(R.id.emailET);
        phoneNumberET = (EditText) findViewById(R.id.phoneET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        confirmPasswordET = (EditText) findViewById(R.id.confirmPasswordET);
        registerButton = (Button) findViewById(R.id.registerButton);
        backToHomeButton = (Button) findViewById(R.id.homePageButton);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword() | !validateTextFields()) {
                    return;
                }


                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                password2 = confirmPasswordET.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_LONG).show();
                    return;
                }
                firstName = firstNameET.getText().toString();
                lastName = lastNameET.getText().toString();
                phoneNumber = phoneNumberET.getText().toString();


                user = new User(email, password, firstName, lastName, phoneNumber);
                registerUser(email, password);
            }
        });
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            updateUI(user, userId);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser, String userId) {
        //String keyId = mDatabase.push().getKey();
        mDatabase.child(userId).setValue(user);
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private boolean validateEmail() {
        email = emailET.getText().toString().trim();

        if (email.isEmpty()) {
            emailET.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please enter a valid email address");
            return false;

        } else {
            emailET.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {
        password = passwordET.getText().toString().trim();

        if (password.isEmpty()) {
            passwordET.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordET.setError("Password too weak");
            return false;
//        } else if (!password.equals(password2)) {
//            passwordET.setError("Passwords don't match");
//            confirmPasswordET.setError("Passwords don't match");
//            return false;
        } else {
            passwordET.setError(null);
            return true;
        }
    }

    private boolean validateTextFields() {
        firstName = firstNameET.getText().toString().trim();
        lastName = lastNameET.getText().toString().trim();
        phoneNumber = phoneNumberET.getText().toString().trim();

        if (firstName.isEmpty()) {
            firstNameET.setError("Field can't be empty");
            return false;
        } else if (lastName.isEmpty()) {
            lastNameET.setError("Field can't be empty");
            return false;
        } else if (phoneNumber.isEmpty()) {
            phoneNumberET.setError("Field can't be empty");
            return false;
        }else {
            firstNameET.setError(null);
            lastNameET.setError(null);
            phoneNumberET.setError(null);
            return true;
        }
    }
}
