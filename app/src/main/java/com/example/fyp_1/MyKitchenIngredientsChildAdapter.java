package com.example.fyp_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyKitchenIngredientsChildAdapter extends RecyclerView.Adapter<MyKitchenIngredientsChildAdapter.ViewHolder> {

    List<MyKitchenItem> items;

    public MyKitchenIngredientsChildAdapter(List<MyKitchenItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_list_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTextView.setText((CharSequence) items.get(position));

//        MyKitchenItem section = items.get(position);
//        String ingredientName = section.getItemName();
//        holder.itemTextView.setText(ingredientName);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemTextView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemTextView = itemView.findViewById(R.id.grocery_list_item_display);
        }
    }


}
