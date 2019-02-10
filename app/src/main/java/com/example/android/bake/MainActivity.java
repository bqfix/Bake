package com.example.android.bake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.JsonUtils;

import java.util.List;

/************************************
 * The MainActivity which displays  *
 * a list of recipes generated from *
 * the baking_recipes.json asset    *
 ************************************/
public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickHandler {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String JSON_LOCATION = "baking_recipes.json";
    private List<Recipe> mRecipes;
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the list of recipes from JSON
        mRecipes = JsonUtils.parseJsonForRecipes(this, JSON_LOCATION);

        //Setup views
        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_rv);

        //RecyclerView Logic
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecipeAdapter = new RecipeAdapter(this);

        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.setRecipes(mRecipes);
    }

    @Override
    public void onItemClick(Recipe recipe) {
        //TODO Implement logic to parcel a Recipe and launch DetailActivity
        Toast.makeText(this, "Currently Not Implemented", Toast.LENGTH_SHORT).show();
    }
}
