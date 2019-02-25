package com.example.android.bake;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.JsonUtils;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RemoteViewsFactory remoteViewsFactory;
        if (intent.hasExtra(getString(R.string.recipe_number_key))) { //Recipe number is available, create RemoteViewsFactory for ingredients
            int recipeNumber = intent.getIntExtra(getString(R.string.recipe_number_key), -1);
            remoteViewsFactory = new ListRemoteViewsFactory(this.getApplicationContext(), recipeNumber);
        } else { //Create RemoteViewsFactory for recipes
            remoteViewsFactory = new ListRemoteViewsFactory(this.getApplicationContext());
        }
        return remoteViewsFactory;
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String JSON_LOCATION = "baking_recipes.json";
    public static final int DEFAULT_RECIPE_NUMBER = -1;


    private Context mContext;
    private List<Recipe> mRecipes;
    private int mRecipeNumber = DEFAULT_RECIPE_NUMBER;

    //Constructor for Recipe List
    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    //Constructor for Ingredient List
    public ListRemoteViewsFactory(Context applicationContext, int recipeNumber) {
        mContext = applicationContext;
        mRecipeNumber = recipeNumber;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mRecipes = JsonUtils.parseJsonForRecipes(mContext, JSON_LOCATION);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        if (mRecipeNumber == DEFAULT_RECIPE_NUMBER) { //Logic for no recipe given, create recipe list
            String recipeName = mRecipes.get(position).getmName();
            views.setTextViewText(R.id.widget_list_item_text, recipeName);

            //Set onclick to launch a service that will update widget to display ingredients
            Intent intent = new Intent(mContext, UpdateWidgetService.class);
            intent.setAction(UpdateWidgetService.ACTION_DISPLAY_INGREDIENTS);
            intent.putExtra(mContext.getString(R.string.recipe_number_key), position);

            PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_list_item_text, pendingIntent);
        } else { //Recipe number given, create ingredients list
            Recipe recipe = mRecipes.get(mRecipeNumber);
            String ingredient = recipe.getmIngredients().get(position).getComposedIngredient();
            views.setTextViewText(R.id.widget_list_item_text, ingredient);
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}


