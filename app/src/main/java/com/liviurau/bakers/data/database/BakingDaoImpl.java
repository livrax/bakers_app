package com.liviurau.bakers.data.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.liviurau.bakers.data.model.Ingredient;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.data.model.Step;

import java.util.ArrayList;
import java.util.List;

public class BakingDaoImpl implements BakingDao {

    private final SQLiteDatabase db;

    public BakingDaoImpl(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        if (db == null) {
            return false;
        }

        ContentValues cv = new ContentValues();
        cv.put(BakingContract.RecipeEntry.COLUMN_ID, recipe.getId());
        cv.put(BakingContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        cv.put(BakingContract.RecipeEntry.COLUMN_INGREDIENTS, recipe.getId());
        cv.put(BakingContract.RecipeEntry.COLUMN_STEPS, recipe.getId());
        cv.put(BakingContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        cv.put(BakingContract.RecipeEntry.COLUMN_IMAGE, recipe.getImageUrl());

        try {
            db.beginTransaction();
            db.insert(BakingContract.RecipeEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean updateRecipe(Recipe recipe) {
        if (db == null) {
            return false;
        }

        ContentValues cv = new ContentValues();
        cv.put(BakingContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        cv.put(BakingContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        cv.put(BakingContract.RecipeEntry.COLUMN_IMAGE, recipe.getImageUrl());

        try {
            db.beginTransaction();
            db.update(BakingContract.RecipeEntry.TABLE_NAME, cv, BakingContract.RecipeEntry.COLUMN_ID + " = ?", new String[]{recipe.getId().toString()});
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public Recipe getRecipe(Integer recipeId) throws SQLException {
        Recipe recipe = new Recipe();

        try {
            String selectQuery = String.format("SELECT * FROM %s WHERE %s=?",
                    BakingContract.RecipeEntry.TABLE_NAME,
                    BakingContract.RecipeEntry.COLUMN_ID);
            db.beginTransaction();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(recipeId)});

            if (cursor.moveToFirst()) {
                do {
                    recipe.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_ID)));
                    recipe.setName(cursor.getString(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_NAME)));
                    recipe.setServings(cursor.getInt(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_SERVINGS)));
                    recipe.setImageUrl(cursor.getString(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_IMAGE)));
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return recipe;
    }

    @Override
    public List<Recipe> getRecipes() throws SQLException {
        if (db == null) {
            return null;
        }

        List<Recipe> recipeList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + BakingContract.RecipeEntry.TABLE_NAME;
            db.beginTransaction();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    recipeList.add(getRecipeCursor(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return recipeList;

    }

    @Override
    public boolean addIngredient(Integer recipeId, Ingredient ingredient) {
        if (db == null) {
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put(BakingContract.IngredientEntry.COLUMN_ID, ingredient.getId());
        cv.put(BakingContract.IngredientEntry.COLUMN_RECIPE, recipeId);
        cv.put(BakingContract.IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
        cv.put(BakingContract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
        cv.put(BakingContract.IngredientEntry.COLUMN_NAME, ingredient.getIngredient());

        try {
            db.beginTransaction();
            db.insert(BakingContract.IngredientEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean updateIngredient(Integer recipeId, Ingredient ingredient) {
        if (db == null) {
            return false;
        }

        ContentValues cv = new ContentValues();
        cv.put(BakingContract.IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
        cv.put(BakingContract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
        cv.put(BakingContract.IngredientEntry.COLUMN_NAME, ingredient.getIngredient());

        try {
            db.beginTransaction();
            db.update(BakingContract.RecipeEntry.TABLE_NAME, cv, BakingContract.IngredientEntry.COLUMN_ID + " = ?", new String[]{recipeId.toString()});
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Ingredient getIngredient(Integer recipeId, Integer ingredientId) throws SQLException {
        Ingredient ingredient = new Ingredient();

        try {
            String selectQuery = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                    BakingContract.IngredientEntry.TABLE_NAME,
                    BakingContract.IngredientEntry.COLUMN_RECIPE,
                    BakingContract.IngredientEntry.COLUMN_ID);
            db.beginTransaction();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(recipeId), String.valueOf(ingredientId)});

            if (cursor.moveToFirst()) {
                do {
                    ingredient.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_ID)));
                    ingredient.setRecipeId(cursor.getInt(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_RECIPE)));
                    ingredient.setQuantity(cursor.getInt(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_QUANTITY)));
                    ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_MEASURE)));
                    ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_NAME)));
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return ingredient;
    }

    @Override
    public List<Ingredient> getIngredients(Integer recipeId) throws SQLException {
        if (db == null) {
            return null;
        }

        List<Ingredient> ingredientList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + BakingContract.IngredientEntry.TABLE_NAME + " WHERE " + BakingContract.IngredientEntry.COLUMN_RECIPE + "=?";
            db.beginTransaction();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{recipeId.toString()});

            if (cursor.moveToFirst()) {
                do {
                    ingredientList.add(getIngredientCursor(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return ingredientList;
    }

    @Override
    public boolean addStep(Integer recipeId, Step step) {
        if (db == null) {
            return false;
        }

        ContentValues cv = new ContentValues();
        cv.put(BakingContract.StepEntry.COLUMN_ID, step.getId());
        cv.put(BakingContract.StepEntry.COLUMN_RECIPE, recipeId);
        cv.put(BakingContract.StepEntry.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
        cv.put(BakingContract.StepEntry.COLUMN_LONG_DESCRIPTION, step.getLongDescription());
        cv.put(BakingContract.StepEntry.COLUMN_VIDEO_URL, step.getVideoUrl());
        cv.put(BakingContract.StepEntry.COLUMN_IMAGE_URL, step.getThumbnailUrl());

        try {
            db.beginTransaction();
            db.insert(BakingContract.StepEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean updateStep(Integer recipeId, Step step) {
        if (db == null) {
            return false;
        }

        ContentValues cv = new ContentValues();
        cv.put(BakingContract.StepEntry.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
        cv.put(BakingContract.StepEntry.COLUMN_LONG_DESCRIPTION, step.getLongDescription());
        cv.put(BakingContract.StepEntry.COLUMN_VIDEO_URL, step.getVideoUrl());
        cv.put(BakingContract.StepEntry.COLUMN_IMAGE_URL, step.getThumbnailUrl());

        try {
            db.beginTransaction();
            db.update(BakingContract.RecipeEntry.TABLE_NAME, cv, BakingContract.StepEntry.COLUMN_ID + " = ?", new String[]{recipeId.toString()});
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Step getStep(Integer recipeId, Integer stepId) throws SQLException {
        Step step = new Step();

        try {
            String selectQuery = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                    BakingContract.StepEntry.TABLE_NAME,
                    BakingContract.StepEntry.COLUMN_RECIPE,
                    BakingContract.StepEntry.COLUMN_ID);
            db.beginTransaction();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(recipeId), String.valueOf(stepId)});

            if (cursor.moveToFirst()) {
                do {
                    step.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_ID)));
                    step.setRecipeId(cursor.getInt(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_RECIPE)));
                    step.setShortDescription(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_SHORT_DESCRIPTION)));
                    step.setLongDescription(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_LONG_DESCRIPTION)));
                    step.setVideoUrl(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_VIDEO_URL)));
                    step.setThumbnailUrl(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_IMAGE_URL)));
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return step;
    }

    @Override
    public List<Step> getSteps(Integer recipeId) throws SQLException {
        if (db == null) {
            return null;
        }

        List<Step> stepList = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + BakingContract.StepEntry.TABLE_NAME + " WHERE " + BakingContract.StepEntry.COLUMN_RECIPE + "=?";
            db.beginTransaction();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{recipeId.toString()});

            if (cursor.moveToFirst()) {
                do {
                    stepList.add(getStepCursor(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return stepList;
    }


    private Recipe getRecipeCursor(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_ID)));
        recipe.setName(cursor.getString(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_NAME)));
        recipe.setServings(cursor.getInt(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_SERVINGS)));
        recipe.setImageUrl(cursor.getString(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_IMAGE)));

        return recipe;
    }

    private Ingredient getIngredientCursor(Cursor cursor){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_ID)));
        ingredient.setRecipeId(cursor.getInt(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_RECIPE)));
        ingredient.setQuantity(cursor.getInt(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_QUANTITY)));
        ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_MEASURE)));
        ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_NAME)));

        return ingredient;
    }

    private Step getStepCursor(Cursor cursor){
        Step step = new Step();
        step.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_ID)));
        step.setRecipeId(cursor.getInt(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_RECIPE)));
        step.setShortDescription(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_SHORT_DESCRIPTION)));
        step.setLongDescription(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_LONG_DESCRIPTION)));
        step.setVideoUrl(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_VIDEO_URL)));
        step.setThumbnailUrl(cursor.getString(cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_IMAGE_URL)));

        return step;
    }
}
