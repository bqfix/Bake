package com.example.android.bake.recipes;


import android.os.Parcel;
import android.os.Parcelable;

/**************************************
 * A POJO to hold each step in        *
 * a recipe, used in the Recipe class *
 **************************************/
public class StepInstruction implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStepNumber);
        dest.writeString(this.mShortDescription);
        dest.writeString(this.mFullDescription);
        dest.writeString(this.mVideoURL);
    }

    protected StepInstruction(Parcel in) {
        this.mStepNumber = in.readInt();
        this.mShortDescription = in.readString();
        this.mFullDescription = in.readString();
        this.mVideoURL = in.readString();
    }

    public static final Parcelable.Creator<StepInstruction> CREATOR = new Parcelable.Creator<StepInstruction>() {
        @Override
        public StepInstruction createFromParcel(Parcel source) {
            return new StepInstruction(source);
        }

        @Override
        public StepInstruction[] newArray(int size) {
            return new StepInstruction[size];
        }
    };
}
