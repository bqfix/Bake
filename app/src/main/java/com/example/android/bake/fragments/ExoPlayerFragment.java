package com.example.android.bake.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bake.R;
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

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ExoPlayerFragment extends Fragment implements Player.EventListener {

    private final String LOG_TAG = "ExoPlayerFragment";
    private final long EXOPLAYER_POS_DEFAULT = 0;

    private String mVideoUriString;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long mExoPlayerPos;
    private boolean mExoPlayerPlayWhenReady;

    private TextView mVideoErrorText;
    private PlayerView mExoPlayerView;

    public ExoPlayerFragment() {
        //Required empty constructor
    }

    //Factory method to create a new instance of this fragment
    public static ExoPlayerFragment newInstance(Context context, String videoUriString) {
        ExoPlayerFragment fragment = new ExoPlayerFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.video_uri_parcelable_key), videoUriString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mVideoUriString = getArguments().getString(getString(R.string.video_uri_parcelable_key), "");
        }

        //Check for savedInstanceState
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.exoplayer_position_key))) {
            mExoPlayerPos = savedInstanceState.getLong(getString(R.string.exoplayer_position_key), EXOPLAYER_POS_DEFAULT);
            mExoPlayerPlayWhenReady = savedInstanceState.getBoolean(getString(R.string.exoplayer_playwhenready_key), false);
        } else {
            mExoPlayerPos = EXOPLAYER_POS_DEFAULT;
            mExoPlayerPlayWhenReady = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exoplayer, container, false);
        mVideoErrorText = (TextView) rootView.findViewById(R.id.exo_error_message);
        mExoPlayerView = (PlayerView) rootView.findViewById(R.id.media_step_exoplayer);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            initializePlayer(mVideoUriString);
            initializeMediaSession();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer(mVideoUriString);
            initializeMediaSession();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveMediaState();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayerAndMediaSession();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Check if value has changed from default, if so, state has been saved, needs to be included in Bundle
        if (mExoPlayerPos != EXOPLAYER_POS_DEFAULT) {
            outState.putLong(getString(R.string.exoplayer_position_key), mExoPlayerPos);
            outState.putBoolean(getString(R.string.exoplayer_playwhenready_key), mExoPlayerPlayWhenReady);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayerAndMediaSession();
        }
    }

    //Method to intialize ExoPlayer
    private void initializePlayer(String videoUriString) {
        //Check network status to see if videos are accessible
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (videoUriString == null || videoUriString.isEmpty() || activeNetwork == null) {
            showError();
        } else {
            if (mExoPlayer == null) {
                //Make ExoPlayer Visible
                mVideoErrorText.setVisibility(View.GONE);
                mExoPlayerView.setVisibility(View.VISIBLE);

                //ExoPlayer Setup
                RenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity());
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), renderersFactory, trackSelector, loadControl);
                mExoPlayer.addListener(this);
                mExoPlayerView.setPlayer(mExoPlayer);
                buildMediaSource(videoUriString);
                mExoPlayer.setPlayWhenReady(mExoPlayerPlayWhenReady);
                mExoPlayer.seekTo(mExoPlayerPos);
            }
        }
    }

    //Method to assign the media to be player to the ExoPlayer
    private void buildMediaSource(String videoUriString) {
        String userAgent = Util.getUserAgent(getActivity(), "Bake");
        Uri videoUri = Uri.parse(videoUriString);
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
        mMediaSession = new MediaSessionCompat(getActivity(), LOG_TAG);
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
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            mMediaSession.setPlaybackState(mStateBuilder.build());
        } else if ((playbackState == Player.STATE_READY)) {
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

    private void saveMediaState(){
        if (mExoPlayer != null) {
            mExoPlayerPos = mExoPlayer.getCurrentPosition();
            mExoPlayerPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
    }
}
