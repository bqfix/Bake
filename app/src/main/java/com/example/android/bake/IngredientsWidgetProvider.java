package com.example.android.bake;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static boolean isDisplayingRecipes = false;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        //Decide which views should be displayed
        if (isDisplayingRecipes) {
            views.setViewVisibility(R.id.widget_recipes_list, View.VISIBLE);
            views.setViewVisibility(R.id.widget_ingredients_linear, View.GONE);
        } else {
            views.setViewVisibility(R.id.widget_recipes_list, View.GONE);
            views.setViewVisibility(R.id.widget_ingredients_linear, View.VISIBLE);
        }

        //Set back button functionality to display recipes
        Intent backIntent = new Intent(context, UpdateWidgetService.class);
        backIntent.setAction(UpdateWidgetService.ACTION_DISPLAY_RECIPES);
        context.startService(backIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void setIsDisplayingRecipes(boolean isDisplayingRecipes) {
        IngredientsWidgetProvider.isDisplayingRecipes = isDisplayingRecipes;
    }
}

