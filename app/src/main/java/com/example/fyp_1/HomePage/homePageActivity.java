package com.example.fyp_1.HomePage;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homePageActivity extends AppCompatActivity {
    
    //Variables
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent homepageIntent = new Intent(homePageActivity.this, MyKitchenIngredients2.class);
        startActivity(homepageIntent);

    }

}
