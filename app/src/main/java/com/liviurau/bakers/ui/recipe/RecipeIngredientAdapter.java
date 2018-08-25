package com.liviurau.bakers.ui.recipe;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liviurau.bakers.R;
import com.liviurau.bakers.data.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeStepViewHolder> {

    private List<Ingredient> ingredients;
    private final Context mContext;

    public RecipeIngredientAdapter(RecipeFragment recipeFragment, List<Ingredient> ingredients) {
        this.mContext = recipeFragment.getContext();
        this.ingredients = ingredients;
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ingredientTextLayout)
        TextView ingredientTextLayout;

        RecipeStepViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_layout, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        String stringBuffer = String.format("%s (%d %s)", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure());

        holder.ingredientTextLayout.setText(stringBuffer);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}
