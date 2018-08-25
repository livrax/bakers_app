package com.liviurau.bakers.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.liviurau.bakers.R;
import com.liviurau.bakers.RecipeService;
import com.liviurau.bakers.data.database.BakingContract;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.ui.main.MainActivity;
import com.liviurau.bakers.ui.recipe.RecipeActivity;

import static com.liviurau.bakers.widget.RecipeRemoteViewsFactory.WIDGET_PREFERENCE;
import static com.liviurau.bakers.widget.RecipeRemoteViewsFactory.WIDGET_RECIPE_KEY;

public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int recipeId, int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(WIDGET_PREFERENCE, Context.MODE_PRIVATE);
        Integer prefsInt = prefs.getInt(WIDGET_RECIPE_KEY, 1);
        Recipe recipe = new RecipeService(context).getRecipe(prefsInt);

        if (recipe != null) {

            recipeId = recipe.getId();

            Intent intent;

            if (recipeId == BakingContract.INVALID_RECIPE_ID) {
                intent = new Intent(context, MainActivity.class);
            } else {
                intent = new Intent(context, RecipeActivity.class);
                intent.putExtra(EXTRA_RECIPE_ID, recipeId);
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            views.setTextViewText(R.id.appwidget_text, recipe.getName());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_ingredients);
        }
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, Integer recipeId, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeId, appWidgetId);

        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        ShowRecipeService.startActionUpdateRecipeWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.recipe_widget
            );
            Intent intent = new Intent(context, RecipeRemoteViewsService.class);
            views.setRemoteAdapter(R.id.recipe_ingredients, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        ShowRecipeService.startActionUpdateRecipeWidgets(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }
}

