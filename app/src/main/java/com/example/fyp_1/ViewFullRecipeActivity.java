package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.fyp_1.model.Recipe;
import com.example.fyp_1.model.RecipeInstructionStep;
import com.example.fyp_1.model.RecipeInstructionStepEquipment;
import com.example.fyp_1.model.RecipeInstructionStepIngredient;
import com.example.fyp_1.model.RecipeInstructions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewFullRecipeActivity extends AppCompatActivity {

    private static final String TAG = "ViewFullRecipe";
    int recipeToDisplay;
    String recipeNameToDisplay, recipeImageURLToDisplay;
    ImageView recipeImageView;
    TextView recipeNameTV, recipeIngredientTV, recipeIngredientListTV, ingredientEquipmentTV, ingredientEquipmentListTV, ingredientMethodTV, ingredientMethodListTV;

    //RCV compnents
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_recipe);
        recipeNameTV = (TextView) findViewById(R.id.displayFullRecipeNameTV);
        recipeImageView = (ImageView) findViewById(R.id.dislayFullRecipeIV);
        recipeIngredientTV = (TextView) findViewById(R.id.displayFullRecipeIngredientsTV);
        recipeIngredientListTV = (TextView) findViewById(R.id.displayFullRecipeIngredientsListTV);
        ingredientEquipmentTV = (TextView) findViewById(R.id.displayFullRecipeEquipmentTV);
        ingredientEquipmentListTV = (TextView) findViewById(R.id.displayFullRecipeEquipmentListTV);
        ingredientMethodTV = (TextView) findViewById(R.id.displayFullRecipeMethodTV);
        //ingredientMethodListTV = (TextView) findViewById(R.id.displayFullRecipeMethodStepsTV);


        //Tab Layout & ViewPager2
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new RecipeFragmentAdapter(this));

        //Fragments
        //IngredientListFragment ingredientListFragment = new IngredientListFragment();


        //View ingredientTabView = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:{
                        tab.setText("Ingredients");
                        tab.setIcon(R.drawable.ingredients_icon);
                        break;
                    }case 1:{
                        tab.setText("Utensils");
                        tab.setIcon(R.drawable.utensil_icon);
                        break;
                    }case 2:{
                        tab.setText("Method");
                        tab.setIcon(R.drawable.ic_baseline_format_list_bulleted_24);
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();


        Intent i = getIntent();
        recipeToDisplay = getIntent().getIntExtra("the_recipe_id", 0);
        recipeNameToDisplay = getIntent().getStringExtra("the_recipe_name");
        recipeImageURLToDisplay = getIntent().getStringExtra("the_recipe_image_url");
        Log.d(TAG, "RECIPE ID TO DISPLAY: " + recipeToDisplay);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.spoonacular.com/recipes/" + recipeToDisplay + "/analyzedInstructions?apiKey=f6d9e1e1d7e340208e01b093f6455646")
                .get()
                .addHeader("x-rapidapi-key", "e716136075msh69b9329992873dbp1c8e3djsn254931884571")
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();


                    ViewFullRecipeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            RecipeInstructions[] recipeInstructions = parseRecipeResponse(myResponse);
                            displayInstructionsInUi(recipeInstructions);

                            //mTextViewResult.setText(myResponse);
                        }
                    });
                }
            }
        });
    }

    /**
     * Parse specific Recipe details
     *
     * @param response Response from request.
     */
    private RecipeInstructions[] parseRecipeResponse(String response) {

        // Setup variables
        RecipeInstructions[] recipeInstructions = null;
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Map Recipe Instructions from JSON String into Java object.
            recipeInstructions = mapper.readValue(response, RecipeInstructions[].class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return recipeInstructions;
    }

    /**
     * Responsible for displaying the recipe instructions in activity.
     *
     * @param recipeInstructions The parsed instructions.
     */
    private void displayInstructionsInUi(RecipeInstructions[] recipeInstructions) {

        // TODO remove single string
        // and replace with dedicated UI components
        //String recipe = recipeNameToDisplay + System.getProperty("line.separator");
        String recipe = "";
        String equipmentListDisplay = "";
        String ingredientsListDisplay = "";

        List<RecipeInstructionStepIngredient> ingredientList = new ArrayList<>();
        List<RecipeInstructionStepEquipment> equipmentList = new ArrayList<>();
        ArrayList<RecipeInstructionStep> stepsList = new ArrayList<>();

        // Loop over each recipe (expecting only 1)
        for (RecipeInstructions recipeInstruction : recipeInstructions) {

            // Looping over each step object from the recipe response
            for (RecipeInstructionStep step : recipeInstruction.getSteps()) {

                // 1. Make Toast
                // Equipment for step
                // Ingredients for step

                // All recipe properties are available
                recipe += step.number + ":" + step.step + System.getProperty("line.separator");
                stepsList.add(step);

                for (RecipeInstructionStepIngredient ingredient : step.ingredients) {
                    ingredientList.add(ingredient);
                    ingredientsListDisplay += ingredient.name + System.getProperty("line.separator");

                }

                for (RecipeInstructionStepEquipment equipment : step.equipment) {
                    equipmentList.add(equipment);
                    equipmentListDisplay += equipment.name + System.getProperty("line.separator");

                }

            }

        }


        recipeNameTV.setText(recipeNameToDisplay);

        //Fragments
        IngredientListFragment ingredientListFragment = new IngredientListFragment();
        Bundle args = new Bundle();
        args.putString("argText", ingredientsListDisplay);
        ingredientListFragment.setArguments(args);

        recipeIngredientListTV.setText(ingredientsListDisplay);
        ingredientEquipmentListTV.setText(equipmentListDisplay);
        //ingredientMethodListTV.setText(recipe);
        Picasso.get()
                .load(recipeImageURLToDisplay)
                .into(recipeImageView);


        mRecyclerView = findViewById(R.id.recipeDisplayRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new DisplayRecipeAdapter(ViewFullRecipeActivity.this, stepsList);
        mRecyclerView.setAdapter(mAdapter);
    }
}