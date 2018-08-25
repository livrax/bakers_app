package com.liviurau.bakers.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import static com.liviurau.bakers.data.database.BakingContract.INVALID_RECIPE_ID;
import static com.liviurau.bakers.widget.RecipeRemoteViewsFactory.WIDGET_PREFERENCE;
import static com.liviurau.bakers.widget.RecipeRemoteViewsFactory.WIDGET_RECIPE_KEY;

public class ShowRecipeService extends IntentService {

    public static final String ACTION_SHOW_RECIPE = "action_show_recipe";
    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "action_update_recipe_widgets";
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    public ShowRecipeService() {
        super("ShowRecipeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_RECIPE.equals(action)) {
                final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                        INVALID_RECIPE_ID);
                handleActionShowRecipe(recipeId);
            } else if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                        INVALID_RECIPE_ID);
                handleActionUpdateRecipeWidgets(recipeId);
            }
        }
    }

    private void handleActionShowRecipe(int recipeId) {

        startActionUpdateRecipeWidgets(this);
    }

    public static void startActionShowRecipe(Context context, Integer recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(WIDGET_PREFERENCE, Context.MODE_PRIVATE).edit();
        prefs.putInt(WIDGET_RECIPE_KEY, recipeId);

        prefs.apply();

        Intent intent = new Intent(context, ShowRecipeService.class);
        intent.setAction(ACTION_SHOW_RECIPE);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        context.startService(intent);
    }


    public static void startActionUpdateRecipeWidgets(Context context) {
        Intent intent = new Intent(context, ShowRecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    private void handleActionUpdateRecipeWidgets(Integer recipeId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipeId ,appWidgetIds);
    }
}
