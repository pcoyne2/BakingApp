package com.udacity.coyne.bakingapp;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.coyne.bakingapp.databinding.StepFragmentBinding;

import static android.view.View.GONE;


/**
 * Created by Patrick Coyne on 7/23/2017.
 */

public class StepsFragment extends Fragment implements ExoPlayer.EventListener{
    private static final String ARG_STEPS_ID = "step_id";
    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String TAG = "StepsFragment";


    private Steps step;
    private Recipe recipe;

    private StepFragmentBinding binding;
//    private TextView textView;
//    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
//    private ExoPlayer.EventListener exoEventListener;
    private Uri videoUri;
//    private ImageView thumbnail;
//    private ImageButton back, forward;

    private Callbacks callbacks;


    private int resumeWindow;
    private long resumePosition;

    public static StepsFragment newInstance(int recipesId, int stepsId){
        Bundle args = new Bundle();
//        args.putSerializable(ARG_RECIPE_ID, recipesId);
//        args.putSerializable(ARG_STEPS_ID, stepsId);
        args.putInt(ARG_STEPS_ID, stepsId);
        args.putInt(ARG_RECIPE_ID, recipesId);

        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recipeId = getArguments().getInt(ARG_RECIPE_ID);
        int stepId = getArguments().getInt(ARG_STEPS_ID);

        recipe = RecipesSingleton.get(getActivity()).getRecipe(recipeId);
        step = recipe.getSteps().get(stepId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.step_fragment, container, false);
        final View view = binding.getRoot();
//        textView = view.findViewById(R.id.step_text);
        binding.stepText.setText(step.getDescription());
//        thumbnail = (ImageView)view.findViewById(R.id.step_image);

//        simpleExoPlayerView = new SimpleExoPlayerView(getActivity());
//        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);

        videoUri = null;
        if(!step.getVideoURL().equals("")) {
            videoUri = Uri.parse(step.getVideoURL());
            initializeMediaSession();
            initializePlayer(videoUri);
            binding.stepImage.setVisibility(GONE);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    !getResources().getBoolean(R.bool.isTablet)) {
                binding.playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                binding.playerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                binding.backButton.setVisibility(GONE);
                binding.forwardButton.setVisibility(GONE);
            }
        }else if(!step.getThumbnailURL().equals("")){// If this were an image put imageview but also contains mp4
            if(step.getThumbnailURL().contains("mp4")){
                videoUri = Uri.parse(step.getThumbnailURL());
                initializeMediaSession();
                initializePlayer(videoUri);
                binding.stepImage.setVisibility(GONE);
            }else{
                binding.playerView.setVisibility(GONE);
                Picasso.with(getActivity()).load(step.getThumbnailURL()).into(binding.stepImage);
            }
        }else{
            binding.playerView.setVisibility(GONE);
            binding.stepImage.setVisibility(GONE);
        }

//        back = (ImageButton)view.findViewById(R.id.back_button);
//        forward = (ImageButton)view.findViewById(R.id.forward_button);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onPrevButtonClicked();
            }
        });
        binding.forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onNextButtonClicked();
            }
        });

        return view;
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "RecipeStepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if(!isVisibleToUser) {
                if (mExoPlayer != null) {
                    //Pause video when user swipes away in view pager
                    mExoPlayer.setPlayWhenReady(false);
                }
            }
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    public interface Callbacks{
        public void onNextButtonClicked();
        public void onPrevButtonClicked();
    }

//
    @Override
    public void onResume() {
        super.onResume();
//        if(videoUri != null) {
////            SetupExoPlayer();
//        }
        callbacks = (Callbacks) getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(mExoPlayer != null){
            releasePlayer();
//        }
        callbacks = null;
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if(mMediaSession!=null) {
            mMediaSession.setActive(false);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
