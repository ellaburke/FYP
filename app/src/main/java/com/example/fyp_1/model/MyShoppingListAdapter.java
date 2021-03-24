package com.example.fyp_1.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.R;

import java.util.List;

public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListAdapter.ViewHolder> {

    private List<MyShoppingListItem> shoppingListItem;

    public MyShoppingListAdapter(List<MyShoppingListItem> shoppingListItem) {
        this.shoppingListItem = shoppingListItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyShoppingListItem listItem = shoppingListItem.get(position);
        String listItemName = listItem.getName();
        holder.itemName.setText(listItemName);

    }

    @Override
    public int getItemCount() {
        return shoppingListItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.grocery_list_item_display);

        }
    }
}
