package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class MyKitchenIngredientsActivity extends AppCompatActivity implements MyKitchenIngredientsAdapter.OnListingListener {

    private static final String TAG = "MyKitchenIngredients";

    private FirebaseUser user;
    DatabaseReference mDatabaseRef;
    MyKitchenItem myKitchenItem;
    private String userId;
    String itemId;
    String ingredientId;
    Dialog popupTipDialog;
    Button closePopupTipDialog;

    //Barcode
    String barcode;

    //Upload Listing
    String itemToList;
    String itemToListID;

    StringBuffer sb = null;

    //RCV
    RecyclerView mVegRecyclerView;
    RecyclerView mFruitRecyclerView;
    RecyclerView mDairyRecyclerView;
    RecyclerView mCerealRecyclerView;
    RecyclerView mMeatRecyclerView;
    RecyclerView mFishRecyclerView;
    RecyclerView mCupboardRecyclerView;
    RecyclerView mFreezerRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<MyKitchenItem> mKitchenItems;
    private List<MyKitchenItem> mDairyKitchenItems;
    private List<MyKitchenItem> mVegKitchenItems;
    private List<MyKitchenItem> mFruitKitchenItems;
    private List<MyKitchenItem> mFishKitchenItems;
    private List<MyKitchenItem> mMeatKitchenItems;
    private List<MyKitchenItem> mCupboardKitchenItems;
    private List<MyKitchenItem> mCerealKitchenItems;
    private List<MyKitchenItem> mFreezerKitchenItems;
    CheckBox checkboxSelected;

    //FAB Animations
    Animation animatorRotateOpen;
    Animation animatorRotateClose;
    Animation animatorFromBottom;
    Animation animatorToBottom;

    Boolean clicked = true;
    String myItemInput, myItemAmountInput, myItemCategory, myItemMeasurement, itemAmountAndMeasurement;

    FloatingActionButton addToKitchenBtn, addToKitchenByScanBtn, addToKitchenByTextBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_kitchen_ingredients);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //Popup Dialog
        popupTipDialog = new Dialog(this);
        closePopupTipDialog = (Button) findViewById(R.id.got_it_button);

        checkboxSelected = (CheckBox) findViewById(R.id.grocery_list_item_check_box);

        //init Veg RCV
        mVegRecyclerView = findViewById(R.id.vegtables_recycler_view);
        mVegRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mVegRecyclerView.setLayoutManager(mLayoutManager);
        mVegKitchenItems = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("myKitchenItems");

        //Fruit RCV
        mFruitRecyclerView = findViewById(R.id.fruit_recycler_view);
        mFruitRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mFruitRecyclerView.setLayoutManager(mLayoutManager);
        mFruitKitchenItems = new ArrayList<>();

        //Dairy RCV
        mDairyRecyclerView = findViewById(R.id.dairy_recycler_view);
        mDairyRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mDairyRecyclerView.setLayoutManager(mLayoutManager);
        mDairyKitchenItems = new ArrayList<>();

        //Cereal or bread RCV
        mCerealRecyclerView = findViewById(R.id.cereal_or_bread_recycler_view);
        mCerealRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mCerealRecyclerView.setLayoutManager(mLayoutManager);
        mCerealKitchenItems = new ArrayList<>();

        //Meat or Poultry RCV
        mMeatRecyclerView = findViewById(R.id.meat_or_poultry_recycler_view);
        mMeatRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mMeatRecyclerView.setLayoutManager(mLayoutManager);
        mMeatKitchenItems = new ArrayList<>();

        //Fish RCV
        mFishRecyclerView = findViewById(R.id.fish_recycler_view);
        mFishRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mFishRecyclerView.setLayoutManager(mLayoutManager);
        mFishKitchenItems = new ArrayList<>();

        //Cupboard RCV
        mCupboardRecyclerView = findViewById(R.id.cupboard_recycler_view);
        mCupboardRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mCupboardRecyclerView.setLayoutManager(mLayoutManager);
        mCupboardKitchenItems = new ArrayList<>();

        //Freezer RCV
        mFreezerRecyclerView = findViewById(R.id.freezer_recycler_view);
        mFreezerRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mFreezerRecyclerView.setLayoutManager(mLayoutManager);
        mFreezerKitchenItems = new ArrayList<>();


        //initialise animations
        animatorRotateOpen = AnimationUtils.loadAnimation(MyKitchenIngredientsActivity.this, R.anim.rotate_open_anim);
        animatorRotateClose = AnimationUtils.loadAnimation(MyKitchenIngredientsActivity.this, R.anim.rotate_close_anim);
        animatorFromBottom = AnimationUtils.loadAnimation(MyKitchenIngredientsActivity.this, R.anim.from_bottom_anim);
        animatorToBottom = AnimationUtils.loadAnimation(MyKitchenIngredientsActivity.this, R.anim.to_bottom_anim);

        //init FAB
        addToKitchenBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient);
        addToKitchenByScanBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient_by_scan);
        addToKitchenByTextBtn = (FloatingActionButton) findViewById(R.id.fab_add_ingredient_by_text);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MyKitchenItem mKI = postSnapshot.getValue(MyKitchenItem.class);
                    if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Dairy")) {
                        mDairyKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mDairyKitchenItems, MyKitchenIngredientsActivity.this);
                        mDairyRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Veg")) {
                        mVegKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mVegKitchenItems, MyKitchenIngredientsActivity.this);
                        mVegRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Fruit")) {
                        mFruitKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mFruitKitchenItems, MyKitchenIngredientsActivity.this);
                        mFruitRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Meat/Poultry")) {
                        mMeatKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mMeatKitchenItems, MyKitchenIngredientsActivity.this);
                        mMeatRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Fish")) {
                        mFishKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mFishKitchenItems, MyKitchenIngredientsActivity.this);
                        mFishRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Cupboard")) {
                        mCupboardKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mCupboardKitchenItems, MyKitchenIngredientsActivity.this);
                        mCupboardRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Bread/Cereal")) {
                        mCerealKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mCerealKitchenItems, MyKitchenIngredientsActivity.this);
                        mCerealRecyclerView.setAdapter(mAdapter);
                    } else if (mKI.getUserId().equals(userId) && mKI.itemCategory.equals("Freezer")) {
                        mFreezerKitchenItems.add(mKI);
                        mAdapter = new MyKitchenIngredientsAdapter(MyKitchenIngredientsActivity.this, (ArrayList<MyKitchenItem>) mFreezerKitchenItems, MyKitchenIngredientsActivity.this);
                        mFreezerRecyclerView.setAdapter(mAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyKitchenIngredientsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyKitchenIngredientsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_to_my_kitchen_dialog, null);
                EditText itemInput = (EditText) mView.findViewById(R.id.item_name_et);
                EditText itemAmountInput = (EditText) mView.findViewById(R.id.item_amount_et);
                TextView cancelTV = (TextView) mView.findViewById(R.id.cancel_dialog_option);
                TextView addTV = (TextView) mView.findViewById(R.id.add_dialog_option);
                //init variables for dialog
                Spinner itemAmountSpinner = (Spinner) mView.findViewById(R.id.item_amount_spinner);
                Spinner categorySpinner = (Spinner) mView.findViewById(R.id.food_category_spinner2);

                // Create an ArrayAdapter using the string array and a default spinner
                ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(MyKitchenIngredientsActivity.this, R.array.item_amount_array,
                        android.R.layout.simple_spinner_item);
                ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(MyKitchenIngredientsActivity.this, R.array.food_category_array,
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

        //Display popup tip upon opening activity
        popupTipDialog.setContentView(R.layout.tip_popup);
        popupTipDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupTipDialog.show();
//
//        closePopupTipDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupTipDialog.cancel();
//            }
//        });

    }

    private void onScanBtnClicked() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Item");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scan Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onScanBtnClicked();
                    }
                }).setNegativeButton("Correct", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        //System.out.println("THE BARCODE" + result.getContents());
                        barcode = result.getContents();
                        Intent barcodeIntent = new Intent(MyKitchenIngredientsActivity.this, BarcodeActivity.class);
                        barcodeIntent.putExtra("barcode", barcode);
                        startActivity(barcodeIntent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                Toast.makeText(this, "No result found", Toast.LENGTH_LONG).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void onAddToKitchenBtnClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        if (!clicked) {
            clicked = true;
        } else {
            clicked = false;
        }
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
            addToKitchenByTextBtn.startAnimation(animatorFromBottom);
            addToKitchenByScanBtn.startAnimation(animatorFromBottom);
            addToKitchenBtn.startAnimation(animatorRotateOpen);
        } else {
            addToKitchenByTextBtn.startAnimation(animatorToBottom);
            addToKitchenByScanBtn.startAnimation(animatorToBottom);
            addToKitchenBtn.startAnimation(animatorRotateClose);

        }

    }

    private void setVisibility(Boolean clicked) {
        if (clicked) {
            addToKitchenByScanBtn.setVisibility(View.VISIBLE);
            addToKitchenByTextBtn.setVisibility(View.VISIBLE);
        } else {
            addToKitchenByScanBtn.setVisibility(View.INVISIBLE);
            addToKitchenByTextBtn.setVisibility(View.GONE);

        }
    }

    private void setClickable(Boolean clicked) {
        if (!clicked) {
            addToKitchenByScanBtn.setClickable(true);
            addToKitchenByTextBtn.setClickable(true);
        } else {
            addToKitchenByScanBtn.setClickable(false);
            addToKitchenByTextBtn.setClickable(false);
        }
    }

    @Override
    public void onListingClick(int position) {
        //Log.d(TAG, "onListingClicked:  clicked");
        mDairyKitchenItems.get(position);
        //Toast.makeText(MyKitchenIngredientsActivity.this, (CharSequence) mDairyKitchenItems.get(position), Toast.LENGTH_SHORT).show();
        Log.d(TAG, String.valueOf(position));
        //mVegKitchenItems.get(position);
        //mFruitKitchenItems.get(position);
        //mCerealKitchenItems.get(position);
        //mMeatKitchenItems.get(position);
        //mFishKitchenItems.get(position);
        //mFreezerKitchenItems.get(position);
        //mCupboardKitchenItems.get(position);

        ingredientId = mDairyKitchenItems.get(position).getItemId();
        Log.d(TAG, "THE INGREDIENT ID: " + ingredientId);
        Intent listItemIntent = new Intent(this, MoveItemToListingActivity.class);
        listItemIntent.putExtra("item_to_list", ingredientId);
        startActivity(listItemIntent);

    }

}