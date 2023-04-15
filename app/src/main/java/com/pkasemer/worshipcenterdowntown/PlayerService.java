package com.pkasemer.worshipcenterdowntown;

import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.pkasemer.worshipcenterdowntown.Utils.BitmapCallback;
import com.pkasemer.worshipcenterdowntown.Utils.CacheDataSourceFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerService extends Service {

    //member
    private final IBinder serviceBinder = new ServiceBinder();

    //player
    ExoPlayer player;
    PlayerNotificationManager notificationManager;
    private WorkManager workManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private ExoPlayer.Listener exoPlayerEventListener;
    private PendingIntent pendingIntent;
    Context context;
    Bitmap track_bitmap;

    //class binder for clients
    public class ServiceBinder extends Binder {
        public PlayerService getPlayerService() {
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        context = getApplicationContext();
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //assign variables
        CacheDataSourceFactory cacheDatasourceFactory = new CacheDataSourceFactory(this, 500 * 1024 * 1024, 5 * 1024 * 1024);

        MediaSource.Factory mediaSourceFactory =
                new DefaultMediaSourceFactory(this)
                        .setDataSourceFactory(cacheDatasourceFactory);

        player = new ExoPlayer
                .Builder(getApplicationContext())
                .setMediaSourceFactory(mediaSourceFactory)
                .build();

        Intent openAppIntent = new Intent(getApplicationContext(), RootActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        //audio focus attributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build();
        workManager = WorkManager.getInstance(getApplicationContext());

        player.setAudioAttributes(audioAttributes, true); // set music attributes to the player

        player.setHandleAudioBecomingNoisy(true); // for JAKE Only ;)
//        player.setWakeMode();

        final String channelId = getResources().getString(R.string.app_name) + "Music Channel";
        final int notificationId = 1333328;


        notificationManager = new PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setNotificationListener(notificationListener)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setSmallIconResourceId(R.drawable.ic_worship_notification)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setNextActionIconResourceId(R.drawable.ic_skip_next)
                .setPreviousActionIconResourceId(R.drawable.ic_skip_previous)
                .setPauseActionIconResourceId(R.drawable.ic_pause)
                .setPlayActionIconResourceId(R.drawable.ic_play)
                .setStopActionIconResourceId(R.drawable.ic_baseline_close_24)
                .setChannelNameResourceId(R.string.app_name)
                .build();

        //set player to notification manager
        notificationManager.setPlayer(player);
        notificationManager.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationManager.setColor(ContextCompat.getColor(this, R.color.purple_500));
        notificationManager.setColorized(true);
        notificationManager.setUsePlayPauseActions(true);
        notificationManager.setUseNextActionInCompactView(true);
        notificationManager.setUsePreviousActionInCompactView(true);
        notificationManager.setUseChronometer(false);
        notificationManager.setUseRewindAction(false);
        notificationManager.setUseStopAction(false);
        notificationManager.setUseFastForwardAction(false);

        mediaSession = new MediaSessionCompat(getApplicationContext(), "MWonyaaSession");
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                super.onPause();
                player.setPlayWhenReady(false);
            }
        });
        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        notificationManager.setMediaSessionToken(mediaSession.getSessionToken());
        mediaSessionConnector.setQueueNavigator(timelineQueue());

        mediaSessionConnector.setPlayer(player);
        mediaSession.setActive(true);


        exoPlayerEventListener = new Player.Listener() {

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                MediaMetadata mediaMetadata = Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata;

                mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, getDuration())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, (String) mediaMetadata.title)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, (String) mediaMetadata.artist)
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, track_bitmap)
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, track_bitmap)
                        .build());
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
            }
        };


        player.setHandleAudioBecomingNoisy(true);
        player.addListener(exoPlayerEventListener);
        player.seekTo(0);

    }





    @Override
    public void onDestroy() {
        //release the player and free other services
        if (player.isPlaying()) player.stop();
        mediaSession.release();
        mediaSessionConnector.setPlayer(null);
        notificationManager.setPlayer(null);
        player.release();
        player = null;
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.setPlayWhenReady(true);
        return START_STICKY;
    }

    // notifcaiton listener
    PlayerNotificationManager.NotificationListener notificationListener = new PlayerNotificationManager.NotificationListener() {
        @Override
        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
            stopForeground(true);
            if (player.isPlaying()) {
                player.pause();
            }
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId, notification);
        }


    };

    //notification description adapter
    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            return Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title;
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
//            Toast.makeText(PlayerService.this, "notification", Toast.LENGTH_SHORT).show();
            return pendingIntent;
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {

            return Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist;
        }

        @Nullable
        @Override
        public CharSequence getCurrentSubText(Player player) {
            return Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artist;
        }


        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            final AtomicReference<Bitmap> imageBitmap = new AtomicReference<>();
            BitmapImage(player, new BitmapCallback() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    imageBitmap.set(bitmap);
                    callback.onBitmap(imageBitmap.get());
                }
            });
            return imageBitmap.get();
        }
    };

    private void BitmapImage(Player player, BitmapCallback callback) {
        if (player != null && player.getCurrentMediaItem() != null) {
            Uri artworkUri = player.getCurrentMediaItem().mediaMetadata.artworkUri;
            Glide.with(context)
                    .asBitmap()
                    .load(artworkUri)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.default_radio)
                            .error(R.drawable.default_radio)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            callback.onBitmapLoaded(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    private long getDuration() {
        return player.getDuration();
    }


    public TimelineQueueNavigator timelineQueue() {
        TimelineQueueNavigator timelineQueueNavigator = new TimelineQueueNavigator(mediaSession) {

            @Override
            public void onSkipToNext(Player player) {
                super.onSkipToNext(player);
                Toast.makeText(PlayerService.this, "Skip", Toast.LENGTH_SHORT).show();
            }

            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                MediaMetadata mediaMetadata = Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata;
                Bundle extras = new Bundle();
                extras.putString(MediaMetadataCompat.METADATA_KEY_TITLE, (String) mediaMetadata.title);
                extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, (String) mediaMetadata.artist);
                return new MediaDescriptionCompat.Builder()
                        .setIconBitmap(track_bitmap)
                        .setExtras(extras)
                        .build();
            }
        };

        return timelineQueueNavigator;
    }


}