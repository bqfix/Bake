package com.example.android.bake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bake.recipes.Ingredient;
import com.example.android.bake.recipes.Recipe;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView mPreviewImage;
    private TextView mNameText;
    private TextView mServingsText;
    private TextView mIngredientsText;
    private RecyclerView mStepRecycler;

    private Recipe mRecipe;

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


    }

    //Helper method for when a valid recipe is unavailable
    private void recipeUnavailable() {
        finish();
        Toast.makeText(this, getString(R.string.recipe_unavailable_error), Toast.LENGTH_SHORT).show();
    }
}
