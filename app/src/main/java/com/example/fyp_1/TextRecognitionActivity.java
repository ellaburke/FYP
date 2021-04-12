package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.squareup.picasso.Picasso;


import java.util.List;

public class TextRecognitionActivity extends AppCompatActivity {

    //Constants
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};
    //Variables
    private Button captureImageBtn, detectTextBtn, galleryImageButton;
    private ImageView imageView;
    private TextView textView;
    Bitmap imageBitmap;
    Bitmap selectedImage;

    //Permissions
    String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;

    String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE;

    String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);


        captureImageBtn = findViewById(R.id.capture_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        galleryImageButton = findViewById(R.id.gallery_image_btn);
        imageView = findViewById(R.id.image_view1);
        textView = findViewById(R.id.text_display);



        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                textView.setText("");
            }
        });

        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextFromImage();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK ) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //Uri selectedImage = data.getData();
            System.out.println("IMAGE BITMAP" + imageBitmap);
            imageView.setImageBitmap(imageBitmap);
            //Picasso.get().load(selectedImage).into(imageView);



        }

    }

    private void detectTextFromImage() {
        int rotationDegree = 0;
        InputImage firebaseVisionImage = InputImage.fromBitmap(imageBitmap,rotationDegree);
        TextRecognizer recognizer = TextRecognition.getClient();

        Task<Text> result = recognizer.process(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text firebaseVisionText) {
                        displayTextFromImage(firebaseVisionText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TextRecognitionActivity.this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

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
                String text = block.getText();
                textView.setText(text);
            }
        }
    }
}
