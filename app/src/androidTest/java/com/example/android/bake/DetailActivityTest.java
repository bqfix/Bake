package com.example.android.bake;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bake.recipes.Ingredient;
import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.recipes.StepInstruction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {
    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(DetailActivity.class, false, false);


    @Before
    public void launchActivity(){
        //Create fake data
        List<Ingredient> testIngredients = new ArrayList<>();
        testIngredients.add(new Ingredient(2, "oz", "cheese"));
        List<StepInstruction> testSteps = new ArrayList<StepInstruction>();
        testSteps.add(new StepInstruction(0, "", "", ""));
        Recipe testRecipe = new Recipe(1, "Cheese", testIngredients, testSteps, 0, "");

        //Launch
        Intent intent = new Intent();
        intent.putExtra("recipe_parcelable", testRecipe);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void onRecyclerClick_DisplaysVideo(){
        onView(withId(R.id.detail_step_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.step_exoplayer_fragment_container)).check(matches(isDisplayed()));
    }
}
