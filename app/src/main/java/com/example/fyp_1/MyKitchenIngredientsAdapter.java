package com.example.fyp_1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyKitchenIngredientsAdapter extends RecyclerView.Adapter<MyKitchenIngredientsAdapter.ExampleViewHolder> {
    private ArrayList<MyKitchenItem> mGroceryList;
    public ArrayList<MyKitchenItem> mGroceryListChecked  = new ArrayList<>();
    private Context mContext;
    //private OnListingListener mOnListingListener;

    //implements View.OnClickListener
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Grocery List Item
        public TextView itemName;
        public CheckBox checkBox;

        //
        //OnListingListener mOnListingListener;


        //, OnListingListener onListingListener
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.grocery_list_item_display);
            checkBox = itemView.findViewById(R.id.grocery_list_item_check_box);
            //mOnListingListener = onListingListener;
            //checkBox.setOnClickListener(this);

        }


//        @Override
//        public void onClick(View v) {
//            mOnListingListener.onListingClick(getAdapterPosition());
//        }

    }

//    public interface OnListingListener {
//        void onListingClick(int position);
//    }


    //, OnListingListener onListingListener
    public MyKitchenIngredientsAdapter(Context context, ArrayList<MyKitchenItem> groceryLists) {
        super();
        mContext = context;
        mGroceryList = groceryLists;
        //this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, parent, false);
        return new ExampleViewHolder(v);
        //, mOnListingListener
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        MyKitchenItem currentList = mGroceryList.get(position);
        holder.itemName.setText(currentList.getItemName());

        final MyKitchenItem allSelected = mGroceryList.get(position);
        System.out.println("HERE" + allSelected);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    mGroceryListChecked.add(allSelected);
                    Log.d("SELECTED ITEM", String.valueOf(mGroceryListChecked));
                } else {
                    mGroceryListChecked.remove(allSelected);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return mGroceryList.size();
    }
    public ArrayList<MyKitchenItem> listOfSelectedItems() {
        return mGroceryListChecked;
    }
}