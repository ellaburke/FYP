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

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ExampleViewHolder> {
    private List<User> mGroceryList;
    private Context mContext;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        //Grocery List Item
        public TextView itemName;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.grocery_list_item_display);

        }
    }


    public ListAdapter(Context context, ArrayList<User> groceryLists) {
        //super();
        mContext = context;
        mGroceryList = groceryLists;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, parent, false);
        return new ExampleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        User currentList = mGroceryList.get(position);
        holder.itemName.setText((CharSequence) currentList.getUsersGroceryList());

    }

    @Override
    public int getItemCount() {

        return mGroceryList.size();
    }
}
