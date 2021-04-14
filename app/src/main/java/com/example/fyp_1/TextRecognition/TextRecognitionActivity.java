package com.example.fyp_1.TextRecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fyp_1.R;
import com.example.fyp_1.ShoppingListTab.MyShoppingListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import android.net.Uri;

import java.io.IOException;
import java.util.List;

public class TextRecognitionActivity extends AppCompatActivity {

    //Constants
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    static final int RESULT_GALLERY = 0;

    //Variables
    private Button captureImageBtn, detectTextBtn, galleryImageButton;
    private ImageView imageView;
    private EditText textView;
    Bitmap imageBitmap;
    String itemsList = "";
    String newItemsList = "";
    InputImage inputVisionImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);

        captureImageBtn = findViewById(R.id.capture_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        galleryImageButton = findViewById(R.id.gallery_image_btn);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        // set text for demo
        //textView.setText("Please choose / capture an image!!");

        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_GALLERY);

            }
        });

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    e.printStackTrace();
                }
            }
        });

        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call MyShopping List Activity
                newItemsList += textView.getText();
                Intent myShoppingListIntent = new Intent(getApplicationContext(), MyShoppingListActivity.class);
                myShoppingListIntent.putExtra("addToDB",newItemsList);
                startActivity(myShoppingListIntent);
                overridePendingTransition(0,0);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!= null) {
            textView.setText("Detecting Ingredients");
           // detectTextBtn.setText("Add to Shopping List");
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    //imageView.setImageBitmap(imageBitmap);
                    inputVisionImage = InputImage.fromBitmap(imageBitmap, 0);
                    imageView.setImageBitmap(imageBitmap);
                    detectTextFromImage();
                    break;
                case RESULT_GALLERY:
                    Uri imageUri = data.getData();
                    System.out.println("Uri - " + imageUri.getPath());
                    try {
                        inputVisionImage = InputImage.fromFilePath(TextRecognitionActivity.this, imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageURI(imageUri);
                    detectTextFromImage();
                    break;
                default:
                    break;
            }
        }
    }



    private void detectTextFromImage() {
        TextRecognizer recognizer = TextRecognition.getClient();

        Task<Text> result = recognizer.process(inputVisionImage)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text firebaseVisionText) {
                        displayTextFromImage(firebaseVisionText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TextRecognitionActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.d("Error", e.getMessage());
                    }
                });
    }

    private void displayTextFromImage(Text firebaseVisionText) {
        List<Text.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if(blockList.size() ==0){
            Toast.makeText(this,"No Text Found In Image", Toast.LENGTH_SHORT).show();
        }
        else{
            for(Text.TextBlock block: firebaseVisionText.getTextBlocks()){
                itemsList += block.getText() +"\n";
            }
            textView.setText(itemsList);
        }
    }
}