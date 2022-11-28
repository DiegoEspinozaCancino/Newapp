package com.example.newapp.ui.music;

import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.newapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.Objects;

public class MPlayerService extends Service {
    private final IBinder serviceBinder = new ServiceBinder();
    ExoPlayer player;
    PlayerNotificationManager notificationManager;



    public class ServiceBinder extends Binder {
        public MPlayerService getPlayerService(){
            return MPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
                            //SE ASIGNAN VARIABLES
        player = new ExoPlayer.Builder(getApplicationContext()).build();
                            //ATRIBUTOS AL FOCO DEL AUDIO
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes, true); // SETEA LOS ATRIBUTOS AL REPRODUCTOR

                                                    //ADMINISTRADOR DE NOTIFICACIONES
        final String channelId = getResources().getString(R.string.app_name) + "Music Channel";
        final int notificationId = 1111111;
        notificationManager = new PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setNotificationListener(notificationListener)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setSmallIconResourceId(R.drawable.ic_ioled_notification)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setNextActionIconResourceId(R.drawable.ic_skip_next)
                .setPreviousActionIconResourceId(R.drawable.ic_previous)
                .setPauseActionIconResourceId(R.drawable.ic_pause)
                .setPlayActionIconResourceId(R.drawable.ic_play)
                .setChannelNameResourceId(R.string.app_name)
                .build();
                                                //SETEA AL GESTOR DE NOTIFICACIONES EL REPRODUCTOR
        notificationManager.setPlayer(player);
        notificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
        notificationManager.setUseRewindAction(false);
        notificationManager.setUseFastForwardAction(false);
    }

    @Override
    public void onDestroy() {
                            //SUELTA EL REPRODUCTOR
        if(player.isPlaying()) player.stop();
        notificationManager.setPlayer(null);
        player.release();
        player = null;
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    //LISTENER DE NOTIFICACION
    PlayerNotificationManager.NotificationListener notificationListener = new PlayerNotificationManager.NotificationListener() {
     @Override
     public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
     PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
     stopForeground(true);
     if(player.isPlaying())
         player.pause();
      }
      @Override
      public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing){
          PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId, notification);
      }
      };

                                                //ADAPTER QUE DESCRIBE LA NOTIFICACION
    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            return Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title;
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            Intent openAppIntent = new Intent(getApplicationContext(), MusicPlayerActivity.class);

            return PendingIntent.getActivity(getApplicationContext(), 0,openAppIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return null;
        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            //SE TRATA DE CREAR UNA VISTA DE IMAGEN EN EL "FLY" CUANDO ESTA SE ILUSTRA
            ImageView view = new ImageView(getApplicationContext());
            view.setImageURI(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artworkUri);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getDrawable();
            if(bitmapDrawable == null){
                bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.cascos);
            }
            assert bitmapDrawable != null;
            return bitmapDrawable.getBitmap();
        }
    };
}