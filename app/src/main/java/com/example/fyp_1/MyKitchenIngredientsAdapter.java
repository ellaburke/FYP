package com.example.fyp_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyKitchenIngredientsAdapter extends RecyclerView.Adapter<MyKitchenIngredientsAdapter.ExampleViewHolder> {
    private ArrayList<MyKitchenItem> mGroceryList;
    private ArrayList<MyKitchenItem> checkedKitchenItems = new ArrayList<>();
    private Context mContext;
    private OnListingListener mOnListingListener;
    //private AdapterView.OnItemClickListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Grocery List Item
        public TextView itemName;
        OnListingListener mOnListingListener;
        CheckBox mCheckBoxSelected;
        AdapterView.OnItemClickListener mItemClickListener;


        public ExampleViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.grocery_list_item_display);
            mCheckBoxSelected = itemView.findViewById(R.id.grocery_list_item_check_box);
            mOnListingListener = onListingListener;

            itemView.setOnClickListener(this);
            mCheckBoxSelected.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mOnListingListener.onListingClick(getAdapterPosition());
            this.mItemClickListener.onItemClick((AdapterView<?>) itemView,v,getLayoutPosition(),getItemId());
        }

        public void setmItemClickListener(AdapterView.OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }
    }

    public interface OnListingListener {
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
        return new ExampleViewHolder(v, mOnListingListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        MyKitchenItem currentList = mGroceryList.get(position);
        holder.itemName.setText(currentList.getItemName());

        holder.setmItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CheckBox checkBox = (CheckBox) view;

                    //If checked or not
                if(checkBox.isChecked()){
                    checkedKitchenItems.add(mGroceryList.get(position));

                }else if(!checkBox.isChecked()){
                    checkedKitchenItems.remove(mGroceryList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return mGroceryList.size();
    }
}
