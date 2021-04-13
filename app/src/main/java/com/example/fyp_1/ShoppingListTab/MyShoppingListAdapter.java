package com.example.fyp_1.ShoppingListTab;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.MyKitchenItem;
import com.example.fyp_1.Notifications.NotificationAdapter;
import com.example.fyp_1.R;
import com.example.fyp_1.model.MyShoppingListItem;

import java.util.ArrayList;
import java.util.List;

public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListAdapter.ViewHolder> {

    private List<MyShoppingListItem> shoppingListItem;
    //private OnListingListener mOnListingListener;
    public static ArrayList<MyShoppingListItem> mShoppingListChecked;  //= new ArrayList<>();

    public MyShoppingListAdapter(List<MyShoppingListItem> shoppingListItem) {
        this.shoppingListItem = shoppingListItem;
        //this.mOnListingListener = onListingListener;
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


        final MyShoppingListItem allSelected = shoppingListItem.get(position);


        holder.checkBoxDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBoxDelete.isChecked()) {
                    if (mShoppingListChecked == null) {
                        mShoppingListChecked = new ArrayList<>();
                    }
                    mShoppingListChecked.add(allSelected);
                    Log.d("SELECTED ITEM", String.valueOf(mShoppingListChecked));
                } else {
                    mShoppingListChecked.remove(allSelected);

                }
            }
        });

    }

    public ArrayList<MyShoppingListItem> listOfSelectedItems() {
        return mShoppingListChecked;
    }

    public void ClearSelectedItems () {
        mShoppingListChecked.clear();
    }

    @Override
    public int getItemCount() {
        return shoppingListItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        CheckBox checkBoxDelete;
        //OnListingListener mOnListingListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.grocery_list_item_display);
            checkBoxDelete = itemView.findViewById(R.id.grocery_list_item_check_box);
           // mOnListingListener = onListingListener;

            //checkBoxDelete.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View v) {
//            mOnListingListener.onItemToRemoveFromList(this.getAdapterPosition());
//        }
    }

//    public interface OnListingListener {
//        void onItemToRemoveFromList(int position);
//
//    }

}
