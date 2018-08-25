package com.liviurau.bakers.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liviurau.bakers.R;
import com.liviurau.bakers.RecipeService;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.ui.recipe.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liviurau.bakers.ui.recipe.RecipeActivity.SELECTED_RECIPE;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList = new ArrayList<>();
    private final Context mContext;

    public RecipeListAdapter(Context context) {
        this.mContext = context;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.recipeCardView)
        CardView recipeCardView;
        @BindView(R.id.recipeTextView)
        TextView recipeTextView;
        @BindView(R.id.recipeDetailsTextView)
        TextView recipeDetailsTextView;

        RecipeViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            recipeCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int listPosition = Integer.parseInt(recipeTextView.getTag().toString());

            Recipe recipe = new RecipeService(view.getContext()).getRecipe(listPosition);

            Intent i = new Intent(view.getContext(), RecipeActivity.class);
            i.putExtra(SELECTED_RECIPE, recipe);
            ActivityCompat.startActivity(view.getContext(), i, null);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        String ingredients = "";
        String steps = "";
        String servings = "";

        if (recipe.getIngredients() != null) {
            ingredients = String.format(mContext.getString(R.string.ingredients_count_label), String.valueOf(recipe.getIngredients().size()));
        }
        if (recipe.getSteps() != null) {
            steps = String.format(mContext.getString(R.string.steps_count_label), String.valueOf(recipe.getSteps().size()));
        }
        if (recipe.getServings() != null) {
            servings = String.format(mContext.getString(R.string.servings_count_label), String.valueOf(recipe.getServings()));
        }

        holder.recipeTextView.setText(recipe.getName());
        holder.recipeTextView.setTag(recipe.getId());
        holder.recipeDetailsTextView.setText(String.format("%s  %s  %s", servings, ingredients, steps));

        if (!TextUtils.isEmpty(recipe.getImageUrl())) {
            Picasso.with(mContext).load(recipe.getImageUrl()).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void swapList(List<Recipe> list) {

        if (list != null) {
            this.recipeList.clear();
            this.recipeList.addAll(list);
            this.notifyDataSetChanged();
        }
    }
}
