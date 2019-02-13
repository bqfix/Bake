package com.example.android.bake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.recipes.StepInstruction;

public class MediaActivity extends AppCompatActivity {

    private static final int DEFAULT_STEP_NUMBER = 0;

    private Recipe mRecipe;
    private StepInstruction mCurrentStep;

    private TextView mStepDescripText;

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
            int stepNumber = startingIntent.getIntExtra(getString(R.string.step_number_key), DEFAULT_STEP_NUMBER);
            mCurrentStep = mRecipe.getmSteps().get(stepNumber);
        } else {
            infoUnavailable();
        }

        //Set activity title using the step's number
        setTitle(getString(R.string.media_activity_title_base) + mCurrentStep.getmStepNumberString());

        //Assign views
        mStepDescripText = (TextView) findViewById(R.id.media_step_descrip);

        //Assign view values
        mStepDescripText.setText(mCurrentStep.getmFullDescription());


    }



    //Helper method for when a valid recipe is unavailable
    private void infoUnavailable() {
        finish();
        Toast.makeText(this, getString(R.string.info_unavailable_error), Toast.LENGTH_SHORT).show();
    }

}
