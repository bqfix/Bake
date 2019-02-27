package com.example.android.bake;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/************************************
 * The MainActivity which displays  *
 * a list of recipes generated from *
 * the baking_recipes.json asset    *
 ************************************/
public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickHandler, LoaderManager.LoaderCallbacks<List<Recipe>> {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int LOADER_ID = 77;
    private final String JSON_LOCATION = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private List<Recipe> mRecipes;
    private RecyclerView mRecipeRecycler;
    private RecipeAdapter mRecipeAdapter;
    private TextView mErrorText;
    private ProgressBar mProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        //Setup views
        mRecipeRecycler = (RecyclerView) findViewById(R.id.recipe_rv);
        mErrorText = (TextView) findViewById(R.id.error_text);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);

        //Set mErrorText to Refresh if clicked
        mErrorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkStatusAndExecute();
            }
        });

        //RecyclerView Logic
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecipeAdapter = new RecipeAdapter(this);

        mRecipeRecycler.setLayoutManager(layoutManager);
        mRecipeRecycler.setAdapter(mRecipeAdapter);

        //Attempt to restore mRecipes from saved state, else execute a network call
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.all_recipes_parcelable_key))) {
            mRecipes = savedInstanceState.getParcelableArrayList(getString(R.string.all_recipes_parcelable_key));
            setRecipesAndShow();
            Log.e(LOG_TAG, "reloaded from savedInstanceState");
        } else {
            checkNetworkStatusAndExecute();
        }
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(getString(R.string.recipe_parcelable_key), recipe);
        startActivity(detailIntent);
    }

    //Menu setup
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

    //Loader setup

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new RecipeLoader(this, JSON_LOCATION);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipes) {
        mRecipes = recipes;
        setRecipesAndShow();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }

    //Helper methods for showing results recycler view layout, progress bar, and error text
    void showResults() {
        mErrorText.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mRecipeRecycler.setVisibility(View.VISIBLE);
    }

    void showProgress() {
        mErrorText.setVisibility(View.INVISIBLE);
        mRecipeRecycler.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.VISIBLE);
    }

    void showErrorText() {
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mRecipeRecycler.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    //Helper method to check network status and initiate loader or otherwise show an error
    void checkNetworkStatusAndExecute() {
        //Check network connectivity
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            showProgress();
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            showErrorText();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipes != null) {
            outState.putParcelableArrayList(getString(R.string.all_recipes_parcelable_key), (ArrayList<Recipe>) mRecipes);
        }
        super.onSaveInstanceState(outState);
    }

    private void setRecipesAndShow(){
        mRecipeAdapter.setRecipes(mRecipes);
        showResults();
    }
}
