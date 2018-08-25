package com.liviurau.bakers.utils;

import com.liviurau.bakers.data.model.Ingredient;
import com.liviurau.bakers.data.model.Recipe;
import com.liviurau.bakers.data.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liviu Rau on 17-Feb-18.
 */

class JsonUtils {

    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_STEPS = "steps";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";

    private static final String INGREDIENT_QUANTITY = "quantity";
    private static final String INGREDIENT_MEASURE = "measure";
    private static final String INGREDIENT_NAME = "ingredient";

    private static final String STEP_ID = "id";
    private static final String STEP_SHORT_DESCRIPTION = "shortDescription";
    private static final String STEP_LONG_DESCRIPTION = "description";
    private static final String STEP_VIDEO_URL = "videoURL";
    private static final String STEP_IMAGE_URL = "thumbnailURL";

    public static ArrayList<Recipe> parseRecipes(String param) {

        ArrayList<Recipe> list = new ArrayList<>();

        try {
            JSONArray results = new JSONArray(param);

            for (int i = 0; i < results.length(); i++) {
                JSONObject recipeInfo = results.optJSONObject(i);
                Recipe recipe = new Recipe();
                recipe.setId(recipeInfo.optInt(RECIPE_ID));
                recipe.setName(recipeInfo.optString(RECIPE_NAME));

                JSONArray ingredients = recipeInfo.optJSONArray(RECIPE_INGREDIENTS);
                recipe.setIngredients(jsonToIngredients(ingredients));

                JSONArray steps = recipeInfo.optJSONArray(RECIPE_STEPS);
                recipe.setSteps(jsonToSteps(steps));

                recipe.setServings(recipeInfo.optInt(RECIPE_SERVINGS));
                recipe.setImageUrl(recipeInfo.optString(RECIPE_IMAGE));

                list.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static List<Step> jsonToSteps(JSONArray jsonArray) {
        List<Step> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            Step step = new Step();
            step.setId(jsonObject.optInt(STEP_ID));
            step.setShortDescription(jsonObject.optString(STEP_SHORT_DESCRIPTION));
            step.setLongDescription(jsonObject.optString(STEP_LONG_DESCRIPTION));
            step.setVideoUrl(jsonObject.optString(STEP_VIDEO_URL));
            step.setThumbnailUrl(jsonObject.optString(STEP_IMAGE_URL));

            list.add(step);
        }

        return list;
    }

    private static List<Ingredient> jsonToIngredients(JSONArray jsonArray) {
        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(jsonObject.optInt(INGREDIENT_QUANTITY));
            ingredient.setMeasure(jsonObject.optString(INGREDIENT_MEASURE));
            ingredient.setIngredient(jsonObject.optString(INGREDIENT_NAME));

            list.add(ingredient);
        }

        return list;
    }

}
