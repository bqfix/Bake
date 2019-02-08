package com.example.android.bake.recipes;


/**************************************
 * A POJO to hold each step in        *
 * a recipe, used in the Recipe class *
 **************************************/
public class StepInstruction {

    private int mStepNumber;
    private String mShortDescription;
    private String mFullDescription;
    private String mVideoURL;

    public StepInstruction(int mStepNumber, String mShortDescription, String mFullDescription, String mVideoURL) {
        this.mStepNumber = mStepNumber;
        this.mShortDescription = mShortDescription;
        this.mFullDescription = mFullDescription;
        this.mVideoURL = mVideoURL;
    }

    public int getmStepNumber() {
        return mStepNumber;
    }

    public void setmStepNumber(int mStepNumber) {
        this.mStepNumber = mStepNumber;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getmFullDescription() {
        return mFullDescription;
    }

    public void setmFullDescription(String mFullDescription) {
        this.mFullDescription = mFullDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public void setmVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }
}
