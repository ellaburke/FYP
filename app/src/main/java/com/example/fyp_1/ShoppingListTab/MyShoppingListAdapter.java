package com.example.fyp_1.ShoppingListTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.Notifications.NotificationAdapter;
import com.example.fyp_1.R;
import com.example.fyp_1.model.MyShoppingListItem;

import java.util.List;

public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListAdapter.ViewHolder> {

    private List<MyShoppingListItem> shoppingListItem;
    private OnListingListener mOnListingListener;

    public MyShoppingListAdapter(List<MyShoppingListItem> shoppingListItem, OnListingListener onListingListener) {
        this.shoppingListItem = shoppingListItem;
        this.mOnListingListener = onListingListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view, mOnListingListener);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView itemName;
        CheckBox checkBoxDelete;
        OnListingListener mOnListingListener;

        public ViewHolder(@NonNull View itemView, OnListingListener onListingListener) {
            super(itemView);

            itemName = itemView.findViewById(R.id.grocery_list_item_display);
            checkBoxDelete = itemView.findViewById(R.id.grocery_list_item_check_box);
            mOnListingListener = onListingListener;

            checkBoxDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnListingListener.onItemToRemoveFromList(this.getAdapterPosition());
        }
    }

    public interface OnListingListener {
        void onItemToRemoveFromList(int position);

    }
}
