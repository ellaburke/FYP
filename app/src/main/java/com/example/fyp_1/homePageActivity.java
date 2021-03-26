package com.example.fyp_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        /*---------------------Hooks------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*---------------------Tool Bar------------------------*/
        setSupportActionBar(toolbar);

        /*---------------------Navigation Drawer Menu------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(homePageActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_location:
                Intent intent = new Intent(homePageActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_view_listing:
                Intent intent2 = new Intent(homePageActivity.this, viewListingActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_upload_listing:
                Intent intent3 = new Intent(homePageActivity.this, addListingActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(homePageActivity.this, MyListingsProfileActivity.class);
                intent4.putExtra("email", user.getEmail());
                startActivity(intent4);
                break;
            case R.id.user_rating:
                Intent intent6 = new Intent(homePageActivity.this, UserRating.class);
                startActivity(intent6);
                break;
//            case R.id.nav_scan:
//                Intent intent7 = new Intent(homePageActivity.this, TextDetectActivity.class);
//                startActivity(intent7);
//                break;
            case R.id.nav_my_kitchen:
                Intent intent8 = new Intent(homePageActivity.this, MyKitchenIngredients2.class);
                intent8.putExtra("email", user.getEmail());
                startActivity(intent8);
                break;
            case R.id.nav_shopping_list:
                Intent intent10 = new Intent(homePageActivity.this, MyShoppingListActivity.class);
                startActivity(intent10);
                break;

        }

        return true;
    }

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
