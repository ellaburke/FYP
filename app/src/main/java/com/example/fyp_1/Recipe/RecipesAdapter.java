package com.example.fyp_1.Recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.R;
import com.example.fyp_1.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ExampleViewHolder> {
    private ArrayList<Recipe> mRecipes;
    private Context mContext;
    private OnRecipeListener mOnRecipeListener;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Recipe List Item
        public TextView itemName;
        public ImageView itemImage;
        public TextView itemMissingIngredientAmount;
        OnRecipeListener mOnRecipeListener;

        public ExampleViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.recipes_list_item_display);
            itemImage = itemView.findViewById(R.id.recipe_image_display);
            itemMissingIngredientAmount = itemView.findViewById(R.id.missed_ingredient_count_display);
            this.mOnRecipeListener = onRecipeListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnRecipeListener.onRecipeClick(getAdapterPosition());
        }
    }
    public interface OnRecipeListener{
        void onRecipeClick(int position);
    }


    public RecipesAdapter(Context context, ArrayList<Recipe> recipes, OnRecipeListener onRecipeListener) {
        super();
        this.mContext = context;
        this.mRecipes = recipes;
        this.mOnRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item, parent, false);
        return new ExampleViewHolder(v,mOnRecipeListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Recipe currentList = mRecipes.get(position);
        holder.itemName.setText(currentList.getTitle());
        holder.itemMissingIngredientAmount.setText(currentList.getMissedIngredientCount());
        Picasso.get()
                .load(currentList.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.itemImage);

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}