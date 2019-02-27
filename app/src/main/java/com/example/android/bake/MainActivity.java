package com.example.android.bake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.JsonUtils;

import java.util.List;
import java.util.zip.Inflater;

/************************************
 * The MainActivity which displays  *
 * a list of recipes generated from *
 * the baking_recipes.json asset    *
 ************************************/
public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickHandler {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String JSON_LOCATION = "baking_recipes.json";
    private List<Recipe> mRecipes;
    private RecyclerView mRecipeRecycler;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        //Get the list of recipes from JSON
        mRecipes = JsonUtils.parseJsonForRecipes(this, JSON_LOCATION);

        //Setup views
        mRecipeRecycler = (RecyclerView) findViewById(R.id.recipe_rv);

        //RecyclerView Logic
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecipeAdapter = new RecipeAdapter(this);

        mRecipeRecycler.setLayoutManager(layoutManager);
        mRecipeRecycler.setAdapter(mRecipeAdapter);
        mRecipeAdapter.setRecipes(mRecipes);
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(getString(R.string.recipe_parcelable_key), recipe);
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_about) : {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }

    }
}
