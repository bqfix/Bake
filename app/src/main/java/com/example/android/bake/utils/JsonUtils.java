package com.example.android.bake.utils;

import android.content.Context;
import android.util.Log;

import com.example.android.bake.R;
import com.example.android.bake.recipes.Ingredient;
import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.recipes.StepInstruction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/*********************************************
 * A class to contain methods for parsing    *
 * JSON and returning usable objects         *
 *********************************************/
public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    /**
     * A method to extract a List of Recipes from a provided jsonResponse
     *
     * @param context      of the calling activity
     * @param jsonResponse of the file to be parsed
     * @return a List of Recipes
     */
    public static List<Recipe> parseJsonForRecipes(Context context, String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray recipesArray = new JSONArray(jsonResponse);
            //Iterate through, and create/add a Recipe object for each
            for (int recipesPosition = 0; recipesPosition < recipesArray.length(); recipesPosition++) {
                JSONObject currentRecipe = recipesArray.getJSONObject(recipesPosition);

                //Create each variable needed for new Recipe object
                int recipeId = currentRecipe.getInt(context.getString(R.string.recipe_id_key));
                String recipeName = currentRecipe.getString(context.getString(R.string.recipe_name_key));
                List<Ingredient> recipeIngredients = new ArrayList<Ingredient>();
                List<StepInstruction> recipeSteps = new ArrayList<StepInstruction>();
                int recipeServings = currentRecipe.getInt(context.getString(R.string.recipe_servings_key));
                String recipeImage = currentRecipe.getString(context.getString(R.string.recipe_image_key));

                //Iterate through ingredients and add to recipeIngredients
                JSONArray ingredientsArray = currentRecipe.getJSONArray(context.getString(R.string.recipe_ingredients_key));
                for (int ingredientsPosition = 0; ingredientsPosition < ingredientsArray.length(); ingredientsPosition++) {
                    JSONObject currentIngredient = ingredientsArray.getJSONObject(ingredientsPosition);

                    //Create each variable needed for new Ingredient object
                    double ingredientQuantity = currentIngredient.getDouble(context.getString(R.string.ingredients_quantity_key));
                    String ingredientMeasure = currentIngredient.getString(context.getString(R.string.ingredients_measure_key));
                    String ingredientName = currentIngredient.getString(context.getString(R.string.ingredients_name_key));

                    //Add new Ingredient
                    recipeIngredients.add(new Ingredient(ingredientQuantity, ingredientMeasure, ingredientName));
                }

                //Iterate through steps and add to recipeSteps
                JSONArray stepsArray = currentRecipe.getJSONArray(context.getString(R.string.recipe_steps_key));
                for (int stepsPosition = 0; stepsPosition < stepsArray.length(); stepsPosition++) {
                    JSONObject currentStep = stepsArray.getJSONObject(stepsPosition);

                    //Create each variable needed for new StepInstruction object
                    int stepNumber = currentStep.getInt(context.getString(R.string.step_id_key));
                    String stepShortDescrip = currentStep.getString(context.getString(R.string.step_short_descrip_key));
                    String stepFullDescrip = currentStep.getString(context.getString(R.string.step_full_descrip_key));
                    String stepVideoURL = "";

                    //Attempt to set stepVideoURL to one of the provided URLs
                    String videoURL = currentStep.getString(context.getString(R.string.step_video_key));
                    String thumbnailURL = currentStep.getString(context.getString(R.string.step_thumbnail_video_key));
                    if (!(videoURL.isEmpty())) {
                        stepVideoURL = videoURL;
                    } else if (!(thumbnailURL.isEmpty())) {
                        stepVideoURL = thumbnailURL;
                    }

                    //Add new StepInstruction
                    recipeSteps.add(new StepInstruction(stepNumber, stepShortDescrip, stepFullDescrip, stepVideoURL));
                }

                //Add new Recipe
                recipes.add(new Recipe(recipeId, recipeName, recipeIngredients, recipeSteps, recipeServings, recipeImage));
            }
        } catch (JSONException jsonException) {
            Log.e(LOG_TAG, jsonException.getMessage());
        }


        return recipes;
    }

    /**
     * Builds the URL
     *
     * @param stringUrl the url to be input
     * @return The URL to query
     */
    public static URL buildURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, exception.getMessage());
        }
        return url;
    }

    /**
     * Method to make the initial Http request
     *
     * @param url the url to be used (likely generated from buildURL method)
     * @return a string with the full JSON response to be parsed by extractStories method
     */
    public static String makeHttpRequest(URL url) {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read data if valid connection was made
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + responseCode);
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Could not connect");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException exception) {
                Log.e(LOG_TAG, "Failed to close input stream");
            }

        }
        return jsonResponse;
    }

    /**
     * Helper method to perform the reading from the input stream
     *
     * @param inputStream provided from the makeHttpRequest method
     * @return a built JSONResponse to be returned by the makeHttpRequest method
     * @throws IOException that is handled by the makeHttpRequest method which calls this method
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
