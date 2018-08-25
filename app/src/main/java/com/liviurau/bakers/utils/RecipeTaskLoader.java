package com.liviurau.bakers.utils;


import android.content.AsyncTaskLoader;
import android.content.Context;

import com.liviurau.bakers.data.model.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RecipeTaskLoader extends AsyncTaskLoader<List<Recipe>> {
    private List<Recipe> list;

    public RecipeTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Recipe> loadInBackground() {
        URL path = NetworkUtils.buildRecipesPath();
        try {
            String response = NetworkUtils.getResponse(path);

            if (response != null) {
                list = JsonUtils.parseRecipes(response);
                return list;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        super.deliverResult(data);
    }
}
