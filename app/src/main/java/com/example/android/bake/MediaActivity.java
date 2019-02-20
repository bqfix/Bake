package com.example.android.bake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bake.recipes.Recipe;
import com.example.android.bake.recipes.StepInstruction;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaActivity extends AppCompatActivity implements Player.EventListener {

    private static final int DEFAULT_STEP_NUMBER = 0;

    private final String LOG_TAG = MediaActivity.class.getSimpleName();
    private Recipe mRecipe;
    private StepInstruction mCurrentStep;
    private SimpleExoPlayer mExoPlayer;
    private String mVideoUriString;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

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

        //Get URI String for later usage when initializing ExoPlayer
        mVideoUriString = mCurrentStep.getmVideoURL();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
        initializeMediaSession();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
        initializeMediaSession();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayerAndMediaSession();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayerAndMediaSession();
    }

    //Helper method for when a valid recipe is unavailable
    private void infoUnavailable() {
        finish();
        Toast.makeText(this, getString(R.string.info_unavailable_error), Toast.LENGTH_SHORT).show();
    }


    //Method to intialize ExoPlayer
    private void initializePlayer() {
        if (mVideoUriString.isEmpty()) {
            showError();
        } else {
            if (mExoPlayer == null) {
                //Make ExoPlayer Visible
                mVideoErrorText.setVisibility(View.GONE);
                mExoPlayerView.setVisibility(View.VISIBLE);

                //ExoPlayer Setup
                RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, renderersFactory, trackSelector, loadControl);
                mExoPlayer.addListener(this);
                mExoPlayer.setPlayWhenReady(true);
                mExoPlayerView.setPlayer(mExoPlayer);

                buildMediaSource();
            }
        }
    }

    //Method to assign the media to be player to the ExoPlayer
    private void buildMediaSource() {
        String userAgent = Util.getUserAgent(this, "Bake");
        Uri videoUri = Uri.parse(mVideoUriString);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(videoUri);
        mExoPlayer.prepare(mediaSource);
    }

    //Method to release ExoPlayer
    private void releasePlayerAndMediaSession() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    //Method to call when video is unavailable
    private void showError() {
        mExoPlayerView.setVisibility(View.GONE);
        mVideoErrorText.setVisibility(View.VISIBLE);
    }

    //Method to initialize a MediaSession
    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(this, LOG_TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new StepMediaCallback());
        mMediaSession.setActive(true);
    }

    //Overrides to allow Media Session handling
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            mMediaSession.setPlaybackState(mStateBuilder.build());
        } else if((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }
    }

    private class StepMediaCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
