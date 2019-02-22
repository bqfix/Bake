package com.example.android.bake.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.bake.recipes.StepInstruction;

import java.util.List;

public class StepFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<StepInstruction> mStepInstructions;

    public StepFragmentPagerAdapter(FragmentManager fm, Context context, List<StepInstruction> stepInstructions) {
        super(fm);
        mContext = context;
        mStepInstructions = stepInstructions;
    }

    @Override
    public Fragment getItem(int position) {
        return StepFragment.newInstance(mContext, mStepInstructions.get(position), false); //ViewPager never used on tablet, should always be false if created from ViewPager
    }

    @Override
    public int getCount() {
        if (mStepInstructions == null) return 0;
        else return mStepInstructions.size();
    }
}
