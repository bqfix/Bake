package com.example.android.bake.recipes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*********************************************
 * A POJO to hold recipes to be displayed in *
 * the MainActivity RecyclerView,            *
 *  and accessed in the DetailsView          *
 *********************************************/
public class Recipe implements Parcelable {

    private static final String SERVINGS_ENDING = " servings";

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeList(this.mIngredients);
        dest.writeList(this.mSteps);
        dest.writeInt(this.mServings);
        dest.writeString(this.mImage);
    }

    protected Recipe(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mIngredients = new ArrayList<Ingredient>();
        in.readList(this.mIngredients, Ingredient.class.getClassLoader());
        this.mSteps = new ArrayList<StepInstruction>();
        in.readList(this.mSteps, StepInstruction.class.getClassLoader());
        this.mServings = in.readInt();
        this.mImage = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
