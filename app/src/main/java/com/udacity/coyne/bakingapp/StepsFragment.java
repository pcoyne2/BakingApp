package com.udacity.coyne.bakingapp;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import static android.view.View.GONE;


/**
 * Created by Patrick Coyne on 7/23/2017.
 */

public class StepsFragment extends Fragment {
    private static final String ARG_STEPS_ID = "step_id";
    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String TAG = "StepsFragment";


    private Steps step;
    private Recipe recipe;

    private TextView textView;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ExoPlayer.EventListener exoEventListener;
    private Uri videoUri;
    private ImageView thumbnail;

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
        View view = inflater.inflate(R.layout.step_fragment, container, false);
        textView = view.findViewById(R.id.step_text);
        textView.setText(step.getDescription()+" "+step.getThumbnailURL() + " "+step.getVideoURL());
        thumbnail = (ImageView)view.findViewById(R.id.step_image);

        simpleExoPlayerView = new SimpleExoPlayerView(getActivity());
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);

        videoUri = null;
        if(!step.getVideoURL().equals("")) {
            videoUri = Uri.parse(step.getVideoURL());
            thumbnail.setVisibility(GONE);
        }else if(!step.getThumbnailURL().equals("")){// If this were an image put imageview but also contains mp4
            if(step.getThumbnailURL().contains("mp4")){
                videoUri = Uri.parse(step.getThumbnailURL());
                thumbnail.setVisibility(GONE);
            }else{
                simpleExoPlayerView.setVisibility(GONE);
                Picasso.with(getActivity()).load(step.getThumbnailURL()).into(thumbnail);
            }
        }else{
            simpleExoPlayerView.setVisibility(GONE);
            thumbnail.setVisibility(GONE);
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
//            if(videoUri != null) {
//                SetupExoPlayer();
//            }
        }else{
            StopExoPlayer();
        }
    }

    private void SetupExoPlayer(){
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

        //Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "exoplayer2example"), bandwidthMeterA);
//Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        final MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
        player.prepare(mediaSource);
        //loopingSource);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.v(TAG,"Listener-onTimelineChanged...");

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG,"Listener-onTracksChanged...");

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG,"Listener-onLoadingChanged...");

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG,"Listener-onPlayerStateChanged...");

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG,"Listener-onPlayerError...");
                player.stop();
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);

            }

            @Override
            public void onPositionDiscontinuity() {
                Log.v(TAG,"Listener-onPositionDiscontinuity...");

            }
        });

        player.setPlayWhenReady(false); //run file/link when ready to play.


    }

    public void StopExoPlayer(){
        if(player != null){
            player.stop();
        }
    }
//
    @Override
    public void onResume() {
        super.onResume();
        if(videoUri != null) {
            SetupExoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player != null){
            player.release();
        }
    }
}
