package com.example.android.bake.recipes;


import android.os.Parcel;
import android.os.Parcelable;

/*********************************************
 * A POJO to hold ingredients and their      *
 * details, used in the Recipe class         *
 *********************************************/
public class Ingredient implements Parcelable {

    private double mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(double mQuantity, String mMeasure, String mName) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mName = mName;
    }

    public double getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(double mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public void setmMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getComposedIngredient() {
        return String.valueOf(mQuantity) + " " + mMeasure + " " + mName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mQuantity);
        dest.writeString(this.mMeasure);
        dest.writeString(this.mName);
    }

    protected Ingredient(Parcel in) {
        this.mQuantity = in.readDouble();
        this.mMeasure = in.readString();
        this.mName = in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
