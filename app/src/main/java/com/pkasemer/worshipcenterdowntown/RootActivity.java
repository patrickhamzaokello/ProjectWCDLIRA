package com.pkasemer.worshipcenterdowntown;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.palette.graphics.Palette;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pkasemer.worshipcenterdowntown.Apis.ApiService;
import com.pkasemer.worshipcenterdowntown.Dialogs.ShareFeature;
import com.pkasemer.worshipcenterdowntown.Dialogs.UpdateApp;
import com.pkasemer.worshipcenterdowntown.HelperClasses.SharedPrefManager;
import com.pkasemer.worshipcenterdowntown.Models.Radio;
import com.pkasemer.worshipcenterdowntown.Models.User;
import com.pkasemer.worshipcenterdowntown.Utils.PlayTracksCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RootActivity extends AppCompatActivity implements PlayTracksCallback {

    BottomNavigationView navView;
    //today
    public ExoPlayer player;

    public ScrollView playerView;
    ImageButton playerCloseBtn;
    //controls
    TextView songNameView, songArtist, progressView, durationView, playerLyrics;

    TextView homeSongNameView, viewlyricTilte,  homeArtistNameView;
    ImageButton homePlayPauseBtn,homeSkipNextBtn,homeskipPrevBtn;
    ImageView homeSongImage;
    MaterialCardView loveHMBtn, loveBtn;
    ImageButton playPauseBtn, skipPreviousBtn, skipNextBtn, shuffleModeBtn, repeatModeBtn;
    Button createStation, likeBtn;
    ProgressBar songLoaderProgress, homesongLoaderProgress;
    private static final int BUFFER_UPDATE_INTERVAL = 1000; // 1 sec
    private static final int PROGRESS_UPDATE_INTERVAL = 1000; // 1 sec
    private final int UPDATE_LYRICS_DELAY = 1000; //ms
    //wrappers
    ConstraintLayout homeControlWrapper, dialog_background;
    //artwork
    RoundedImageView artworkView;
    SeekBar seekbar;
    Button loveHMTrackBtn;
    // status bar & navigation color
    // repeat mode
    int repeatMode = 1; //  repeat one = 1 repeat all = 2, repeat off = 3;
    int shuffleMode = 1; //  shuffle on = 1 shuffle off = 2;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    SearchView searchView;
    TextView lyricsView, big_text_lyrics_view;
    //is the act. bond to the service
    boolean isBound = false;
    public static final String CURRENT_TRACK_BROADCAST = "current_track_broadcast";


    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
    NavController navController;
    boolean track_id_checker, recent_track_id_checker;
    private String m_songID = "0";
    private ApiService apiEndPoints;
    List<Radio> RadioList;

    //a broadcast to know weather the data is synced or not


    //Broadcast receiver to know the sync status
    private FirebaseAnalytics mFirebaseAnalytics;

    User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_activity);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("My new title"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide();


        //Initialize Bottom Navigation View.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMasage, new IntentFilter(getString(R.string.cartcoutAction)));


        setUpNavigation();
        initializePlayer();
        initializeViews();
        setupFirebaseAnalytics();
        bindToPlayerService();


    }


    public void switchContent(int id, Fragment fragment, String fragmentname) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
//        ft.addToBackStack(fragmentname);
        ft.commit();
    }


    public BroadcastReceiver mMasage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cartcount = intent.getStringExtra(getString(R.string.cartCount));

            if ((Integer.parseInt(cartcount)) != 0) {
                navView.getOrCreateBadge(R.id.navigation_cart).setNumber(Integer.parseInt(cartcount));
                navView.getOrCreateBadge(R.id.navigation_cart).setVisible(true);

            } else {
                navView.getOrCreateBadge(R.id.navigation_cart).clearNumber();
                navView.getOrCreateBadge(R.id.navigation_cart).setVisible(false);

            }
        }
    };


    private void setDecorFitsSystemWindows() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    private void initializePlayer() {
        playerView = findViewById(R.id.modernPlayer);
        playerView.setVisibility(View.GONE);

    }

    private void setUpNavigation() {
        //Initialize NavController.
        navView = findViewById(R.id.bottomNav_view);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.getOrCreateBadge(R.id.navigation_cart).setBackgroundColor(getResources().getColor(R.color.sweetRed));

    }




    private void bindToPlayerService() {
        onBindService();
    }







    private void setupFirebaseAnalytics() {
        userModel = SharedPrefManager.getInstance(this).getUser();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(userModel.getId()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, userModel.getFname());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Live_User");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void initializeViews() {
        playerCloseBtn = findViewById(R.id.playerCloseBtns);
        songNameView = findViewById(R.id.songNameView);
        songArtist = findViewById(R.id.songArtist);
        skipPreviousBtn = findViewById(R.id.skipPreviousBtn);
        skipNextBtn = findViewById(R.id.skipnextBtn);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        repeatModeBtn = findViewById(R.id.repeatModeBtn);
        shuffleModeBtn = findViewById(R.id.shuffleModeBtn);

        homeSongNameView = findViewById(R.id.homeSongNameView);
        playerLyrics = findViewById(R.id.playerLyrics);
        homeArtistNameView = findViewById(R.id.homeArtistNameView);
        homePlayPauseBtn = findViewById(R.id.homePlayPauseBtn);
        homeSongImage = findViewById(R.id.homeSongImage);


        songLoaderProgress = findViewById(R.id.songLoaderProgress);
        homesongLoaderProgress = findViewById(R.id.homesongLoaderProgress);

        homeControlWrapper = findViewById(R.id.homeControlWrapper);


        artworkView = findViewById(R.id.cir_artworkView);
        seekbar = findViewById(R.id.seekbar);
        progressView = findViewById(R.id.progressView);
        durationView = findViewById(R.id.durationView);
        homeskipPrevBtn = findViewById(R.id.homeskipPrevBtn);
        homeSkipNextBtn = findViewById(R.id.homeSkipNextBtn);

        // Set the default visibility of the textView to GONE
        playerLyrics.setVisibility(View.GONE);

        artworkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerLyrics.getVisibility() == View.GONE) {
                    playerLyrics.setVisibility(View.VISIBLE);
//                    artworkView.setImageResource(R.drawable.ic_hide_text);
                } else {
                    playerLyrics.setVisibility(View.GONE);
//                    artworkView.setImageResource(R.drawable.ic_show_text);
                }
            }
        });




    }







    private void onBindService() {
        // Check if the PlayerService is already running
        if (!isServiceRunning(PlayerService.class)) {
            // Start the PlayerService
            try {
                startService(new Intent(RootActivity.this, PlayerService.class));
            } catch (IllegalStateException e) {
                // Handle the exception
                Log.e("root", "Unable to start service: " + e.getMessage());
            }
        }

        try {
            // Bind the client to the PlayerService
            bindService(new Intent(RootActivity.this, PlayerService.class), playerServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to bind to service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //get the service instance
            PlayerService.ServiceBinder binder = (PlayerService.ServiceBinder) iBinder;
            player = binder.getPlayerService().player;
            isBound = true;
            // call player control method
            playerControls();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // Reset the bound flag
            isBound = false;
        }
    };

    @Override
    public void onBackPressed() {

        if (playerView.getVisibility() == View.VISIBLE) {
            exitPlayerView();
        } else {
            super.onBackPressed();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release the player
        doUnbindService();

    }

    private void doUnbindService() {
        if (isBound) {
            // Unbind the client from the PlayerService
            unbindService(playerServiceConnection);
            isBound = false;
        }

        // Check if the PlayerService is still running
        if (isServiceRunning(PlayerService.class)) {
            // Stop the PlayerService
            Intent intent = new Intent(this, PlayerService.class);
            stopService(intent);
        }
    }


    private void playerControls() {
        songNameView.setSelected(true);
        songArtist.setSelected(true);
        homeSongNameView.setSelected(true);
        homeArtistNameView.setSelected(true);
        homeSongImage.setSelected(true);
        //exit player view
        playerCloseBtn.setOnClickListener(view -> exitPlayerView());
        //open player view on home control
        homeControlWrapper.setOnClickListener(view -> showPlayerView());


        //player listener
        player.addListener(new Player.Listener() {

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                // show the playing song title
                homeControlWrapper.setVisibility(View.VISIBLE);
                songNameView.setText(mediaItem.mediaMetadata.title);
                songArtist.setText(mediaItem.mediaMetadata.artist);
                homeSongNameView.setText(mediaItem.mediaMetadata.title);
                homeArtistNameView.setText(mediaItem.mediaMetadata.artist);
                progressView.setText(getReadableTime((int) player.getCurrentPosition()));
                seekbar.setProgress((int) player.getCurrentPosition());
                seekbar.setSecondaryProgress((int) (player.getBufferedPosition()));
                seekbar.setMax((int) player.getDuration());
                durationView.setText(getReadableTime((int) player.getDuration()));
                playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                homePlayPauseBtn.setImageResource(R.drawable.ic_pause);

                // share Dialog

                // show the current artwork
                showCurrentArtwork();

                //update the progress of the current song

//                artworkView.setAnimation(loadRotation());
                songArtistClick(songArtist, mediaItem);
                SongNameClicked(songNameView, mediaItem);



                if (!player.isPlaying()) {
                    player.play();
                }

                // Firing events at specified playback positions 30 seconds 0r  30000 miliseconds
                // Counting Stream after 30 seconds of listening
                player
                        .createMessage(
                                (messageType, payload) -> {
//                                    addRecentlyPlayedThread(mediaItem);

                                })
                        .setLooper(Looper.getMainLooper())
                        .setPosition(30000)
                        .setDeleteAfterDelivery(true)
                        .send();

                currentTrackPlaying(mediaItem);


            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);

                if (playbackState == Player.STATE_IDLE) {
                    //error fix to handle trash music
                    player.prepare();
                }

                if (playbackState == Player.STATE_BUFFERING) {
                    songLoaderProgress.setVisibility(View.VISIBLE);
                    homesongLoaderProgress.setVisibility(View.VISIBLE);
                }

                if (playbackState == Player.STATE_ENDED) {
                    // Stop playback and release player resources
                    Toast.makeText(RootActivity.this, "Playback Ended", Toast.LENGTH_SHORT).show();
                }


                if (playbackState == Player.STATE_READY) {
                    // set va;ies to player view
                    songNameView.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                    homeSongNameView.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                    songArtist.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist);
                    homeArtistNameView.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist);
                    progressView.setText(getReadableTime((int) player.getCurrentPosition()));
                    durationView.setText(player.getDuration() < 0 ? "Live" : getReadableTime((int) player.getDuration()));
                    seekbar.setMax((int) player.getDuration());
                    seekbar.setProgress((int) player.getCurrentPosition());
                    seekbar.setSecondaryProgress((int) (player.getBufferedPosition()));
                    songLoaderProgress.setVisibility(View.GONE);
                    homesongLoaderProgress.setVisibility(View.GONE);

                    // share Dialog


                    playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    homePlayPauseBtn.setImageResource(R.drawable.ic_pause);

                    // show the current artwork
                    showCurrentArtwork();
                    player.setPlayWhenReady(true);
                    //update the progress of the current song
                    updateProgressHandler.post(updateProgressRunnable);
                    updateBufferHandler.post(updateBufferRunnable);
                } else {

                    playPauseBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                    homePlayPauseBtn.setImageResource(R.drawable.ic_play);
                    player.setPlayWhenReady(false);
                    //remove callbacks
                    updateBufferHandler.removeCallbacks(updateBufferRunnable);
                    updateProgressHandler.removeCallbacks(updateProgressRunnable);
                }

            }

            @Override
            public void onIsPlayingChanged(boolean isplaying) {
                Player.Listener.super.onIsPlayingChanged(isplaying);
                if (isplaying) {
                    playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    homePlayPauseBtn.setImageResource(R.drawable.ic_pause);
                } else {
                    playPauseBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                    homePlayPauseBtn.setImageResource(R.drawable.ic_play);
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                if (error.getCause().getMessage().contains("Searched too many bytes")) {
                    // Handle the specific error by skipping the current track
                    Toast.makeText(RootActivity.this, "Can't Play this Track, Skipping it", Toast.LENGTH_SHORT).show();
                    skipToNextTrack();
                }

            }

            //skip to 1s on every music transition

            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
                if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                    player.seekTo(100l);
                }

            }


        });

        // skip to next track
        skipNextBtn.setOnClickListener(view -> skipToNextTrack());
        homeSkipNextBtn.setOnClickListener(view -> skipToNextTrack());
        homeskipPrevBtn.setOnClickListener(view -> skipToPreviousTrack());
        // skip to previous track
        skipPreviousBtn.setOnClickListener(view -> skipToPreviousTrack());

        //play or pause the player
        playPauseBtn.setOnClickListener(view -> playOrPausePlayer());
        homePlayPauseBtn.setOnClickListener(view -> playOrPausePlayer());

        //seekbar listener
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressValue = seekBar.getProgress();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player.getPlaybackState() == ExoPlayer.STATE_READY) {
                    seekBar.setProgress(progressValue);
                    progressView.setText(getReadableTime(progressValue));
                    player.seekTo(progressValue);
                }
            }
        });

        repeatModeBtn.setOnClickListener(view -> {

            if (repeatMode == 1) {
                // repeat one
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
                repeatMode = 2;
                repeatModeBtn.setImageResource(R.drawable.ic_fluent_mdl2_repeat_one);
            } else if (repeatMode == 2) {
                // repeat all
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
                repeatMode = 3;
                repeatModeBtn.setImageResource(R.drawable.ic_repeat_all);

            } else if (repeatMode == 3) {
                // repeat off
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
                repeatMode = 1;
                repeatModeBtn.setImageResource(R.drawable.repeat_all_off_24_regular);

            }

        });


        shuffleModeBtn.setOnClickListener(view -> {

            if (shuffleMode == 1) {
                // shuffle on and repeat all list
                player.setShuffleModeEnabled(true);
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
                shuffleMode = 2;
                shuffleModeBtn.setImageResource(R.drawable.ic_shuffle);

            } else if (shuffleMode == 2) {
                // repeat off
                player.setShuffleModeEnabled(false);
                player.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
                shuffleMode = 1;
                shuffleModeBtn.setImageResource(R.drawable.shuffle_off);
            }

        });


    }


    private final Runnable updateBufferRunnable = new Runnable() {
        @Override
        public void run() {
            seekbar.setSecondaryProgress((int) (player.getBufferedPosition()));
            updateBufferHandler.postDelayed(updateBufferRunnable, BUFFER_UPDATE_INTERVAL);
        }
    };
    private final Runnable updateProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (player.isPlaying()) {
                progressView.setText(getReadableTime((int) player.getCurrentPosition()));
                seekbar.setProgress((int) player.getCurrentPosition());

            }
            updateProgressHandler.postDelayed(updateProgressRunnable, PROGRESS_UPDATE_INTERVAL);
        }
    };
    private final Handler updateBufferHandler = new Handler();
    private final Handler updateProgressHandler = new Handler();


    private void songArtistClick(TextView songArtist, MediaItem mediaItem) {
        //open artist fragment
        songArtist.setOnClickListener(view -> {
            exitPlayerView();
            Bundle media_bundle = mediaItem.mediaMetadata.extras;
            if (media_bundle != null) {
                String artistID = media_bundle.getString("artist_bn_id");

                if (!TextUtils.isEmpty(artistID)) {
//                    Fragment fragment = new Fragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("mArtistID", artistID);
//                    fragment.setArguments(bundle);
//                    navController.navigate(R.id.action_to_artistFragment, bundle);
                }

            }


        });
    }

    // Define a method for sharing a song







    private void SongNameClicked(TextView songName, MediaItem mediaItem) {
        //open artist fragment
        songName.setOnClickListener(view -> {
            exitPlayerView();
            Bundle media_bundle = mediaItem.mediaMetadata.extras;
            if (media_bundle != null) {
                String m_songID = media_bundle.getString("song_bn_id");

                if (!TextUtils.isEmpty(m_songID)) {
//                    Fragment fragment = new Fragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("m_songID", m_songID);
//                    fragment.setArguments(bundle);
//                    navController.navigate(R.id.action_to_songFragment, bundle);
                }

            }


        });
    }


    private void currentTrackPlaying(MediaItem mediaItem) {
        Bundle media_bundle = mediaItem.mediaMetadata.extras;
        if (media_bundle != null) {
            String R_songID = media_bundle.getString("song_bn_id");
            String R_songPath = media_bundle.getString("song_path");
            String R_artistID = media_bundle.getString("artist_bn_id");

            if (!TextUtils.isEmpty(R_songID) && !TextUtils.isEmpty(R_songPath) && !TextUtils.isEmpty(R_artistID)) {

                String title = (String) mediaItem.mediaMetadata.title;
                String artist = (String) mediaItem.mediaMetadata.artist;
                String artworkUrl = String.valueOf(mediaItem.mediaMetadata.artworkUri);

                //storing the user in shared preferences
                SharedPrefManager.getInstance(getApplicationContext()).currentTrack(Integer.parseInt(R_songID), title, artist, R_artistID, artworkUrl, R_songPath);

                Intent intent = new Intent(CURRENT_TRACK_BROADCAST);
                // on below line we are passing data to our broad cast receiver with key and value pair.
                intent.putExtra(getString(R.string.songId), R_songID);
                // on below line we are sending our broad cast with intent using broad cast manager.
                LocalBroadcastManager.getInstance(RootActivity.this).sendBroadcast(intent);
            }

        }

    }


    private void playOrPausePlayer() {
        if (player.isPlaying()) {
            player.pause();
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            homePlayPauseBtn.setImageResource(R.drawable.ic_play);
//            artworkView.clearAnimation();

        } else {
            player.play();
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            homePlayPauseBtn.setImageResource(R.drawable.ic_pause);
//            artworkView.startAnimation(loadRotation());

        }
    }

    private void skipToNextTrack() {
        if (player.isPlaying()) {
            player.seekTo(player.getCurrentPosition() + 10000);
        } else {
            Toast.makeText(this, "Media Not Playing", Toast.LENGTH_SHORT).show();
        }
    }

    private void skipToPreviousTrack() {
        if (player.isPlaying()) {
            player.seekTo(player.getCurrentPosition() - 10000);
        } else {
            Toast.makeText(this, "Media Not Playing", Toast.LENGTH_SHORT).show();
        }
    }


    private void showCurrentArtwork() {

        Glide.with(getApplicationContext())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.default_radio)
                        .error(R.drawable.default_radio))
                .load(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artworkUri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)    // cache both original & resized image
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Extract the main colors from the bitmap using Palette
                        new GeneratePaletteTask().execute(resource);
                        return false;
                    }


                })
                .transition(withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(homeSongImage);


        Glide.with(this)
                .load(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artworkUri)
                .placeholder(R.drawable.default_radio)
                .error(R.drawable.default_radio)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(artworkView);


    }


    private class GeneratePaletteTask extends AsyncTask<Drawable, Void, Palette> {
        @Override
        protected Palette doInBackground(Drawable... params) {
            Drawable resource = params[0];
            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
            return Palette.from(bitmap).generate();
        }

        @Override
        protected void onPostExecute(Palette palette) {
            int darkVibrantColor = palette.getDarkVibrantColor(RootActivity.this.getResources().getColor(R.color.modernPlayerbg));
            int dominantColor = palette.getDarkMutedColor(RootActivity.this.getResources().getColor(R.color.modernPlayerbg));
            int defaultColor = RootActivity.this.getResources().getColor(R.color.modernPlayerbg);
            int playerColor;
            if (darkVibrantColor != defaultColor) {
                playerColor = darkVibrantColor;
            } else if (dominantColor != defaultColor) {
                playerColor = dominantColor;
            } else {
                playerColor = defaultColor;
            }

            int textColor = RootActivity.this.getResources().getColor(R.color.artist_name_color);


            // Set the background color of the LinearLayout
            playerView.setBackgroundColor(darkenbgColor(playerColor));
//            layoutPlayer.setBackgroundTintList(ColorStateList.valueOf(darkenColor(playerColor)));
            playerLyrics.setBackgroundTintList(ColorStateList.valueOf(darkenColor(playerColor)));
            dialog_background.setBackgroundTintList(ColorStateList.valueOf(darkenColor(playerColor)));
            homeControlWrapper.setBackgroundColor(darkenColor(playerColor));

            // track info btns
            playerCloseBtn.setBackgroundTintList(ColorStateList.valueOf(darkenColor(playerColor)));

            //player controls
            skipPreviousBtn.setBackgroundTintList(ColorStateList.valueOf(playerColor));
            playPauseBtn.setBackgroundTintList(ColorStateList.valueOf(playerColor));
            skipNextBtn.setBackgroundTintList(ColorStateList.valueOf(playerColor));



            //text_color
            songArtist.setTextColor(darkenColor(textColor));
        }


        public int darkenColor(int color) {
            int alpha = Color.alpha(color);
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;  // decrease the value by 20%
            return Color.HSVToColor(alpha, hsv);
        }

        public int darkenbgColor(int color) {
            int alpha = Color.alpha(color);
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.6f;  // decrease the value by 40%
            return Color.HSVToColor(alpha, hsv);
        }

    }


    String getReadableTime(int duration) {
        String timerString = "";
        String secondsString;

        int hours = (int) (duration / (1000 * 60 * 60));
        int minutes = (int) (duration % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((duration % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;

        return timerString;
    }


    private void showPlayerView() {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(R.id.modernPlayer);
        View parent = (View) findViewById(android.R.id.content).getParent();
        TransitionManager.beginDelayedTransition((ViewGroup) parent.getParent(), transition);
        playerView.setVisibility(View.VISIBLE);
        closeKeyboard();
    }


    private void exitPlayerView() {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(R.id.modernPlayer);
        View parent = (View) findViewById(android.R.id.content).getParent();
        TransitionManager.beginDelayedTransition((ViewGroup) parent.getParent(), transition);
        playerView.setVisibility(View.GONE);
    }


    private void closeKeyboard() {
        // Get a reference to the InputMethodManager
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // Check if the soft keyboard is open
        if (manager.isAcceptingText()) {
            // Get a reference to the currently focused view
            View view = getCurrentFocus();
            // If there is a focused view, close the soft keyboard
            if (view != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void playablbum(List<Radio> tracks) {
        if (tracks == null || tracks.isEmpty()) {
            Toast.makeText(this, "No tracks to play", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            player.setMediaItems(getMediaItems(tracks), 0, 0);
            player.prepare();
            player.play();
        } catch (Exception e) {
            Toast.makeText(this, "Unable to play track: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void playTrack(List<Radio> tracks, int position) {
        if (tracks == null || tracks.isEmpty()) {
            Toast.makeText(this, "No tracks to play", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            player.setMediaItems(getMediaItems(tracks), position, 0);
            player.prepare();
            player.play();
        } catch (Exception e) {
            Toast.makeText(this, "Unable to play track: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shareFeatureCallback(String share_id, String name, String artist, String share_type) {
        ShareFeature shareFeature = new ShareFeature(share_id, name, artist, share_type);
        shareFeature.setCancelable(true);
        shareFeature.show(getSupportFragmentManager(), "ShareTrackGeneral");
    }

    private List<MediaItem> getMediaItems(List<Radio> albumTracks) {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Radio alb_track : albumTracks) {
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(alb_track.getPath())
                    .setMediaId(alb_track.getId())
                    .setMediaMetadata(getMetadata(alb_track))
                    .build();

            // add the media items to media items list
            mediaItems.add(mediaItem);
        }
        return mediaItems;
    }

    private MediaMetadata getMetadata(Radio track) {
        Bundle bundle = new Bundle();
        bundle.putString("artist_bn_id", track.getId());
        bundle.putString("song_bn_id", track.getId());
        bundle.putString("song_path", track.getPath());

        return new MediaMetadata.Builder()
                .setTitle(track.getTitle())
                .setArtist("WCDT Radio")
                .setArtworkUri(Uri.parse(track.getCover()))
                .setExtras(bundle)
                .build();
    }


    private void showAppUpdate(int apiVersion, boolean isUpdateCancelable) {
        int versionCode = BuildConfig.VERSION_CODE;
        Log.d("showAppUpdate: ", "versionCode: " + versionCode + ", apiVersion: " + apiVersion);
        if (isNewVersionAvailable(versionCode, apiVersion)) {
            showUpdateDialog(isUpdateCancelable);
        }
    }

    private boolean isNewVersionAvailable(int versionCode, int apiVersion) {
        return versionCode != apiVersion;
    }

    private void showUpdateDialog(boolean isCancelable) {
        UpdateApp updateApp = new UpdateApp();
        updateApp.setCancelable(isCancelable);
        if (RootActivity.this.getSupportFragmentManager() != null && !isFinishing()) {
            try {
                updateApp.show(RootActivity.this.getSupportFragmentManager(), "UpdateAppDialog");
            } catch (IllegalStateException e) {
                // Handle the exception
                Log.e("root", "Error " + e.getMessage());
            }
        }
    }
}