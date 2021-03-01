package com.example.fyp_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fyp_1.model.Recipe;
import com.example.fyp_1.model.RecipeInstructions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ViewFullRecipeActivity extends AppCompatActivity {

    private static final String TAG = "ViewFullRecipe";
    int recipeToDisplay;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_recipe);
        mTextViewResult = (TextView) findViewById(R.id.displayFullRecipeTV);

        Intent i = getIntent();
        recipeToDisplay = getIntent().getIntExtra("the_recipe_id", 0);
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

                            parseRecipeResponse(myResponse);
                            //mTextViewResult.setText(myResponse);
                        }
                    });
                }
            }
        });

        
    }

    private void parseRecipeResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // int[] myArray;
            RecipeInstructions[] recipeInstructions = mapper.readValue(response, RecipeInstructions[].class);

            for (RecipeInstructions instructions : recipeInstructions) {
                System.out.println("1" + instructions.getId());
                System.out.println("2" + instructions.getName());
                System.out.println("3" + instructions.getEquipment());
                System.out.println("4" + instructions.getIngredients());
                System.out.println("5" + instructions.getNumber());
                System.out.println("6" + instructions.getStep());
                System.out.println("7" + instructions.getSteps());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void jsonParse(String myResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(myResponse);
        JSONArray stepsArray = jsonObject.getJSONArray("steps");

        for (int i=0; i < stepsArray.length(); i++)
        {
            try {
                JSONObject oneObject = stepsArray.getJSONObject(i);
                // Pulling items from the array
                String oneObjectsItem = oneObject.getString("step");
                mTextViewResult.setText(oneObjectsItem);
                Log.d(TAG, "STEP: " + oneObjectsItem);
            } catch (JSONException e) {
                // Oops
            }
        }
    }
    }

