//package com.example.fyp_1;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Base64;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.functions.FirebaseFunctions;
//import com.google.firebase.functions.HttpsCallableResult;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonPrimitive;
//
//import java.io.ByteArrayOutputStream;
//
//public class TextDetectActivity extends AppCompatActivity {
//
//    private FirebaseFunctions mFunctions;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_text_detect);
//
//        mFunctions = FirebaseFunctions.getInstance();
//
//        // Create json request to cloud vision
//        JsonObject request = new JsonObject();
//        // Add image to request
//        JsonObject image = new JsonObject();
//        //image.add("content", new JsonPrimitive(base64encoded));
//        request.add("image", image);
//        //Add features to the request
//        JsonObject feature = new JsonObject();
//        feature.add("type", new JsonPrimitive("TEXT_DETECTION"));
//        // Alternatively, for DOCUMENT_TEXT_DETECTION:
//
//        //feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
//        JsonArray features = new JsonArray();
//        features.add(feature);
//        request.add("features", features);
//
//
//        annotateImage(request.toString())
//                .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
//                    @Override
//                    public void onComplete(@NonNull Task<JsonElement> task) {
//                        if (!task.isSuccessful()) {
//                            // Task failed with an exception
//                            // ...
//                        } else {
//                            // Task completed successfully
//                            // ...
//                        }
//                    }
//                });
//    }
//
//    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
//        int originalWidth = bitmap.getWidth();
//        int originalHeight = bitmap.getHeight();
//        int resizedWidth = maxDimension;
//        int resizedHeight = maxDimension;
//
//        if (originalHeight > originalWidth) {
//            resizedHeight = maxDimension;
//            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
//        } else if (originalWidth > originalHeight) {
//            resizedWidth = maxDimension;
//            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
//        } else if (originalHeight == originalWidth) {
//            resizedHeight = maxDimension;
//            resizedWidth = maxDimension;
//        }
//        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
//    }
//
//    private Task<JsonElement> annotateImage(String requestJson) {
//        return mFunctions
//                .getHttpsCallable("annotateImage")
//                .call(requestJson)
//                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
//                    @Override
//                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
//                        // This continuation runs on either success or failure, but if the task
//                        // has failed then getResult() will throw an Exception which will be
//                        // propagated down.
//                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
//                    }
//                });
//    }
//
//
//}