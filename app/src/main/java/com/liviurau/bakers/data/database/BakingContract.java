package com.liviurau.bakers.data.database;

import android.provider.BaseColumns;

public class BakingContract {

    public static final Integer INVALID_RECIPE_ID = -1;

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_STEPS = "steps";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_RECIPE = "recipeId";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_NAME = "name";
    }

    public static final class StepEntry implements BaseColumns {
        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_RECIPE = "recipeId";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_LONG_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_IMAGE_URL = "thumbnailURL";
    }
}
