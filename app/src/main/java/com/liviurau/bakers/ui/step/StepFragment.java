package com.liviurau.bakers.ui.step;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
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
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.liviurau.bakers.R;
import com.liviurau.bakers.data.model.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.liviurau.bakers.UiConstants.PLAYER_PLAY_PAUSE;
import static com.liviurau.bakers.UiConstants.PLAYER_POSITION;
import static com.liviurau.bakers.ui.step.StepActivity.SELECTED_STEP;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {

    @BindView(R.id.stepDescriptionContainer)
    NestedScrollView stepDescriptionContainer;

    @BindView(R.id.simpleExoPlayerView)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.stepImageView)
    ImageView stepImageView;

    @BindView(R.id.stepDescriptionTextView)
    TextView stepDescriptionTextView;

    private SimpleExoPlayer mExoPlayer;
    private Step mStep;
    private Unbinder unbinder;
    private long currentPosition = 0;

    private boolean playerReady = true;

    public StepFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(SELECTED_STEP)) {
            mStep = getArguments().getParcelable(SELECTED_STEP);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(PLAYER_POSITION)) {
            currentPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playerReady = savedInstanceState.getBoolean(PLAYER_PLAY_PAUSE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        stepDescriptionTextView.setText(mStep.getLongDescription());

        if (!TextUtils.isEmpty(mStep.getThumbnailUrl())) {
            Picasso.with(this.getContext()).load(mStep.getThumbnailUrl()).into(stepImageView);
            stepImageView.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mStep.getVideoUrl())) {
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        } else {
            stepDescriptionContainer.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(this.getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);

            if (currentPosition != 0)
                mExoPlayer.seekTo(currentPosition);

            mExoPlayer.setPlayWhenReady(playerReady);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mExoPlayer != null) {
            outState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_PLAY_PAUSE, mExoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {

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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
