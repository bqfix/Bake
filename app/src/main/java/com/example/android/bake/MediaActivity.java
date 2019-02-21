package com.example.android.bake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.utils.StepFragmentPagerAdapter;
import com.google.android.exoplayer2.Player;

public class MediaActivity extends AppCompatActivity implements Player.EventListener, ViewPager.OnPageChangeListener {

    private static final int DEFAULT_STEP_NUMBER = 0;

    private final String LOG_TAG = MediaActivity.class.getSimpleName();
    private Recipe mRecipe;
    private int mCurrentStepNumber;
    private FragmentPagerAdapter mFragmentPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        //Check if Recipe included in intent, finish if not
        Intent startingIntent = getIntent();
        if (startingIntent.hasExtra(getString(R.string.recipe_parcelable_key))) {
            mRecipe = startingIntent.getParcelableExtra(getString(R.string.recipe_parcelable_key));
            if (mRecipe == null) {
                infoUnavailable();
            }
        } else {
            infoUnavailable();
        }

        //Check if step number is available, and set current Step
        if (startingIntent.hasExtra(getString(R.string.step_number_key))) {
            mCurrentStepNumber = startingIntent.getIntExtra(getString(R.string.step_number_key), DEFAULT_STEP_NUMBER);
        } else {
            infoUnavailable();
        }

        //Setup fragment
        ViewPager viewPager = (ViewPager) findViewById(R.id.media_step_fragment_viewpager);
        mFragmentPagerAdapter = new StepFragmentPagerAdapter(getSupportFragmentManager(), this, mRecipe.getmSteps());

        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(mCurrentStepNumber);
    }

    //Helper method for when a valid recipe is unavailable
    private void infoUnavailable() {
        finish();
        Toast.makeText(this, getString(R.string.info_unavailable_error), Toast.LENGTH_SHORT).show();
    }

    //Overrides for OnPageChangeListener
    @Override
    public void onPageSelected(int newPageNumber) {
        //Update page title
        setTitle(getString(R.string.media_activity_title_base) + String.valueOf(newPageNumber));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
}
