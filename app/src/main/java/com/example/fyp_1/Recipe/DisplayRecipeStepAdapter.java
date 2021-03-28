package com.example.fyp_1.Recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_1.R;
import com.example.fyp_1.model.RecipeInstructionStep;

import java.util.ArrayList;

public class DisplayRecipeStepAdapter extends RecyclerView.Adapter<DisplayRecipeStepAdapter.ExampleViewHolder> {
    private ArrayList<RecipeInstructionStep> mStep;
    private Context mContext;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        //Recipe Step
        public TextView stepDescription;
        public TextView stepNumber;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            stepDescription = itemView.findViewById(R.id.recipe_step_description);
            stepNumber = itemView.findViewById(R.id.step_number_circle);
            
        
        }
    }
    


    public DisplayRecipeStepAdapter(Context context, ArrayList<RecipeInstructionStep> recipesSteps) {
        super();
        this.mContext = context;
        this.mStep = recipesSteps;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.step_card, parent, false);
        return new ExampleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        RecipeInstructionStep currentList = mStep.get(position);
        holder.stepDescription.setText(currentList.step);
        holder.stepNumber.setText(Integer.toString(currentList.number));
        

    }

    @Override
    public int getItemCount() {
        return mStep.size();
    }
}