package com.example.fyp_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fyp_1.model.BarcodeItem;
import com.example.fyp_1.model.Recipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    private static final String TAG = "BarcodeActivity";
    String barcode;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        Intent i = getIntent();
        barcode = getIntent().getStringExtra("barcode");
        Log.d(TAG, "SCANNED BARCODE: " + barcode);

        mTextViewResult = (TextView) findViewById(R.id.displayBarcodeProduct);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.spoonacular.com/food/products/upc/" + barcode + "?apiKey=f6d9e1e1d7e340208e01b093f6455646")
                .get()
                .addHeader("x-rapidapi-key", "e716136075msh69b9329992873dbp1c8e3djsn254931884571")
                .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .build();

        //https://api.spoonacular.com/food/products/upc/{upc}

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();


                    BarcodeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                parseBarcodeResponse(myResponse);

                            //mTextViewResult.setText(myResponse);
                        }
                    });
                }
            }
        });

    }

    /**
     * Parse the Recipe response.
     * <code>response</code> contains {"key": "value", ..} .. a String
     * @param response
     */
    private void parseBarcodeResponse(String response) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//             int[] myArray;
//            BarcodeItem[] barcode1 = mapper.readValue(response, BarcodeItem[].class);
//
//            for (BarcodeItem bc : barcode1) {
//                System.out.println("FOOD ID" + bc.getId());
//                System.out.println("FOOD TITLE" + bc.getTitle());
//
//            }
        ObjectMapper mapper = new ObjectMapper();
        try {
            BarcodeItem barcode1 = mapper.readValue(response,BarcodeItem.class);
            String barcodeName = barcode1.getTitle();
            System.out.println("FOOD TITLE " + barcodeName);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

//    private void jsonParse(String myResponse) throws JSONException {
//        JSONObject jsonObject = new JSONObject(myResponse);
//        //JSONArray jArray = jsonArray.getJSONArray("ARRAYNAME");
//        for (int i=0; i < jsonObject.length(); i++)
//        {
//            try {
//                JSONObject oneObject = jsonObject.getJSONObject(String.valueOf(i));
//                // Pulling items from the array
//                String oneObjectsItem = oneObject.getString("title");
//                System.out.println("WOOOOO" + oneObjectsItem);
//                mTextViewResult.setText(oneObjectsItem);
//            } catch (JSONException e) {
//                // Oops
//            }
//        }
//    }

}