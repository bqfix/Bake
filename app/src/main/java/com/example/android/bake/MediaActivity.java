package com.example.android.bake;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.recipes.StepInstruction;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaActivity extends AppCompatActivity {

    private static final int DEFAULT_STEP_NUMBER = 0;

    private Recipe mRecipe;
    private StepInstruction mCurrentStep;
    private SimpleExoPlayer mExoPlayer;
    private String mVideoUriString;

    private TextView mStepDescripText;
    private TextView mVideoErrorText;
    private PlayerView mExoPlayerView;


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
        mVideoErrorText = (TextView) findViewById(R.id.media_exoplayer_error);
        mStepDescripText = (TextView) findViewById(R.id.media_step_descrip);
        mExoPlayerView = (PlayerView) findViewById(R.id.media_step_exoplayer);

        //Assign view values
        mStepDescripText.setText(mCurrentStep.getmFullDescription());


        //Setup Exoplayer if needed
        mVideoUriString = mCurrentStep.getmVideoURL();
        if (mVideoUriString.isEmpty()) {
            showError();
        } else {
            initializePlayer();
        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    //Helper method for when a valid recipe is unavailable
    private void infoUnavailable() {
        finish();
        Toast.makeText(this, getString(R.string.info_unavailable_error), Toast.LENGTH_SHORT).show();
    }


    //Method to intialize ExoPlayer
    private void initializePlayer() {
        if (mExoPlayer == null) {
            //Make ExoPlayer Visible
            mVideoErrorText.setVisibility(View.GONE);
            mExoPlayerView.setVisibility(View.VISIBLE);

            //ExoPlayer Setup
            RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this,renderersFactory, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            //MediaSource Setup
            String userAgent = Util.getUserAgent(this, "Bake");
            Uri videoUri = Uri.parse(mVideoUriString);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(videoUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    //Method to release ExoPlayer
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    //Method to call when video is unavailable
    private void  showError() {
        mExoPlayerView.setVisibility(View.GONE);
        mVideoErrorText.setVisibility(View.VISIBLE);
    }
}
