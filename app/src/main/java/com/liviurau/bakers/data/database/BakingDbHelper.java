package com.liviurau.bakers.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BakingDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "baking.db";
    private static final int DATABASE_VERSION = 1;

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + BakingContract.RecipeEntry.TABLE_NAME + " (" +
                BakingContract.RecipeEntry.COLUMN_ID + " REAL, " +
                BakingContract.RecipeEntry.COLUMN_NAME + " REAL, " +
                BakingContract.RecipeEntry.COLUMN_INGREDIENTS + " REAL, " +
                BakingContract.RecipeEntry.COLUMN_STEPS + " REAL, " +
                BakingContract.RecipeEntry.COLUMN_SERVINGS + " REAL, " +
                BakingContract.RecipeEntry.COLUMN_IMAGE + " REAL" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + BakingContract.IngredientEntry.TABLE_NAME + " (" +
                BakingContract.IngredientEntry.COLUMN_ID + " REAL, " +
                BakingContract.IngredientEntry.COLUMN_RECIPE + " REAL, " +
                BakingContract.IngredientEntry.COLUMN_QUANTITY + " REAL, " +
                BakingContract.IngredientEntry.COLUMN_MEASURE + " REAL, " +
                BakingContract.IngredientEntry.COLUMN_NAME + " REAL" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);

        final String SQL_CREATE_STEP_TABLE = "CREATE TABLE " + BakingContract.StepEntry.TABLE_NAME + " (" +
                BakingContract.StepEntry.COLUMN_ID + " REAL, " +
                BakingContract.StepEntry.COLUMN_RECIPE + " REAL, " +
                BakingContract.StepEntry.COLUMN_SHORT_DESCRIPTION + " REAL, " +
                BakingContract.StepEntry.COLUMN_LONG_DESCRIPTION + " REAL, " +
                BakingContract.StepEntry.COLUMN_VIDEO_URL + " REAL, " +
                BakingContract.StepEntry.COLUMN_IMAGE_URL + " REAL" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS %s";

        sqLiteDatabase.execSQL(String.format(query, BakingContract.RecipeEntry.TABLE_NAME));
        sqLiteDatabase.execSQL(String.format(query, BakingContract.IngredientEntry.TABLE_NAME));
        sqLiteDatabase.execSQL(String.format(query, BakingContract.StepEntry.TABLE_NAME));

        onCreate(sqLiteDatabase);
    }
}
