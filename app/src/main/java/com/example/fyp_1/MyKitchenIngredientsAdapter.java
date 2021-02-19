package com.example.fyp_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyKitchenIngredientsAdapter extends RecyclerView.Adapter<MyKitchenIngredientsAdapter.ExampleViewHolder> {
    private ArrayList<MyKitchenItem> mGroceryList;
    private Context mContext;
    private OnListingListener mOnListingListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Grocery List Item
        public TextView itemName;
        OnListingListener mOnListingListener;

        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.grocery_list_item_display);
            mOnListingListener = onListingListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onListingClick(getAdapterPosition());
        }
    }
    public interface OnListingListener{
        void onListingClick(int position);
    }


    public MyKitchenIngredientsAdapter(Context context, ArrayList<MyKitchenItem> groceryLists, OnListingListener onListingListener) {
        super();
        mContext = context;
        mGroceryList = groceryLists;
        this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, parent, false);
        return new ExampleViewHolder(v,mOnListingListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        MyKitchenItem currentList = mGroceryList.get(position);
        holder.itemName.setText(currentList.getItemName());

    }

    @Override
    public int getItemCount() {

        return mGroceryList.size();
    }
}
