package com.example.fyp_1.BarcodeScan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.MyKitchenItem;
import com.example.fyp_1.R;
import com.example.fyp_1.model.BarcodeItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    private static final String TAG = "BarcodeActivity";
    String barcode;
    private TextView mTextViewResult;

    //Barcode product name
    String barcodeName;

    //Firebase Components
    private FirebaseUser user;
    DatabaseReference mDatabaseRef;
    MyKitchenItem myKitchenItem;
    private String userId;
    String itemId;

    //UI Components
    Button wrongBtn;
    Button correctButton;
    Spinner itemAmountSpinner;
    Spinner categorySpinner;
    EditText barcodeProductAmount;
    String myItemInput, myItemAmountInput, myItemCategory, myItemMeasurement, itemAmountAndMeasurement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        Intent i = getIntent();
        barcode = getIntent().getStringExtra("barcode");
        Log.d(TAG, "SCANNED BARCODE: " + barcode);

        //Init UI Components
        mTextViewResult = (TextView) findViewById(R.id.displayBarcodeProduct);
        wrongBtn = (Button) findViewById(R.id.wrongBarcodeBtn);
        correctButton = (Button) findViewById(R.id.correctBarcodeBtn);
        itemAmountSpinner = (Spinner) findViewById(R.id.barcodeitem_amount_spinner);
        categorySpinner = (Spinner) findViewById(R.id.barcodefood_category_spinner2);
        barcodeProductAmount = (EditText) findViewById(R.id.barcodeitem_amount_et);

        //Firebase Components
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("myKitchenItems");

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(BarcodeActivity.this, R.array.item_amount_array,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(BarcodeActivity.this, R.array.food_category_array,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        itemAmountSpinner.setAdapter(staticAdapter);
        categorySpinner.setAdapter(staticAdapter2);

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
                            try {
                                parseBarcodeResponse(myResponse);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }

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
     *
     * @param response
     */
    private void parseBarcodeResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            BarcodeItem barcode1 = mapper.readValue(response, BarcodeItem.class);
            barcodeName = barcode1.getTitle();
            System.out.println("FOOD TITLE " + barcodeName);
            mTextViewResult.setText(barcodeName);

            wrongBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent backToKitchenIngIntent = new Intent(BarcodeActivity.this, MyKitchenIngredients2.class);
                    startActivity(backToKitchenIngIntent);
                }
            });

            correctButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myItemInput = barcodeName;
                    System.out.println("FOODITEM" + myItemInput);

                    myItemCategory = categorySpinner.getSelectedItem().toString();
                    System.out.println("FOODITEM" + myItemCategory);

                    myItemMeasurement = itemAmountSpinner.getSelectedItem().toString();
                    System.out.println("FOODITEM" + myItemMeasurement);

                    myItemAmountInput = barcodeProductAmount.getText().toString();
                    System.out.println("FOODITEM" + myItemAmountInput);

                    itemAmountAndMeasurement = myItemAmountInput + myItemMeasurement;
                    System.out.println("FOODITEM" + itemAmountAndMeasurement);

                    myKitchenItem = new MyKitchenItem(myItemInput, myItemCategory, itemAmountAndMeasurement, userId, itemId);
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("myKitchenItems");
                    itemId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(itemId).setValue(myKitchenItem);
                    Intent backToKitchenIngIntent = new Intent(BarcodeActivity.this, MyKitchenIngredients2.class);
                    startActivity(backToKitchenIngIntent);


                }
            });


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}