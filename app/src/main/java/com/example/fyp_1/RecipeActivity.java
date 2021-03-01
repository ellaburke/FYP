package com.example.fyp_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fyp_1.model.Recipe;
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

public class RecipeActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    //Ingredient Strings
    String ingredient1 = "apples";
    String ingredient2 = "flour";
    String ingredient3 = "sugar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mTextViewResult = (TextView) findViewById(R.id.display_recipe_tv);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.spoonacular.com/recipes/findByIngredients?apiKey=f6d9e1e1d7e340208e01b093f6455646&ingredients=cheese,+chicken,+pasta&number=100")
                .get()
                .addHeader("x-rapidapi-key", "e716136075msh69b9329992873dbp1c8e3djsn254931884571")
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .build();
//
//        Request request1 = new Request.Builder()
//                .url("https://api.spoonacular.com/recipes/findByIngredients?apiKey=f6d9e1e1d7e340208e01b093f6455646&ingredients=cheese")
//                .get()
//                .addHeader("x-rapidapi-key", "e716136075msh69b9329992873dbp1c8e3djsn254931884571")
//                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
//                .build();

//        Request request = new Request.Builder()
//                .url("https://api.spoonacular.com/recipes/complexSearch??apiKey=f6d9e1e1d7e340208e01b093f6455646&query=pasta&maxFat=25&number=2")
//                .get()
//                .addHeader("x-rapidapi-key", "e716136075msh69b9329992873dbp1c8e3djsn254931884571")
//                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
//                .build();

        //Spoonacular API Key - f6d9e1e1d7e340208e01b093f6455646
        // https://api.spoonacular.com/recipes/716429/information?apiKey=f6d9e1e1d7e340208e01b093f6455646&includeNutrition=true.
        //

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();


                    RecipeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mTextViewResult.setText(myResponse);
                            //jsonParse(myResponse);
                            // Parse the JSON response into an object.
                            // 1. org.json.JSONArray (JSON serializer/deserializer)
                            // 2. Jackson (JSON serializer/deserializer)

                            parseRecipeResponse(myResponse);
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
            Recipe[] recipes = mapper.readValue(response, Recipe[].class);

            for (Recipe recipe : recipes) {
                System.out.println(recipe.getId());
                System.out.println(recipe.getTitle());
                System.out.println(recipe.getImage());
                System.out.println(recipe.getMissedIngredientCount());
            }

            Intent intent4 = new Intent(RecipeActivity.this, ViewAllRecipesActivity.class);
            intent4.putExtra("recipes", recipes);
            startActivity(intent4);

            //bundle.putString("");
            //bundle.putString(recipes);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void jsonParse(String myResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(myResponse);
        //JSONArray jArray = jsonArray.getJSONArray("ARRAYNAME");
        for (int i=0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject oneObject = jsonArray.getJSONObject(i);
                // Pulling items from the array
                String oneObjectsItem = oneObject.getString("title");
                //String oneObjectsItem2 = oneObject.getString("name");
                String oneObjectsItem2 = oneObject.getString("image");
                //mTextViewResult.setText(oneObjectsItem);
            } catch (JSONException e) {
                // Oops
            }
        }
    }

}

