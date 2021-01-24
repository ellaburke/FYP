package com.example.fyp_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> {
    private List<Listing> mListings;
    private Context mContext;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Listing
        public ImageView mImageView;
        public TextView textViewName;
        public TextView textViewLocation;
        public TextView textViewExpiryDate;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.listing_image_display);
            textViewName = itemView.findViewById(R.id.listing_name_display);
            textViewLocation = itemView.findViewById(R.id.listing_location_display);
            textViewExpiryDate = itemView.findViewById(R.id.listing_date_display);

            //itemView.setOnClickListener((View.OnClickListener) this);
        }
    }


    public Adapter(Context context, ArrayList<Listing> userListings) {
        //super();
        mContext = context;
        mListings = userListings;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.listing_item, parent, false);
            return new ExampleViewHolder(v);
        }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Listing currentListing = mListings.get(position);
        holder.textViewName.setText(currentListing.getName());
        holder.textViewLocation.setText(currentListing.getLocation());
        holder.textViewExpiryDate.setText(currentListing.getExpiryDate());
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
