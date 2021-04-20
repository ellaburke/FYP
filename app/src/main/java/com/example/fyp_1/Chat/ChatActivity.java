package com.example.fyp_1.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.fyp_1.MyKitchenIngredients2;
import com.example.fyp_1.R;
import com.example.fyp_1.UserProfileAndListings.MyListingsProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    public static final String MESSAGES_CHILD = "messages";
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;

    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private static final String MESSAGE_SENT_EVENT = "message_sent";

    private SharedPreferences mSharedPreferences;

    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>
            mFirebaseAdapter;

    //Current user details + profile pic
    StorageReference storageRef;
    StorageReference profilerCurrentRef;
    private FirebaseUser user;
    private String userId;

    //UI
    ProgressBar progressBar;
    RecyclerView messageRecyclerView;
    EditText messageEditText;
    Button messageSendButton;
    ImageView addMessageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //Current User Profile Pic
        storageRef = FirebaseStorage.getInstance().getReference();
        profilerCurrentRef = storageRef.child(user.getUid() + ".jpg");

        // Initialize Realtime Database
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGES_CHILD);

        //Init UI
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageSendButton = (Button) findViewById(R.id.messageSendButton);
        addMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);

        // The FirebaseRecyclerAdapter class comes from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(messagesRef, ChatMessage.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(MessageViewHolder vh, int position, ChatMessage message) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                vh.bindMessage(message);
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        messageRecyclerView.setAdapter(mFirebaseAdapter);

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver.java for details
        mFirebaseAdapter.registerAdapterDataObserver(
                new MyScrollToBottomObserver(messageRecyclerView, mFirebaseAdapter, mLinearLayoutManager));


        // Disable the send button when there's no text in the input field
        // See MyButtonObserver.java for details
        messageEditText.addTextChangedListener(new MyButtonObserver(messageSendButton));

        // When the send button is clicked, send a text message
        messageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chatMessage = new
                        ChatMessage(messageEditText.getText().toString(),
                        getUserName(),
                        getUserPhotoUrl(),
                        null /* no image */);

                mDatabase.getReference().child(MESSAGES_CHILD).push().setValue(chatMessage);
                messageEditText.setText("");
            }
        });

        // When the image button is clicked, launch the image picker
        addMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());

                final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                ChatMessage tempMessage = new ChatMessage(
                        null, getUserName(), getUserPhotoUrl(), LOADING_IMAGE_URL);

                mDatabase.getReference().child(MESSAGES_CHILD).push()
                        .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.w(TAG, "Unable to write message to database.",
                                            databaseError.toException());
                                    return;
                                }

                                // Build a StorageReference and then upload the file
                                String key = databaseReference.getKey();
                                StorageReference storageReference =
                                        FirebaseStorage.getInstance()
                                                .getReference(user.getUid())
                                                .child(key)
                                                .child(uri.getLastPathSegment());

                                putImageInStorage(storageReference, uri, key);
                            }
                        });
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // After the image loads, get a public downloadUrl for the image
                        // and add it to the message.
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ChatMessage friendlyMessage = new ChatMessage(
                                                null, getUserName(), getUserPhotoUrl(), uri.toString());
                                        mDatabase.getReference()
                                                .child(MESSAGES_CHILD)
                                                .child(key)
                                                .setValue(friendlyMessage);
                                    }
                                });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Image upload task was not successful.", e);
                    }
                });
    }



    @Nullable
    private String getUserPhotoUrl() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            return user.getPhotoUrl().toString();
        }

        return null;
    }

    private String getUserName() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }

        return ANONYMOUS;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_back_icon) {
            Intent backToProfileIntent = new Intent(ChatActivity.this, MyKitchenIngredients2.class);
            startActivity(backToProfileIntent);
            return true;
        }

        return true;
    }
}