package com.liviurau.bakers;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.liviurau.bakers.ui.main.MainActivity;
import com.liviurau.bakers.ui.recipe.RecipeActivity;
import com.liviurau.bakers.ui.step.StepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class BakersTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeCard() {
        Intents.init();

        onView(withId(R.id.recipeRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasExtraWithKey(RecipeActivity.SELECTED_RECIPE));

        Intents.release();
    }

    @Test
    public void clickRecipeStep() {

        onView(withId(R.id.recipeRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Intents.init();

        onView(withId(R.id.stepsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(StepActivity.class.getName()));
        intended(hasExtraWithKey(StepActivity.CURRENT_RECIPE));
        intended(hasExtraWithKey(StepActivity.SELECTED_STEP));

        Intents.release();
    }
}
