package com.liviurau.bakers.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.liviurau.bakers.R;
import com.liviurau.bakers.RecipeService;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.ui.step.StepActivity;
import com.liviurau.bakers.ui.step.StepFragment;
import com.liviurau.bakers.widget.ShowRecipeService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.liviurau.bakers.UiConstants.DEFAULT_POSITION;
import static com.liviurau.bakers.UiConstants.RECIPE_PREFERENCE;
import static com.liviurau.bakers.UiConstants.STEP_PREFERENCE;
import static com.liviurau.bakers.ui.step.StepActivity.CURRENT_RECIPE;
import static com.liviurau.bakers.ui.step.StepActivity.SELECTED_STEP;

public class RecipeActivity  extends AppCompatActivity implements RecipeStepAdapter.OnStepClickListener{

    public static final String SELECTED_RECIPE = "selected_recipe";

    @BindView(R.id.recipeToolbar)
    Toolbar toolbar;

    private int recipeId;
    private int stepId;
    private Recipe currentRecipe;

    private boolean mTwoPane;

    @Override
    public void onStepSelected(int position) {
        loadStep(position);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_return_light);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTwoPane = findViewById(R.id.tabletLinearLayout) != null;


        Recipe recipe = getIntent().getExtras().getParcelable(SELECTED_RECIPE);
        if (recipe != null) {
            if (recipe.getId() == DEFAULT_POSITION) {
                closeOnError();
                return;
            } else {
                currentRecipe = recipe;
                recipeId = recipe.getId();

                setTitle(currentRecipe.getName());
            }
        } else {
            SharedPreferences sharedPref1 = getPreferences(Context.MODE_PRIVATE);
            recipeId = sharedPref1.getInt(RECIPE_PREFERENCE, recipeId);
            stepId = sharedPref1.getInt(STEP_PREFERENCE, stepId);
            currentRecipe = new RecipeService(getApplicationContext()).getRecipe(recipeId);
            setTitle(currentRecipe.getName());
        }

        if (savedInstanceState == null) {
            savedPreferences(recipeId, stepId);
            loadRecipe(recipeId);

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(SELECTED_STEP, currentRecipe.getSteps().get(0));
                StepFragment stepFragment = new StepFragment();
                stepFragment.setArguments(arguments);
                addFragment(R.id.stepFrameLayout, stepFragment);
            }
        }
    }

    private void loadRecipe(Integer recipeId) {
        RecipeService service = new RecipeService(getApplicationContext());
        currentRecipe = service.getRecipe(recipeId);

        Bundle arguments = new Bundle();
        arguments.putParcelable(SELECTED_RECIPE, currentRecipe);

        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(arguments);
        replaceFragment(R.id.recipeFrameLayout, recipeFragment);
    }

    private void loadStep(int stepId){

        savedPreferences(recipeId, stepId);

        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putParcelable(SELECTED_STEP, currentRecipe.getSteps().get(stepId));
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(arguments);

            replaceFragment(R.id.stepFrameLayout, stepFragment);
        } else {
            Intent i = new Intent(getApplicationContext(), StepActivity.class);
            i.putExtra(CURRENT_RECIPE, currentRecipe);
            i.putExtra(SELECTED_STEP, stepId);
            ActivityCompat.startActivity(getApplicationContext(), i, null);
        }
    }

    @OnClick(R.id.addWidgetButton)
    public void addRecipeWidget(View view){
        ShowRecipeService.startActionShowRecipe(this, recipeId);

        Snackbar snackbar = Snackbar.make(view, R.string.refresh_widget_message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sbView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        }
        snackbar.show();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        recipeId = sharedPref.getInt(RECIPE_PREFERENCE, recipeId);
        stepId = sharedPref.getInt(STEP_PREFERENCE, stepId);
        if (savedInstanceState == null) {
            loadStep(stepId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_PREFERENCE, recipeId);
        outState.putInt(STEP_PREFERENCE, stepId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        recipeId = sharedPref.getInt(RECIPE_PREFERENCE, recipeId);
        stepId = sharedPref.getInt(STEP_PREFERENCE, stepId);
    }

    private void savedPreferences(Integer recipeId, Integer stepId) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(RECIPE_PREFERENCE, recipeId);
        editor.putInt(STEP_PREFERENCE, stepId);
        editor.apply();
    }

    private void addFragment(int containerViewId, Fragment fragment){
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    private void replaceFragment(int containerViewId, Fragment fragment){
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_missing, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
