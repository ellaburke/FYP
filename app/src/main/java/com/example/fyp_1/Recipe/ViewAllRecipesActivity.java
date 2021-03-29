package com.example.fyp_1.Recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewAllRecipesActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeListener{

    private static final int RESULT_LOAD_IMAGE = 1;
    DatabaseReference updateRef;
    StorageReference storageRef;
    private Uri selectedImage;
    private String userId;
    private FirebaseUser user;
    String email;
    private static final String USER = "user";
    private static final String PP = "profilePictures";
    private static final String TAG = "RecipesActivity";
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    int recipeId;
    List<Recipe> mRecipes = new ArrayList<>();
    //StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_recipes);

        Intent intent = getIntent();

        Recipe[] recipes = (Recipe[]) intent.getSerializableExtra("recipes");

        mRecyclerView = findViewById(R.id.recipeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        for (Recipe recipe : recipes) {
            mRecipes.add(recipe);
        }
        mAdapter = new RecipesAdapter(ViewAllRecipesActivity.this, (ArrayList<Recipe>) mRecipes, this);
        mRecyclerView.setAdapter(mAdapter);


        email = intent.getStringExtra("email");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        Log.d("USERID", userId);
        updateRef = FirebaseDatabase.getInstance().getReference("user");
        storageRef = FirebaseStorage.getInstance().getReference();

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
            Intent intent = new Intent(ViewAllRecipesActivity.this, MyKitchenIngredients2.class);
            startActivity(intent);
            return true;
        }

        return true;
    }


    @Override
    public void onRecipeClick(int position) {
        Log.d(TAG, "onRecipeClicked:  clicked");
        recipeId = mRecipes.get(position).getId();
        String recipeName = mRecipes.get(position).getTitle();
        String recipeImageUrl = mRecipes.get(position).getImage();
        Log.d(TAG, "THE RECIPE ID: " + recipeId);
        Intent viewRecipeIntent = new Intent(this, ViewFullRecipeActivity.class);
        //viewRecipeIntent.putExtra("the_recipe_id", (Parcelable) mRecipes.get(position));
        viewRecipeIntent.putExtra("the_recipe_id", recipeId);
        viewRecipeIntent.putExtra("the_recipe_name", recipeName);
        viewRecipeIntent.putExtra("the_recipe_image_url", recipeImageUrl);
        startActivity(viewRecipeIntent);
    }




}