package com.example.android.bake.recipes;

import java.util.List;

/*********************************************
 * A POJO to hold recipes to be displayed in *
 * the MainActivity RecyclerView,            *
 *  and accessed in the DetailsView          *
 *********************************************/
public class Recipe {

    private final String SERVINGS_ENDING = " servings";

    private int mId;
    private String mName;
    private List<Ingredient> mIngredients;
    private List<StepInstruction> mSteps;
    private int mServings;
    private String mImage;

    public Recipe(int mId, String mName, List<Ingredient> mIngredients, List<StepInstruction> mSteps, int mServings, String mImage) {
        this.mId = mId;
        this.mName = mName;
        this.mIngredients = mIngredients;
        this.mSteps = mSteps;
        this.mServings = mServings;
        this.mImage = mImage;
    }

    public int getmId() {
        return mId;
    }

    public String getmIdString() {
        return String.valueOf(mId);
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public List<Ingredient> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<StepInstruction> getmSteps() {
        return mSteps;
    }

    public void setmSteps(List<StepInstruction> mSteps) {
        this.mSteps = mSteps;
    }

    public int getmServings() {
        return mServings;
    }

    //Altered to return a string as opposed to an int
    public String getmServingsString() {
        return String.valueOf(mServings) + SERVINGS_ENDING;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }


}
