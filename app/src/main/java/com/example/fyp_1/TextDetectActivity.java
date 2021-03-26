//package com.example.fyp_1;
//
//
//import android.content.Context;
//import android.content.res.AssetManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.util.Pair;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.text.Text;
//import com.google.mlkit.vision.text.TextRecognition;
//import com.google.mlkit.vision.text.TextRecognizer;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.PriorityQueue;
//
//
//public class TextDetectActivity extends AppCompatActivity {
//
//    //UI Components
//    private static final String TAG = "TextDetectActivity";
//    private ImageView mImageView;
//    private Button mTextButton;
//
//    //Selected Image
//    private Bitmap mSelectedImage;
//    // Max width (portrait mode)
//    private Integer mImageMaxWidth;
//    // Max height (portrait mode)
//    private Integer mImageMaxHeight;
//
//
//
//    //Number of results to show in UI
//    private static final int RESULTS_TO_SHOW = 3;
//
//    //Dimensions of Inputs
//    private static final int DIM_IMG_SIZE_X = 224;
//    private static final int DIM_IMG_SIZE_Y = 224;
//
//    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
//            new PriorityQueue<>(
//                    RESULTS_TO_SHOW,
//                    new Comparator<Map.Entry<String, Float>>() {
//                        @Override
//                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
//                                o2) {
//                            return (o1.getValue()).compareTo(o2.getValue());
//                        }
//                    });
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_text_detect);
//
//        mImageView = findViewById(R.id.image_view);
//        mTextButton = findViewById(R.id.button_text);
//
//        mTextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runTextRecognition();
//            }
//        });
//    }
//
//    private void runTextRecognition() {
//        InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
//        TextRecognizer recognizer = TextRecognition.getClient();
//        mTextButton.setEnabled(false);
//        recognizer.process(image)
//                .addOnSuccessListener(
//                        new OnSuccessListener<Text>() {
//                            @Override
//                            public void onSuccess(Text texts) {
//                                mTextButton.setEnabled(true);
//                                processTextRecognitionResult(texts);
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Task failed with an exception
//                                mTextButton.setEnabled(true);
//                                e.printStackTrace();
//                            }
//                        });
//
//
//    }
//
//    private void processTextRecognitionResult(Text texts) {
//        List<Text.TextBlock> blocks = texts.getTextBlocks();
//        if (blocks.size() == 0) {
//            Toast.makeText(getApplicationContext(), "No Text Found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        for (int i = 0; i < blocks.size(); i++) {
//            List<Text.Line> lines = blocks.get(i).getLines();
//            for (int j = 0; j < lines.size(); j++) {
//                List<Text.Element> elements = lines.get(j).getElements();
//                for (int k = 0; k < elements.size(); k++) {
////                    Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
////                    mGraphicOverlay.add(textGraphic);
//
//                }
//            }
//        }
//    }
//
//        // Functions for loading images from app assets.
//
//        // Returns max image width, always for portrait mode. Caller needs to swap width / height for
//        // landscape mode.
//        private Integer getImageMaxWidth () {
//            if (mImageMaxWidth == null) {
//                // Calculate the max width in portrait mode. This is done lazily since we need to
//                // wait for
//                // a UI layout pass to get the right values. So delay it to first time image
//                // rendering time.
//                mImageMaxWidth = mImageView.getWidth();
//            }
//
//            return mImageMaxWidth;
//        }
//
//        // Returns max image height, always for portrait mode. Caller needs to swap width / height for
//        // landscape mode.
//        private Integer getImageMaxHeight () {
//            if (mImageMaxHeight == null) {
//                // Calculate the max width in portrait mode. This is done lazily since we need to
//                // wait for
//                // a UI layout pass to get the right values. So delay it to first time image
//                // rendering time.
//                mImageMaxHeight =
//                        mImageView.getHeight();
//            }
//
//            return mImageMaxHeight;
//        }
//
//        // Gets the targeted width / height.
//        private Pair<Integer, Integer> getTargetedWidthHeight () {
//            int targetWidth;
//            int targetHeight;
//            int maxWidthForPortraitMode = getImageMaxWidth();
//            int maxHeightForPortraitMode = getImageMaxHeight();
//            targetWidth = maxWidthForPortraitMode;
//            targetHeight = maxHeightForPortraitMode;
//            return new Pair<>(targetWidth, targetHeight);
//        }
//
//        public void onItemSelected (AdapterView< ? > parent, View v, int position, long id){
//            //mGraphicOverlay.clear();
//            switch (position) {
//                case 0:
//                    mSelectedImage = getBitmapFromAsset(this, "Please_walk_on_the_grass.jpg");
//                    break;
//                case 1:
//                    // Whatever you want to happen when the thrid item gets selected
//                    mSelectedImage = getBitmapFromAsset(this, "grace_hopper.jpg");
//                    break;
//            }
//            if (mSelectedImage != null) {
//                // Get the dimensions of the View
//                Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();
//
//                int targetWidth = targetedSize.first;
//                int maxHeight = targetedSize.second;
//
//                // Determine how much to scale down the image
//                float scaleFactor =
//                        Math.max(
//                                (float) mSelectedImage.getWidth() / (float) targetWidth,
//                                (float) mSelectedImage.getHeight() / (float) maxHeight);
//
//                Bitmap resizedBitmap =
//                        Bitmap.createScaledBitmap(
//                                mSelectedImage,
//                                (int) (mSelectedImage.getWidth() / scaleFactor),
//                                (int) (mSelectedImage.getHeight() / scaleFactor),
//                                true);
//
//                mImageView.setImageBitmap(resizedBitmap);
//                mSelectedImage = resizedBitmap;
//            }
//        }
//
//        //@Override
//        public void onNothingSelected (AdapterView < ? > parent){
//            // Do nothing
//        }
//
//        public static Bitmap getBitmapFromAsset (Context context, String filePath){
//            AssetManager assetManager = context.getAssets();
//
//            InputStream is;
//            Bitmap bitmap = null;
//            try {
//                is = assetManager.open(filePath);
//                bitmap = BitmapFactory.decodeStream(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return bitmap;
//        }
//    }
