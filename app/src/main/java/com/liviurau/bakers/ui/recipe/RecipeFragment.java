package com.liviurau.bakers.ui.recipe;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liviurau.bakers.CustomDivider;
import com.liviurau.bakers.R;
import com.liviurau.bakers.data.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.liviurau.bakers.ui.recipe.RecipeActivity.SELECTED_RECIPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    public static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView ingredientsRecyclerView;

    @BindView(R.id.stepsTitleTextView)
    TextView stepsTitleTextView;

    @BindView(R.id.stepsRecyclerView)
    RecyclerView stepsRecyclerView;

    private RecipeIngredientAdapter recipeIngredientAdapter;
    private RecipeStepAdapter recipeStepAdapter;
    private Unbinder unbinder;

    private Recipe mRecipe;

    private Parcelable savedLayoutManager;

    private LinearLayoutManager stepsLayoutManager;

    public RecipeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(SELECTED_RECIPE)) {
            mRecipe = getArguments().getParcelable(SELECTED_RECIPE);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_LAYOUT_MANAGER)) {
            savedLayoutManager = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        recipeIngredientAdapter = new RecipeIngredientAdapter(this, mRecipe.getIngredients());
        recipeStepAdapter = new RecipeStepAdapter(this, mRecipe.getSteps());

        StaggeredGridLayoutManager ingredientsLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        ingredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        ingredientsRecyclerView.setAdapter(recipeIngredientAdapter);
        ingredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        stepsLayoutManager = new LinearLayoutManager(this.getContext());
        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
        stepsRecyclerView.setAdapter(recipeStepAdapter);
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        CustomDivider dividerItemDecoration = new CustomDivider(getResources());
        stepsRecyclerView.addItemDecoration(dividerItemDecoration);

        restoreLayoutManagerPosition();

        return rootView;
    }

    private void restoreLayoutManagerPosition() {
        if (savedLayoutManager != null) {
            stepsRecyclerView.getLayoutManager().onRestoreInstanceState(savedLayoutManager);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, stepsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
