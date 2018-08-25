package com.liviurau.bakers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.liviurau.bakers.data.database.BakingDaoImpl;
import com.liviurau.bakers.data.database.BakingDbHelper;
import com.liviurau.bakers.data.model.Ingredient;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.data.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    private List<Recipe> recipes;
    private final BakingDaoImpl bakingDao;

    public RecipeService(Context context) {
        BakingDbHelper dbHelper = new BakingDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        bakingDao = new BakingDaoImpl(db);
    }

    public void setList(List<Recipe> list) {

        recipes = bakingDao.getRecipes();
        if (recipes.size() == 0) {
            for (Recipe recipe : list) {

                insertOrUpdateRecipe(recipe);
                Recipe recipe1 = bakingDao.getRecipe(recipe.getId());

                for (Ingredient ingredient : recipe.getIngredients()) {
                    insertOrUpdateIngredient(recipe1.getId(), ingredient);
                }
                for (Step step : recipe.getSteps()) {
                    insertOrUpdateStep(recipe1.getId(), step);
                }

            }
        }
    }

    private boolean insertOrUpdateRecipe(Recipe recipe) {

        Recipe recipe1 = bakingDao.getRecipe(recipe.getId());

        if (recipe1.getId() != null) {
            if (recipe.getId().equals(recipe1.getId())) {
                return bakingDao.updateRecipe(recipe);
            }
        } else {
            return bakingDao.addRecipe(recipe);
        }

        return false;
    }

    private boolean insertOrUpdateIngredient(Integer recipeId, Ingredient ingredient) {

        Ingredient ingredient1 = bakingDao.getIngredient(recipeId, ingredient.getId());

        if (ingredient1 != null && ingredient.getId() != null && ingredient.getRecipeId() != null) {
            if (ingredient.getId().equals(ingredient1.getId()) && ingredient.getRecipeId().equals(recipeId)) {
                return bakingDao.updateIngredient(recipeId, ingredient);
            }
        } else {
            return bakingDao.addIngredient(recipeId, ingredient);
        }

        return false;
    }

    private boolean insertOrUpdateStep(Integer recipeId, Step step) {

        Step step1 = bakingDao.getStep(recipeId, step.getId());

        if (step1 != null && step.getId() != null && step.getRecipeId() != null) {
            if (step.getId().equals(step1.getId()) && step.getRecipeId().equals(recipeId)) {
                return bakingDao.updateStep(recipeId, step);
            }
        } else {
            return bakingDao.addStep(recipeId, step);
        }

        return false;
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        for (Recipe recipe : bakingDao.getRecipes()){
            recipeList.add(getRecipe(recipe.getId()));
        }

        return recipeList;
    }

    public Recipe getRecipe(Integer id) {
        Recipe recipe = bakingDao.getRecipe(id);

        List<Ingredient> ingredientList = bakingDao.getIngredients(id);
        List<Step> stepList = bakingDao.getSteps(id);

        recipe.setIngredients(ingredientList);
        recipe.setSteps(stepList);

        return recipe;
    }
}
