package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MyKitchenIngredients2 extends AppCompatActivity {

    private static final String TAG = "MyKitchenIngredients";
    RecyclerView mainRecyclerView;

    private FirebaseUser user;
    DatabaseReference mDatabaseRef;
    MyKitchenItem myKitchenItem;
    private String userId;
    String itemId;
    String ingredientId;
    Dialog popupTipDialog;
    Button closePopupTipDialog;
    ArrayList<MyKitchenItem> selectedList;
    //MyKitchenIngredientsChildAdapter myKitchenIngredientsChildAdapter;

    //   Barcode
    String barcode;
    String barcodeResult;

    //FAB Animations
    Animation animatorRotateOpen;
    Animation animatorRotateClose;
    Animation animatorFromBottom;
    Animation animatorToBottom;

    Boolean clicked = true;
    String myItemInput, myItemAmountInput, myItemCategory, myItemMeasurement, itemAmountAndMeasurement;

    FloatingActionButton addToKitchenBtn, addToKitchenByScanBtn, addToKitchenByTextBtn, checkedItemsBtn;


    ArrayList<FoodCategorySection> foodCategorySectionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kitchen_ingredients2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("myKitchenItems");

        //Popup Dialog
        popupTipDialog = new Dialog(this);
        closePopupTipDialog = (Button) findViewById(R.id.got_it_button);

        //initialise animations
        animatorRotateOpen = AnimationUtils.loadAnimation(MyKitchenIngredients2.this, R.anim.rotate_open_anim);
        animatorRotateClose = AnimationUtils.loadAnimation(MyKitchenIngredients2.this, R.anim.rotate_close_anim);
        animatorFromBottom = AnimationUtils.loadAnimation(MyKitchenIngredients2.this, R.anim.from_bottom_anim);
        animatorToBottom = AnimationUtils.loadAnimation(MyKitchenIngredients2.this, R.anim.to_bottom_anim);

        //init FAB
        addToKitchenBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient);
        addToKitchenByScanBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient_by_scan);
        addToKitchenByTextBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient_by_text);
        checkedItemsBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient_selected);


        //RCV
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        MyKitchenItemsAdapter2 myKitchenItemsAdapter2 = new MyKitchenItemsAdapter2(foodCategorySectionList);
        mainRecyclerView.setAdapter(myKitchenItemsAdapter2);
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        String dairySection = "Dairy";
        ArrayList<MyKitchenItem> dairySectionItems = new ArrayList<>();

        String vegSection = "Vegtables";
        ArrayList<MyKitchenItem> vegSectionItems = new ArrayList<>();

        String fruitSection = "Fruit";
        ArrayList<MyKitchenItem> fruitSectionItems = new ArrayList<>();

        String meatOrPoultrySection = "Meat or Poultry";
        ArrayList<MyKitchenItem> meatOrPoultrySectionItems = new ArrayList<>();

        String fishSection = "Fish";
        ArrayList<MyKitchenItem> fishSectionItems = new ArrayList<>();

        String frozenSection = "Frozen";
        ArrayList<MyKitchenItem> frozenSectionItems = new ArrayList<>();

        String cupboardSection = "Cupboard";
        ArrayList<MyKitchenItem> cupboardSectionItems = new ArrayList<>();

        String breadOrCerealSection = "Bread or Cereal";
        ArrayList<MyKitchenItem> breadOrCerealSectionItems = new ArrayList<>();


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodCategorySectionList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MyKitchenItem mKI = postSnapshot.getValue(MyKitchenItem.class);
                    if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Dairy")) {
                        dairySectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(dairySection, dairySectionItems));
                        Log.d("hello", String.valueOf(dairySectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Veg")) {
                        vegSectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(vegSection, vegSectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Fruit")) {
                        fruitSectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(fruitSection, fruitSectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Meat/Poultry")) {
                        meatOrPoultrySectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(meatOrPoultrySection, meatOrPoultrySectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Fish")) {
                        fishSectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(fishSection, fishSectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Cupboard")) {
                        cupboardSectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(cupboardSection, cupboardSectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Bread/Cereal")) {
                        breadOrCerealSectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(breadOrCerealSection, breadOrCerealSectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();

                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Freezer")) {
                        frozenSectionItems.add(mKI);
                        foodCategorySectionList.add(new FoodCategorySection(frozenSection, frozenSectionItems));
                        Log.d(TAG, "initData: " + foodCategorySectionList);
                        System.out.println(foodCategorySectionList);
                        //myKitchenItemsAdapter2.notifyDataSetChanged();
                    }
                }
                myKitchenItemsAdapter2.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyKitchenIngredients2.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        addToKitchenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToKitchenBtnClicked();
            }
        });

        addToKitchenByScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanBtnClicked();
            }
        });

        addToKitchenByTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyKitchenIngredients2.this);
                View mView = getLayoutInflater().inflate(R.layout.add_to_my_kitchen_dialog, null);
                EditText itemInput = (EditText) mView.findViewById(R.id.item_name_et);
                EditText itemAmountInput = (EditText) mView.findViewById(R.id.item_amount_et);
                TextView cancelTV = (TextView) mView.findViewById(R.id.cancel_dialog_option);
                TextView addTV = (TextView) mView.findViewById(R.id.add_dialog_option);
                //init variables for dialog
                Spinner itemAmountSpinner = (Spinner) mView.findViewById(R.id.item_amount_spinner);
                Spinner categorySpinner = (Spinner) mView.findViewById(R.id.food_category_spinner2);

                // Create an ArrayAdapter using the string array and a default spinner
                ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(MyKitchenIngredients2.this, R.array.item_amount_array,
                        android.R.layout.simple_spinner_item);
                ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(MyKitchenIngredients2.this, R.array.food_category_array,
                        android.R.layout.simple_spinner_item);

                // Specify the layout to use when the list of choices appears
                staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                itemAmountSpinner.setAdapter(staticAdapter);
                categorySpinner.setAdapter(staticAdapter2);

                addTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!itemInput.getText().toString().isEmpty() && !categorySpinner.getSelectedItem().toString().isEmpty()) {
                            myItemInput = itemInput.getText().toString();
                            myItemAmountInput = itemAmountInput.getText().toString();
                            myItemCategory = categorySpinner.getSelectedItem().toString();
                            myItemMeasurement = itemAmountSpinner.getSelectedItem().toString();
                            itemAmountAndMeasurement = myItemAmountInput + myItemMeasurement;
                            myKitchenItem = new MyKitchenItem(myItemInput, myItemCategory, itemAmountAndMeasurement, userId, itemId);
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference("myKitchenItems");
                            itemId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(itemId).setValue(myKitchenItem);
                            itemInput.setText("");
                            itemAmountInput.setText("");
                            //categorySpinner.set
                            //dialog.dismiss();
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                cancelTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        checkedItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyKitchenItem> mSelectedItems = myKitchenItemsAdapter2.childAdapter.listOfSelectedItems();
                String selectedItemName = "";

                if (mSelectedItems != null) {
                    for (int index = 0; index < mSelectedItems.size(); index++) {
                        System.out.println(mSelectedItems.get(index).itemName);
                        selectedItemName += mSelectedItems.get(index).itemName;
                        if (index != mSelectedItems.size() - 1) {
                            selectedItemName += ",+";
                        }
                    }
                }
                if (selectedItemName != "") {
                    Intent intentRecipe = new Intent(MyKitchenIngredients2.this, RecipeActivity.class);
                    intentRecipe.putExtra("ingredientList", selectedItemName.toString());
                    startActivity(intentRecipe);
                }

                myKitchenItemsAdapter2.childAdapter.ClearSelectedItems();

            }
        });

        //Display popup tip upon opening activity
        popupTipDialog.setContentView(R.layout.tip_popup);
        popupTipDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupTipDialog.show();


        }

private void onScanBtnClicked(){
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Item");
        integrator.initiateScan();
        }

@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
        if(result.getContents()!=null){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(result.getContents());
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Scan Again",new DialogInterface.OnClickListener(){
@Override
public void onClick(DialogInterface dialog,int which){
        onScanBtnClicked();
        }
        }).setNegativeButton("Correct",new DialogInterface.OnClickListener(){
@Override
public void onClick(DialogInterface dialog,int which){
        finish();
        //System.out.println("THE BARCODE" + result.getContents());
        barcode=result.getContents();
        Intent barcodeIntent=new Intent(MyKitchenIngredients2.this,BarcodeActivity.class);
        barcodeIntent.putExtra("barcode",barcode);
        startActivity(barcodeIntent);
        //addBarcodeResultToList();

        }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

        }else{
        Toast.makeText(this,"No result found",Toast.LENGTH_LONG).show();

        }
        }else{
        super.onActivityResult(requestCode,resultCode,data);
        }

        }

public void addBarcodeResultToList(){

        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MyKitchenIngredients2.this);
        View mView=getLayoutInflater().inflate(R.layout.add_to_my_kitchen_dialog,null);
        EditText itemInput=(EditText)mView.findViewById(R.id.item_name_et);
        EditText itemAmountInput=(EditText)mView.findViewById(R.id.item_amount_et);
        TextView cancelTV=(TextView)mView.findViewById(R.id.cancel_dialog_option);
        TextView addTV=(TextView)mView.findViewById(R.id.add_dialog_option);

        //set with barcode item name
        itemInput.setText(barcode);

        //init variables for dialog
        Spinner itemAmountSpinner=(Spinner)mView.findViewById(R.id.item_amount_spinner);
        Spinner categorySpinner=(Spinner)mView.findViewById(R.id.food_category_spinner2);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter=ArrayAdapter.createFromResource(MyKitchenIngredients2.this,R.array.item_amount_array,
        android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> staticAdapter2=ArrayAdapter.createFromResource(MyKitchenIngredients2.this,R.array.food_category_array,
        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        itemAmountSpinner.setAdapter(staticAdapter);
        categorySpinner.setAdapter(staticAdapter2);

//        addTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!itemInput.getText().toString().isEmpty() && !categorySpinner.getSelectedItem().toString().isEmpty()) {
//                    myItemInput = itemInput.getText().toString();
//                    myItemAmountInput = itemAmountInput.getText().toString();
//                    myItemCategory = categorySpinner.getSelectedItem().toString();
//                    myItemMeasurement = itemAmountSpinner.getSelectedItem().toString();
//                    itemAmountAndMeasurement = myItemAmountInput + myItemMeasurement;
//                    myKitchenItem = new MyKitchenItem(myItemInput, myItemCategory, itemAmountAndMeasurement, userId, itemId);
//                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("myKitchenItems");
//                    itemId = mDatabaseRef.push().getKey();
//                    mDatabaseRef.child(itemId).setValue(myKitchenItem);
//                    itemInput.setText("");
//                    itemAmountInput.setText("");
//                    //categorySpinner.set
//                    //dialog.dismiss();
//                }
//            }
//        });
//        mBuilder.setView(mView);
//        AlertDialog dialog = mBuilder.create();
//        dialog.show();
//
//        cancelTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });


        }

private void onAddToKitchenBtnClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        if(!clicked){
        clicked=true;
        }else{
        clicked=false;
        }
        }

private void setAnimation(Boolean clicked){
        if(!clicked){
        addToKitchenByTextBtn.startAnimation(animatorFromBottom);
        addToKitchenByScanBtn.startAnimation(animatorFromBottom);
        addToKitchenBtn.startAnimation(animatorRotateOpen);
        }else{
        addToKitchenByTextBtn.startAnimation(animatorToBottom);
        addToKitchenByScanBtn.startAnimation(animatorToBottom);
        addToKitchenBtn.startAnimation(animatorRotateClose);

        }

        }

private void setVisibility(Boolean clicked){
        if(clicked){
        addToKitchenByScanBtn.setVisibility(View.VISIBLE);
        addToKitchenByTextBtn.setVisibility(View.VISIBLE);
        }else{
        addToKitchenByScanBtn.setVisibility(View.INVISIBLE);
        addToKitchenByTextBtn.setVisibility(View.GONE);

        }
        }

private void setClickable(Boolean clicked){
        if(!clicked){
        addToKitchenByScanBtn.setClickable(true);
        addToKitchenByTextBtn.setClickable(true);
        }else{
        addToKitchenByScanBtn.setClickable(false);
        addToKitchenByTextBtn.setClickable(false);
        }
        }


        }