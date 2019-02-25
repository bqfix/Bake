package com.example.android.bake;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class UpdateWidgetService extends IntentService {

    public static final String ACTION_DISPLAY_INGREDIENTS = "com.example.android.bake.action.display_ingredients";
    public static final String ACTION_DISPLAY_RECIPES = "com.example.android.bake.action.display_recipes";

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }


    //Methods to allow explicitly starting actions
    public static void startActionDisplayIngredients(Context context, int recipeNumber) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_DISPLAY_INGREDIENTS);
        intent.putExtra(context.getString(R.string.recipe_number_key), recipeNumber);
        context.startService(intent);
    }

    public static void startActionDisplayRecipes(Context context) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_DISPLAY_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISPLAY_INGREDIENTS.equals(action) && intent.hasExtra(getString(R.string.recipe_number_key))) {
                int recipeNumber = intent.getIntExtra(getString(R.string.recipe_number_key), ListRemoteViewsFactory.DEFAULT_RECIPE_NUMBER);
                handleActionDisplayIngredients(recipeNumber);
            } else if (ACTION_DISPLAY_RECIPES.equals(action)){
                handleActionDisplayRecipes();
            }
        }
    }

    private void handleActionDisplayIngredients(int recipeNumber){

    }

    private void handleActionDisplayRecipes(){

    }
}
