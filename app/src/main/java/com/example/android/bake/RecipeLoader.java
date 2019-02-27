package com.example.android.bake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.JsonUtils;

import java.net.URL;
import java.util.List;

import static com.example.android.bake.utils.JsonUtils.buildURL;
import static com.example.android.bake.utils.JsonUtils.makeHttpRequest;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {
    /**********************************************
     * A class to create a custom loader to get   *
     * data for the main activity's recycler view *
     **********************************************/

    private String mUrlString;

    public RecipeLoader(@NonNull Context context, String urlString) {
        super(context);
        this.mUrlString = urlString;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Recipe> loadInBackground() {
        if (mUrlString == null || mUrlString.length() < 1) {
            return null;
        }
        URL url = buildURL(mUrlString);
        String jsonResponse = makeHttpRequest(url);
        return JsonUtils.parseJsonForRecipes(getContext(), jsonResponse);
    }
}