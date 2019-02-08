package com.example.android.bake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.JsonUtils;

import java.util.List;

/************************************
 * The MainActivity which displays  *
 * a list of recipes generated from *
 * the baking_recipes.json asset    *
 ************************************/
public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String JSON_LOCATION = "baking_recipes.json";
    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecipes = JsonUtils.parseJsonForRecipes(this, JSON_LOCATION);

        Log.e(LOG_TAG, mRecipes.toString());
    }
}
