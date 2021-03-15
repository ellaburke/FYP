package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RecipeFragmentAdapter extends FragmentStateAdapter {

    public RecipeFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new IngredientListFragment();
            case 1:
                return new EquipmentListFragment();
            case 2:
                return new MethodFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
