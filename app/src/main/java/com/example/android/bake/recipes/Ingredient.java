package com.example.android.bake.recipes;


/*********************************************
 * A POJO to hold ingredients and their      *
 * details, used in the Recipe class         *
 *********************************************/
public class Ingredient {

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
}
