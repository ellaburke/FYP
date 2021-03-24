//package com.example.fyp_1;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
//
//import java.util.List;
//
//public class TextRecognitionActivity extends AppCompatActivity {
//
//    //Constants
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//    private static final int RESULT_LOAD_IMAGE = 2;
//    //Variables
//    private Button captureImageBtn, detectTextBtn, galleryImageButton;
//    private ImageView imageView;
//    private TextView textView;
//    Bitmap imageBitmap;
//    Bitmap selectedImage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_text_recognition);
//
//
//        captureImageBtn = findViewById(R.id.capture_image_btn);
//        detectTextBtn = findViewById(R.id.detect_text_image_btn);
//        galleryImageButton = findViewById(R.id.gallery_image_btn);
//        imageView = findViewById(R.id.image_view);
//        textView = findViewById(R.id.text_display);
//
//        galleryImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
//            }
//        });
//
//        captureImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent();
//                textView.setText("");
//            }
//        });
//
//        detectTextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detectTextFromImage();
//            }
//        });
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            // display error state to the user
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }
////        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
////            Bundle extras = data.getExtras();
////            imageBitmap = (Bitmap) extras.get("data");
////            imageView.setImageBitmap(imageBitmap);
////        }
//    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (resultCode == RESULT_OK) {
////            switch (requestCode) {
////                case RESULT_LOAD_IMAGE: {
////                    Bundle extras = data.getExtras();
////                    selectedImage = (Bitmap) extras.get("data");
////                    imageView.setImageBitmap(selectedImage);
////                }
////                break;
////                case REQUEST_IMAGE_CAPTURE: {
////                    Bundle extras = data.getExtras();
////                    selectedImage = (Bitmap) extras.get("data");
////                    imageView.setImageBitmap(selectedImage);
////                }
////                break;
////            }
////        }
////    }
//
//    private void detectTextFromImage() {
//        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
//        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
//        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//            @Override
//            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                displayTextFromImage(firebaseVisionText);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(TextRecognitionActivity.this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                Log.d("Error", e.getMessage());
//            }
//        });
//    }
//
//    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
//        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
//        if(blockList.size() ==0){
//            Toast.makeText(this,"No Text Found In Image", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            for(FirebaseVisionText.Block block: firebaseVisionText.getBlocks()){
//                String text = block.getText();
//                textView.setText(text);
//            }
//        }
//    }
//}