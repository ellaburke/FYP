package com.example.fyp_1.UserProfileAndListings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.R;
import com.example.fyp_1.model.Listing;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyListingProfileAdapter extends RecyclerView.Adapter<MyListingProfileAdapter.ExampleViewHolder>{
    private List<Listing> mListings;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Listing Image
        public ImageView mImageView;
        OnListingListener mOnListingListener;


        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.myListingProfileImage);
            mOnListingListener = onListingListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onListingClick(getAdapterPosition());
        }
    }

    public interface OnListingListener {
        void onListingClick(int position);
    }

    public MyListingProfileAdapter(Context context, ArrayList<Listing> userListings, OnListingListener onListingListener) {
        //super();
        mContext = context;
        mListings = userListings;
        this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_listing_profile_card_view, parent, false);
        return new ExampleViewHolder(v, mOnListingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Listing currentListing = mListings.get(position);
        Picasso.get()
                .load(currentListing.getListingImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mListings.size();
    }
}
