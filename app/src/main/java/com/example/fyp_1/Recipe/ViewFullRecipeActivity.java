package com.example.fyp_1.Recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp_1.FullRecipeDisplayFragments.IngredientListFragment;
import com.example.fyp_1.FullRecipeDisplayFragments.RecipeFragmentAdapter;
import com.example.fyp_1.MyKitchenItem;
import com.example.fyp_1.R;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.example.fyp_1.model.Listing;
import com.example.fyp_1.model.RecipeInstructionStep;
import com.example.fyp_1.model.RecipeInstructionStepEquipment;
import com.example.fyp_1.model.RecipeInstructionStepIngredient;
import com.example.fyp_1.model.RecipeInstructions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewFullRecipeActivity extends AppCompatActivity {

    private static final String TAG = "ViewFullRecipe";
    private static final String KITCHENITEM = "myKitchenItems";
    int recipeToDisplay;
    String myIngList = "";
    String recipeNameToDisplay, recipeImageURLToDisplay;
    ImageView recipeImageView;
    TextView recipeNameTV, recipeIngredientTV, recipeIngredientListTV, recipeIngredientListTVListed, recipeIngredientListTVShop, ingredientEquipmentTV, ingredientEquipmentListTV, ingredientMethodTV, ingredientMethodListTV;

    //RCV compnents
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //Firebase Ref
    DatabaseReference rootRef;
    DatabaseReference kitchenItemRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_recipe);
        recipeNameTV = (TextView) findViewById(R.id.displayFullRecipeNameTV);
        recipeImageView = (ImageView) findViewById(R.id.dislayFullRecipeIV);
        recipeIngredientTV = (TextView) findViewById(R.id.displayFullRecipeIngredientsTV);
        recipeIngredientTV.setPaintFlags(recipeIngredientTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recipeIngredientListTV = (TextView) findViewById(R.id.displayFullRecipeIngredientsListThatIOwnTV);
        recipeIngredientListTVListed = (TextView) findViewById(R.id.displayFullRecipeIngredientsListThatIDontOwnListedTV);
        recipeIngredientListTVShop = (TextView) findViewById(R.id.displayFullRecipeIngredientsListShopForTV);
        ingredientEquipmentTV = (TextView) findViewById(R.id.displayFullRecipeEquipmentTV);
        ingredientEquipmentTV.setPaintFlags(ingredientEquipmentTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ingredientEquipmentListTV = (TextView) findViewById(R.id.displayFullRecipeEquipmentListTV);
        ingredientMethodTV = (TextView) findViewById(R.id.displayFullRecipeMethodTV);
        ingredientMethodTV.setPaintFlags(ingredientMethodTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //ingredientMethodListTV = (TextView) findViewById(R.id.displayFullRecipeMethodStepsTV);


//        //Tab Layout & ViewPager2
//        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
//        viewPager2.setAdapter(new RecipeFragmentAdapter(this));
//
//        //Fragments
//        IngredientListFragment ingredientListFragment = new IngredientListFragment();

        //Firebase
        //Ref to kitchen item
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        kitchenItemRef = rootRef.child(KITCHENITEM);


        //View ingredientTabView = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                switch (position) {
//                    case 0: {
//                        tab.setText("Ingredients");
//                        tab.setIcon(R.drawable.ingredients_icon);
//                        break;
//                    }
//                    case 1: {
//                        tab.setText("Utensils");
//                        tab.setIcon(R.drawable.utensil_icon);
//                        break;
//                    }
//                    case 2: {
//                        tab.setText("Method");
//                        tab.setIcon(R.drawable.ic_baseline_format_list_bulleted_24);
//                        break;
//                    }
//                }
//            }
//        });
//        tabLayoutMediator.attach();


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

        //String recipe = recipeNameToDisplay + System.getProperty("line.separator");
        String recipe = "";
        String equipmentListDisplay = "";
        String ingredientsListDisplay = "";

        final List<RecipeInstructionStepIngredient>[] ingredientList = new List[]{new ArrayList<>()};
        List<RecipeInstructionStepEquipment> equipmentList = new ArrayList<>();
        ArrayList<RecipeInstructionStep> stepsList = new ArrayList<>();
        ArrayList<String> doHave = new ArrayList<>();
        ArrayList<String> dontHave = new ArrayList<>();

        ArrayList<String> availableListings = new ArrayList<>();
        ArrayList<String> notAvailableAsListing = new ArrayList<>();

        // Loop over each recipe (expecting only 1)
        for (RecipeInstructions recipeInstruction : recipeInstructions) {

            // Looping over each step object from the recipe response
            for (RecipeInstructionStep step : recipeInstruction.getSteps()) {

                // All recipe properties are available
                recipe += step.number + ":" + step.step + System.getProperty("line.separator");
                stepsList.add(step);
                int i = 0;

                //for (RecipeInstructionStepIngredient ingredient : step.ingredients) {
                RecipeInstructionStepIngredient[] ingredientList2;
                ingredientList2 = step.ingredients;
                for (RecipeInstructionStepIngredient ing2 : ingredientList2) {
                    ingredientList[0].add(ing2);
                }

                for (RecipeInstructionStepEquipment equipment : step.equipment) {
                    if (!equipmentList.contains(equipment)) {
                        equipmentList.add(equipment);
                    }

                }

            }
        }

        //(1)sort list & remote duplicates
        equipmentList = sortEquipmentList(equipmentList);
        //(2) iterate through sorted no duplicate list and then add to UI
        for (RecipeInstructionStepEquipment equipment : equipmentList) {
            equipmentListDisplay += equipment.name + System.getProperty("line.separator");
        }

        List<String> itemsByName = new ArrayList<>();
        //(3)sort list & remote duplicates
        ingredientList[0] = sortIngredientList(ingredientList[0]);
        //(4) iterate through sorted no duplicate list and then add to UI
        for (RecipeInstructionStepIngredient ing : ingredientList[0]) {
            ingredientsListDisplay += ing.name + System.getProperty("line.separator");
            itemsByName.add(ing.name);
        }


        FirebaseDatabase.getInstance().getReference().child("myKitchenItems")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ingredientsListDisplay2 = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MyKitchenItem mKI = snapshot.getValue(MyKitchenItem.class);
                            if (itemsByName.contains(mKI.getItemName().toLowerCase())) {
                                doHave.add(mKI.getItemName().toLowerCase());
                                ingredientsListDisplay2 += mKI.getItemName() + System.getProperty("line.separator");

                            } else {
                                System.out.println("3. NOT IN LIST: " + mKI.getItemName());
                            }
                            recipeIngredientListTV.setText(ingredientsListDisplay2);
                            itemsByName.removeAll(doHave);
                            System.out.println("REMOVED LIST 2: " + itemsByName);

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

        FirebaseDatabase.getInstance().getReference().child("listings")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ingredientsThatAreListedDisplay = "";
                        String ingredientsThatAreNotListedDisplay = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Listing availableListing = snapshot.getValue(Listing.class);
                            if (itemsByName.contains(availableListing.getName().toLowerCase())) {
                                availableListings.add(availableListing.getName().toLowerCase());
                                ingredientsThatAreListedDisplay += availableListing.getName() + System.getProperty("line.separator");


                            } else {
                                //System.out.println("3. NOT IN LIST: " + availableListing.getName());
                            }
                            recipeIngredientListTVListed.setText(ingredientsThatAreListedDisplay);
                            itemsByName.remove(availableListings);
                            System.out.println("REMOVED LIST 2: " + itemsByName);

                        }
                        for(String ingToShop: itemsByName) {
                            ingredientsThatAreNotListedDisplay += ingToShop + System.getProperty("line.separator");
                        }
                        recipeIngredientListTVShop.setText(ingredientsThatAreNotListedDisplay);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

//        String ingredientsThatAreNotListedDisplay = "";
//        for(String ingToShop: itemsByName) {
//            ingredientsThatAreNotListedDisplay += ingToShop + System.getProperty("line.separator");
//        }
//        recipeIngredientListTVShop.setText(ingredientsThatAreNotListedDisplay);


        // String ingredientsThatAreListedDisplay = "";
        //ingredientsThatAreListedDisplay += mKI.getItemName() + System.getProperty("line.separator");
        //recipeIngredientListTVListed.setText(ingredientsThatAreListedDisplay);


//
//        List <String> newList= doHave;
//        for(String ingIHave: newList){
//            ingredientsListDisplay2 += ingIHave + System.getProperty("line.separator");
//            System.out.println("4. : " + ingredientsListDisplay2);
//        }
        //Set UI
        recipeNameTV.setText(recipeNameToDisplay);
        //recipeIngredientListTV.setText(ingredientsListDisplay2);
        ingredientEquipmentListTV.setText(equipmentListDisplay);
        Picasso.get()
                .load(recipeImageURLToDisplay)
                .into(recipeImageView);

        mRecyclerView = findViewById(R.id.recipeDisplayRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DisplayRecipeStepAdapter(ViewFullRecipeActivity.this, stepsList);
        mRecyclerView.setAdapter(mAdapter);
    }


    public List<RecipeInstructionStepEquipment> sortEquipmentList
            (List<RecipeInstructionStepEquipment> eListOriginal) {
        List<RecipeInstructionStepEquipment> duplicates = new ArrayList<RecipeInstructionStepEquipment>();
        Collections.sort(eListOriginal, new Comparator<RecipeInstructionStepEquipment>() {
            @Override
            public int compare(RecipeInstructionStepEquipment e1, RecipeInstructionStepEquipment e2) {
                return e1.name.compareTo(e2.name);
            }
        });

        for (int i = 1; i < eListOriginal.size(); i++) {
            if (eListOriginal.get(i - 1).id.equals(eListOriginal.get(i).id)) {
                duplicates.add(eListOriginal.get(i));
                eListOriginal.remove(i);
                i = i - 1;
            }
        }
        return eListOriginal;
    }

    public List<RecipeInstructionStepIngredient> sortIngredientList
            (List<RecipeInstructionStepIngredient> eListOriginal) {
        List<RecipeInstructionStepIngredient> duplicates = new ArrayList<RecipeInstructionStepIngredient>();
        Collections.sort(eListOriginal, new Comparator<RecipeInstructionStepIngredient>() {
            @Override
            public int compare(RecipeInstructionStepIngredient i1, RecipeInstructionStepIngredient i2) {
                return i1.name.compareTo(i2.name);
            }
        });

        for (int i = 1; i < eListOriginal.size(); i++) {
            if (eListOriginal.get(i - 1).id.equals(eListOriginal.get(i).id)) {
                duplicates.add(eListOriginal.get(i));
                eListOriginal.remove(i);
                i = i - 1;
            }
        }
        return eListOriginal;
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