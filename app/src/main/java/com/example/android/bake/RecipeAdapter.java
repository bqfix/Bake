package com.example.android.bake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bake.recipes.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/*********************************************
 * A class to create a custom adapter for    *
 * use in the main activity's recycler view  *
 *********************************************/

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    private final RecipeClickHandler mRecipeClickHandler;

    RecipeAdapter(RecipeClickHandler recipeClickHandler) {
        mRecipeClickHandler = recipeClickHandler;
    }



    //Inner ViewHolder Class
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mId;
        private TextView mName;
        private TextView mServings;
        private ImageView mPreviewImage;

        //Constructor
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mId = (TextView) itemView.findViewById(R.id.recipe_id_tv);
            mName = (TextView) itemView.findViewById(R.id.recipe_name_tv);
            mServings = (TextView) itemView.findViewById(R.id.recipe_servings_tv);
            mPreviewImage = (ImageView) itemView.findViewById(R.id.recipe_preview_iv);

            itemView.setOnClickListener(this);
        }

        //On click, pass the Recipe to the RecipeClickHandler interface
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipes.get(adapterPosition);
            mRecipeClickHandler.onItemClick(recipe);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIDForListItem = R.layout.recipe_recycler_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIDForListItem, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        Recipe currentRecipe = mRecipes.get(position);

        recipeViewHolder.mId.setText(currentRecipe.getmIdString());
        recipeViewHolder.mName.setText(currentRecipe.getmName());
        recipeViewHolder.mServings.setText(currentRecipe.getmServingsString());

        //Attempt to set image if it exists, else leave as base "image unavailable" resource
        String previewImageLink = currentRecipe.getmImage();

        if (!(previewImageLink.isEmpty())) {
            Picasso.get()
                    .load(previewImageLink)
                    .into(recipeViewHolder.mPreviewImage);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    /**
     * Helper method to set Recipe List to existing RecipeAdapter to prevent creating a new one
     *
     * @param recipes the new set of Recipes to be displayed
     */

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    //Interface to handle clicks, defined in MainActivity
    public interface RecipeClickHandler{
        void onItemClick(Recipe recipe);
    }

}
