package com.example.android.bake;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bake.fragments.StepFragment;
import com.example.android.bake.recipes.Ingredient;
import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.recipes.StepInstruction;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements StepAdapter.StepClickHandler {

    private ImageView mPreviewImage;
    private TextView mNameText;
    private TextView mServingsText;
    private TextView mIngredientsText;
    private RecyclerView mStepRecycler;

    private Recipe mRecipe;
    private boolean mIsTablet;
    private FragmentManager mFragmentManager;

    private StepAdapter mStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getString(R.string.detail_activity_title));

        //Check if Recipe included in intent, finish if not
        Intent startingIntent = getIntent();
        if (startingIntent.hasExtra(getString(R.string.recipe_parcelable_key))) {
            mRecipe = startingIntent.getParcelableExtra(getString(R.string.recipe_parcelable_key));
            if (mRecipe == null) {
                recipeUnavailable();
            }
        } else {
            recipeUnavailable();
        }

        //Set Activity title
        setTitle(mRecipe.getmName());

        //Assign views
        mPreviewImage = (ImageView) findViewById(R.id.detail_preview_iv);
        mNameText = (TextView) findViewById(R.id.detail_name_tv);
        mServingsText = (TextView) findViewById(R.id.detail_servings_tv);
        mIngredientsText = (TextView) findViewById(R.id.detail_ingredients_tv);
        mStepRecycler = (RecyclerView) findViewById(R.id.detail_step_rv);

        //Assign view values from provided Recipe
        String imagePreviewLink = mRecipe.getmImage();
        if (!(imagePreviewLink.isEmpty())) {
            Picasso.get().load(imagePreviewLink).into(mPreviewImage);
        }
        mNameText.setText(mRecipe.getmName());
        mServingsText.setText(mRecipe.getmServingsString());
        mIngredientsText.setText("");
        for (Ingredient ingredient : mRecipe.getmIngredients()) {
            mIngredientsText.append(ingredient.getComposedIngredient() + "\n\n");
        }

        //Check orientation
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        //Check if on tablet, by seeing if StepFragment container exists
        FrameLayout stepFragmentContainer = (FrameLayout) findViewById(R.id.detail_step_fragment_container);
        mIsTablet = stepFragmentContainer != null;
        if (mIsTablet) { //Insert fragment for first step of Recipe
            Fragment fragment = StepFragment.newInstance(this, mRecipe.getmSteps().get(0), true);
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction().replace(R.id.detail_step_fragment_container, fragment).commit();
        }

        //Recycler Setup
        LinearLayoutManager layoutManager;
        if (isLandscape || mIsTablet) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        }

        mStepAdapter = new StepAdapter(this, mIsTablet);

        mStepRecycler.setLayoutManager(layoutManager);
        mStepRecycler.setAdapter(mStepAdapter);

        mStepAdapter.setSteps(mRecipe.getmSteps());


    }

    //Helper method for when a valid recipe is unavailable
    private void recipeUnavailable() {
        finish();
        Toast.makeText(this, getString(R.string.recipe_unavailable_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(StepInstruction stepInstruction) {
        if (mIsTablet) { //Replace fragment container
            Fragment fragment = StepFragment.newInstance(this, stepInstruction, true);
            mFragmentManager.beginTransaction().replace(R.id.detail_step_fragment_container, fragment).commit();
        } else { //Open MediaActivity
            Intent mediaIntent = new Intent(DetailActivity.this, MediaActivity.class);
            mediaIntent.putExtra(getString(R.string.recipe_parcelable_key), mRecipe);
            mediaIntent.putExtra(getString(R.string.step_number_key), stepInstruction.getmStepNumber());
            startActivity(mediaIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_add_to_widget) :
                //Get ingredients, convert to String delineated by "::", save in SharedPreferences
                List<Ingredient> ingredientList = mRecipe.getmIngredients();
                StringBuilder builder = new StringBuilder("");
                for (Ingredient ingredient : ingredientList) {
                    builder.append(ingredient.getComposedIngredient())
                            .append("::");
                }
                String ingredients = builder.toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().putString(getString(R.string.ingredients_preferences_key), ingredients).apply();

                //Add Recipe name to shared preferences
                String recipeName = mRecipe.getmName();
                sharedPreferences.edit().putString(getString(R.string.recipe_name_preferences_key), recipeName).apply();

                Toast.makeText(this, getString(R.string.saved_to_widget), Toast.LENGTH_SHORT).show();

                //Update widget(s)
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
                IngredientsWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
