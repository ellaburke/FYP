package com.example.fyp_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyKitchenItemsAdapter2 extends RecyclerView.Adapter<MyKitchenItemsAdapter2.ViewHolder> {

    List<FoodCategorySection> sectionList;

    public MyKitchenItemsAdapter2(List<FoodCategorySection> sectionList) {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.food_category_section_rcv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodCategorySection section = sectionList.get(position);
        String sectionName = section.getSectionName();
        List<MyKitchenItem> items = section.getSectionItem();

        holder.sectionNameTextView.setText(sectionName);

        MyKitchenIngredientsChildAdapter childAdapter = new MyKitchenIngredientsChildAdapter(items);
        holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView sectionNameTextView;
        RecyclerView childRecyclerView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            sectionNameTextView = itemView.findViewById(R.id.sectionNameTextView);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        }
    }
}
