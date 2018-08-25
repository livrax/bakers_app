package com.liviurau.bakers.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.liviurau.bakers.R;
import com.liviurau.bakers.RecipeService;
import com.liviurau.bakers.data.model.Recipe;

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private Recipe recipe;

    public static final String WIDGET_PREFERENCE = "prefs";
    public static final String WIDGET_RECIPE_KEY = "widget_recipe_key";

    public RecipeRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences prefs = mContext.getSharedPreferences(WIDGET_PREFERENCE, Context.MODE_PRIVATE);
        Integer prefsInt = prefs.getInt(WIDGET_RECIPE_KEY, 1);
        recipe = new RecipeService(mContext).getRecipe(prefsInt);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);
        rv.setTextViewText(R.id.ingredient_item, recipe.getIngredients().get(position).getIngredient());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
