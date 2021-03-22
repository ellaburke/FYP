package com.example.fyp_1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyKitchenIngredientsChildAdapter<mGroceryListChecked> extends RecyclerView.Adapter<MyKitchenIngredientsChildAdapter.ViewHolder> {

    List<MyKitchenItem> items;
    public static ArrayList<MyKitchenItem> mGroceryListChecked;  //= new ArrayList<>();

    public MyKitchenIngredientsChildAdapter(List<MyKitchenItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyKitchenIngredientsChildAdapter.ViewHolder holder, int position) {
//        holder.itemTextView.setText((CharSequence) items.get(position));

        MyKitchenItem section = items.get(position);
        String ingredientName = section.getItemName();
        holder.itemTextView.setText(ingredientName);

        final MyKitchenItem allSelected = items.get(position);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    if (mGroceryListChecked == null) {
                        mGroceryListChecked = new ArrayList<>();
                    }
                    mGroceryListChecked.add(allSelected);
                    Log.d("SELECTED ITEM", String.valueOf(mGroceryListChecked));
                } else {
                    mGroceryListChecked.remove(allSelected);

                }
            }
        });


    }

    public ArrayList<MyKitchenItem> listOfSelectedItems() {
        return mGroceryListChecked;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void ClearSelectedItems () {
        mGroceryListChecked.clear();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.grocery_list_item_display);
            checkBox = itemView.findViewById(R.id.grocery_list_item_check_box);
        }
    }


}