package com.liviurau.bakers.ui.recipe;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liviurau.bakers.R;
import com.liviurau.bakers.data.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private List<Step> steps;
    private final Context mContext;

    private final OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }


    public RecipeStepAdapter(RecipeFragment recipeFragment, List<Step> steps) {
        this.mContext = recipeFragment.getContext();
        this.steps = steps;
        mCallback = (OnStepClickListener) mContext;
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.stepConstraintLayout)
        ConstraintLayout stepConstraintLayout;
        @BindView(R.id.stepPositionTextView)
        TextView positionTextView;
        @BindView(R.id.stepTextView)
        TextView stepTextView;

        RecipeStepViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            stepConstraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int listPosition = Integer.parseInt(positionTextView.getTag().toString());

            mCallback.onStepSelected(listPosition);
        }
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_layout, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        Step step = steps.get(position);

        if (step.getId() < 10){
            holder.positionTextView.setText(String.format("0%s.", step.getId().toString()));
        } else {
            holder.positionTextView.setText(String.format("%s.", step.getId().toString()));
        }

        holder.positionTextView.setTag(step.getId());
        holder.stepTextView.setText(step.getShortDescription());
        holder.stepTextView.setContentDescription(step.getRecipeId().toString());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

}
