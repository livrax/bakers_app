package com.liviurau.bakers.data.database;

import com.liviurau.bakers.data.model.Ingredient;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.data.model.Step;

import java.util.List;

interface BakingDao {

    boolean addRecipe(Recipe recipe);
    boolean updateRecipe(Recipe recipe);
    Recipe getRecipe(Integer recipeId);
    List<Recipe> getRecipes();

    boolean addIngredient(Integer recipeId, Ingredient ingredient);
    boolean updateIngredient(Integer recipeId, Ingredient ingredient);
    Ingredient getIngredient(Integer recipeId, Integer ingredientId);
    List<Ingredient> getIngredients(Integer recipeId);

    boolean addStep(Integer recipeId, Step step);
    boolean updateStep(Integer recipeId, Step step);
    Step getStep(Integer recipeId, Integer stepId);
    List<Step> getSteps(Integer recipeId);
}
