package com.example.newapp.ui.music;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chibde.visualizer.BarVisualizer;
import com.example.newapp.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MusicPlayerActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    SongAdapter songAdapter;
    List<Song> allSongs = new ArrayList<>();
    ActivityResultLauncher<String> storagePermissionLauncher;
    final String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private Toolbar mptoolbar;

    ExoPlayer player;
    ActivityResultLauncher<String> recordAudioPermissionLauncher;
    final String recordAudioPermission = Manifest.permission.RECORD_AUDIO; //ESTARA ACCEDIENDO EN EL SONGADAPTER
    ConstraintLayout playerView;
    TextView playerCloseBtn;
                                            //CONTROLES
    TextView songNameView, skipPreviousBtn, skipNextBtn, playPauseBtn, repeatModeBtn, playlistBtn;
    TextView  homeSongNameView, homeSkipPreviousBtn, homePlayPauseBtn, homeSkipNextBtn;
                                            //WRAPPERS
    ConstraintLayout homeControlWrapper, headWrapper, artworkWrapper, seekbarWrapper, controlWrapper, audioVisualizerWrapper;
                                            //ARTWORK
    CircleImageView artworkView;
                                            //SEEKBAR
    SeekBar seekBar;
    TextView progressView, durationView;
                                            //AUDIO VISUALIZER
    BarVisualizer audioVisualizer;
                                            //BLUR IMAGE VIEW
    BlurImageView blurImageView;
                                            //ESTADO DE LA BARRA Y COLOR DE NAVEGACION
    int defaultStatusColor;
                                            //MODO REPETICION
    int repeatMode = 1;                     //REPETIR AL COMPLETO =1, REPETIR UNA VEZ =2, MODO ALEATORIO =3
    SearchView searchView;
    //ESTA LA ACTIVITY ENLAZADA?
    boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        //GUARDAR EL COLOR DEL ESTADO
        defaultStatusColor = getWindow().getStatusBarColor();

        //SETEA EL COLOR DE LA NAVEGACION
        getWindow().setNavigationBarColor(ColorUtils.setAlphaComponent(defaultStatusColor, 199)); //0 & 255

            //SETEA EL TOOLBAR Y EL TITULO DE LA APP
        mptoolbar = (Toolbar) findViewById(R.id.mptoolbar);
        setSupportActionBar(mptoolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.app_name));

            //RECYCLERVIEW
        recyclerview = findViewById(R.id.mprecyclerView);
        storagePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted->{
            if(granted){
                fetchSongs();
            }
            else{
                userResponses();
            }
        });
        storagePermissionLauncher.launch(permission);

        //PERMISOS DE GRABACION DE AUDIO
        recordAudioPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted->{
            if(granted && player.isPlaying()){
                activateAudioVisualizer();
            }
            else{
                userResponsesOnRecordAudioPerm();
            }
        });


                                                                            //VISTAS

       // player = new ExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.playerView);
        playerCloseBtn = findViewById(R.id.playerCloseBtn);
        songNameView = findViewById(R.id.songNameView);
        skipPreviousBtn = findViewById(R.id.skipPreviousBtn);
        skipNextBtn = findViewById(R.id.skipNextBtn);
        playPauseBtn = findViewById(R.id.playpauseBtn);
        repeatModeBtn = findViewById(R.id.repeatModeBtn);
        playlistBtn = findViewById(R.id.playlistBtn);

        homeSongNameView = findViewById(R.id.homeSongNameView);
        homeSkipPreviousBtn = findViewById(R.id.homeSkipPreviousBtn);
        homeSkipNextBtn = findViewById(R.id.homeSkipNextBtn);
        homePlayPauseBtn = findViewById(R.id.homePlayPauseBtn);

                                                                    //WRAPPERS
        homeControlWrapper = findViewById(R.id.homeControlWrapper);
        headWrapper = findViewById(R.id.headWrapper);
        artworkWrapper = findViewById(R.id.artworkWrapper);
        seekbarWrapper = findViewById(R.id.seekbarWrapper);
        controlWrapper = findViewById(R.id.controlWrapper);
        audioVisualizerWrapper = findViewById(R.id.audioVisualizerWrapper);

                                                                    //ARTWORK
        artworkView = findViewById(R.id.artworkView);

                                                                    //BARRA DE BUSQUEDA
        seekBar = findViewById(R.id.seekbar);
        progressView = findViewById(R.id.progressView);
        durationView = findViewById(R.id.durationView);

                                                                    //VISUALIZADOR DE AUDIO
        audioVisualizer = findViewById(R.id.visualizer);

                                                                    //BLUR IMAGE VIEW
        blurImageView = findViewById(R.id.blurImageView);
                                                                //LANZA EL PERMISO DE ALMACENAMIENTO EN EL ONCREATE
        //storagePermissionLauncher.launch(permission);
                                                                    //METODO PARA CONTROLAR EL REPRODUCTOR
        //playerControls();

                                                    //ENLAZAMOS EL SERVICIO DE REPRODUCCION, PARA HACER CUALQUIER COSA DESPUES DE ENLAZARLO
        doBindService();
    }

    private void doBindService() {
        Intent playerServiceIntent = new Intent(this, MPlayerService.class);
        bindService(playerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
    }
    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //OBTIENE LA INSTANCIA DE L SERVICIO
            MPlayerService.ServiceBinder binder = (MPlayerService.ServiceBinder) iBinder;
            player = binder.getPlayerService().player;
            isBound = true;
            //LISTO PARA MOSTRAR CANCIONES
            storagePermissionLauncher.launch(permission);
            //LLAMA LOS METODOS QUE CONTROLAN AL REPRODUCTOR
            playerControls();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onBackPressed() {
            //SI DECIMOS QUE LA VISTA DEL REPRODUCTOR ESTA VISIBLE, SE CIERRA
        if(playerView.getVisibility() == View.VISIBLE) {
            exitPlayerView();
        }else {
            super.onBackPressed();
        }
    }

    private void playerControls() {
                            //MARQUEE DEL NOMBRE DE LA CANCION
        songNameView.setSelected(true);
        homeSongNameView.setSelected(true);
                            //SALIR DE LA VISTA DEL REPRODUCTOR
        playerCloseBtn.setOnClickListener(view -> exitPlayerView());
        playlistBtn.setOnClickListener(view -> exitPlayerView());
                            //ABRE LA VISTA DEL REPRODUCTOR DESDE EL HOME WRAPPER CLICK
        homeControlWrapper.setOnClickListener(view -> showPlayerView());
                            //LISTENER DEL REPRODUCTOR
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                //MUESTRA EL TITULO DE LA MUSICA EN REPRODUCCION
                songNameView.setText(mediaItem.mediaMetadata.title);
                homeSongNameView.setText(mediaItem.mediaMetadata.title);

                progressView.setText(getReadableTime((int)player.getCurrentPosition()));
                seekBar.setProgress((int)player.getCurrentPosition());
                seekBar.setMax((int)player.getDuration());
                durationView.setText(getReadableTime((int)player.getDuration()));
                playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle, 0, 0, 0);
                homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0,0,0);

                                                            //METODO PARA MOSTRAR EL ARTWORK DEL ALBUM
                showCurrentArtwork();
                                                            //ACTUALIZAR EL LA POSICION DEL PROGRESO DE UNA CANCION EN REPRODUCCION
                updatePlayerPositionProgress();
                                                            //CARGAR LA ANIMACION DEL ARTWORK
                artworkView.setAnimation(loadRotation());
                                                            //SET EL VISUALIZADOR DE AUDIO
                activateAudioVisualizer();
                                                            //ACTUALIZAR LA VISTA DE COLORES DEL REPRODUCTOR
                updatePlayerColors();

                if(!player.isPlaying()){
                    player.play();

                }
            }
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState == ExoPlayer.STATE_READY){
                    songNameView.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                    homeSongNameView.setText(player.getCurrentMediaItem().mediaMetadata.title);
                    progressView.setText(getReadableTime((int)player.getCurrentPosition()));
                    durationView.setText(getReadableTime((int)player.getDuration()));
                    seekBar.setMax((int)player.getDuration());
                    seekBar.setProgress((int)player.getCurrentPosition());

                    playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle, 0, 0, 0);
                    homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0,0,0);


                    //METODO PARA MOSTRAR EL ARTWORK DEL ALBUM
                    showCurrentArtwork();
                    //ACTUALIZAR EL LA POSICION DEL PROGRESO DE UNA CANCION EN REPRODUCCION
                    updatePlayerPositionProgress();
                    //CARGAR LA ANIMACION DEL ARTWORK
                    artworkView.setAnimation(loadRotation());
                    //SET EL VISUALIZADOR DE AUDIO
                    activateAudioVisualizer();
                    //ACTUALIZAR LA VISTA DE COLORES DEL REPRODUCTOR
                    updatePlayerColors();
                }
                else{
                    playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_circle,0,0,0);
                    homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play,0,0,0);
                }
            }
        });
                                                    //SALTAR A LA SIGUENTE CANCION
        skipNextBtn.setOnClickListener(view -> skipToNextSong());
        homeSkipNextBtn.setOnClickListener(view -> skipToNextSong());

                                                    //SALTAR A LA CANCION ANTERIOR
        skipPreviousBtn.setOnClickListener(view -> skipToPreviousSong());
        homeSkipPreviousBtn.setOnClickListener(view -> skipToPreviousSong());

                                                    //PAUSAR Y REPRODUCIR EL REPRODUCTOR DE MUSICA
        playPauseBtn.setOnClickListener(view -> playOrPausePlayer());
        homePlayPauseBtn.setOnClickListener(view -> playOrPausePlayer());

                                                    //BARRA DE BUSQUEDA LISTENER
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                if(player.getPlaybackState() == ExoPlayer.STATE_READY){
                    seekBar.setProgress(progressValue);
                    progressView.setText(getReadableTime(progressValue));
                    player.seekTo(progressValue);
                }
            }
        });
                                                //MODO REPETICION
        repeatModeBtn.setOnClickListener(view ->{
         if(repeatMode == 1){
                                                //REPETIR UNA VEZ
             player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
             repeatMode = 2;
             repeatModeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_repeat_one,0,0,0);
         }
         else if (repeatMode == 2){
                                                //MODO ALEATORIO
             player.setShuffleModeEnabled(true);
             player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
             repeatMode = 3;
             repeatModeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shuffle,0,0,0);

         }
         else if(repeatMode == 3){
                                                //REPETIR TO.DO
             player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
             player.setShuffleModeEnabled(false);
             repeatMode = 1;
             repeatModeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic__repeat,0,0,0);

         }
                                            //ACTUALIZAR COLORES
            updatePlayerColors();

        });

    }

    private void playOrPausePlayer() {
        if(player.isPlaying()){
            player.pause();
            playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_circle,0,0,0);
            homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play,0,0,0);
            artworkView.clearAnimation();
        }
        else{
            player.play();
            playPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle,0,0,0);
            homePlayPauseBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause,0,0,0);
            artworkView.startAnimation(loadRotation());
        }
        //ACTUALIZAR LOS COLORES DEL REPRODUCTOR
        updatePlayerColors();
    }

    private void skipToPreviousSong() {
        if(player.hasPreviousMediaItem()){
            player.seekToPrevious();
        }
    }
    private void skipToNextSong() {
        if(player.hasNextMediaItem()) {
            player.seekToNext();
        }
    }

        private Animation loadRotation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }

    private void updatePlayerPositionProgress() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(player.isPlaying()){
                    progressView.setText(getReadableTime((int)player.getCurrentPosition()));
                    seekBar.setProgress((int)player.getCurrentPosition());
                }
                                                    //REPETIR EL LLAMADO DEL METODO
                updatePlayerPositionProgress();
            }
        },1000);
    }

    private void showCurrentArtwork() {
        artworkView.setImageURI(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artworkUri);

        if(artworkView.getDrawable() == null){
            artworkView.setImageResource(R.drawable.cascos);
        }
    }

    String getReadableTime(int duration) {
        String time;

        int hrs = duration/(1000*60*60);
        int min = (duration%(1000*60*60))/(1000*60);
        int secs = (((duration%(1000*60*60))%(1000*60*60))%(1000*600))/1000;

        if(hrs <1){
            time = min +":"+secs;
        }
        else{
            time = hrs +":"+ min +":"+secs;
        }
        return time;
    }

    private void updatePlayerColors() {
        // SOLO SI LA VISTA DEL REPRODUCTOR ESTA VISIBLE
        if(playerView.getVisibility() == View.GONE){
            return;
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable) artworkView.getDrawable();
        if(bitmapDrawable == null){
            bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.cascos);
        }
        assert bitmapDrawable != null;
        Bitmap bmp = bitmapDrawable.getBitmap();
                            //SETEA EL BITMAP AL BLUR DE VISTA DE IMAGEN
        blurImageView.setImageBitmap(bmp);
        blurImageView.setBlur(4);

                            //CONTROL DE COLORES DEL REPRODUCTOR
        Palette.from(bmp).generate(palette -> {
            if(palette != null){
                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                if(swatch == null){
                    swatch = palette.getMutedSwatch();
                    if (swatch == null){
                        swatch = palette.getDominantSwatch();
                    }
                }
                //EXTRAER LOS COLORES AL TEXTO
                int tittleTextColor = swatch.getTitleTextColor();
                int bodyTextColor = swatch.getBodyTextColor();
                int rgbColor = swatch.getRgb();

                //SETEAR LOS COLORES A LAS VISTAS DEL REPRODUCTOR
                // COLORES EN LA BARRA DE ESTADO Y NAVEGACION
                getWindow().setStatusBarColor(rgbColor);
                getWindow().setNavigationBarColor(rgbColor);

                //MAS COLORES A LA VISTA
                songNameView.setTextColor(tittleTextColor);
                playerCloseBtn.getCompoundDrawables()[0].setTint(tittleTextColor);
                progressView.setTextColor(bodyTextColor);
                durationView.setTextColor(bodyTextColor);

                repeatModeBtn.getCompoundDrawables()[0].setTint(bodyTextColor);
                skipPreviousBtn.getCompoundDrawables()[0].setTint(bodyTextColor);
                skipNextBtn.getCompoundDrawables()[0].setTint(bodyTextColor);
                playPauseBtn.getCompoundDrawables()[0].setTint(tittleTextColor);
                playlistBtn.getCompoundDrawables()[0].setTint(bodyTextColor);


            }
        });
    }

    private void showPlayerView() {
        playerView.setVisibility(View.VISIBLE);
        updatePlayerColors();
    }

    private void exitPlayerView() {
        playerView.setVisibility(View.GONE);
        getWindow().setStatusBarColor(defaultStatusColor);
        getWindow().setNavigationBarColor(ColorUtils.setAlphaComponent(defaultStatusColor, 199)); //ENTRE 0 y 255
    }

    private void userResponsesOnRecordAudioPerm() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(recordAudioPermission)){
                                            //MUESTRA UNA UI EDUCACIONAL(¿?), EXPLICANDO EL POR QUÉ NECESITAMOS ESTE PERMISO :0
                                            // USO DE DIALOGOS DE ALERTA
                new AlertDialog.Builder(this)
                        .setTitle("Solicitando la muestra del visualizador de audio")
                        .setMessage("Permite que esta aplicacion muestre un visualizador de audio cuando la musica esta en reproduccion")
                        .setPositiveButton("permiso", (dialogInterface, i) -> {

                                                                //SOLICITANDO DEL PERMISO
                            recordAudioPermissionLauncher.launch(recordAudioPermission);
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> {
                            Toast.makeText(getApplicationContext(), "Se ha negado mostrar el visualizador de audio", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Se ha negado mostrar el visualizador de audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //AUDIO VISUALIZER
    private void activateAudioVisualizer() {
         //VERIFICA SI TENEMOS LOS PERMISOS PARA GRABAR AUDIO PARA MOSTRAR EL VISUALIZADOR DE AUDIO
        if(ContextCompat.checkSelfPermission(this, recordAudioPermission) != PackageManager.PERMISSION_GRANTED);{
            return;
        }
        /**
         //SETEA EL COLOR AL VISUALIZADOR
        audioVisualizer.setColor(ContextCompat.getColor(this, R.color.secondary_color)); //ERROR DE LLAMADO DEL COLOR(¿?) - COLOR UBICADO EN ARCHIVO COLORS
        //SETEA EL NUMERO DE BUTTON VISUALIZADORES DEL 10 & 256
        audioVisualizer.setDensity(100);
        //SETEA LA ID DE LA SESION DEL AUDIO DEL REPRODUCTOR
        audioVisualizer.setPlayer(player.getAudioSessionId());
         **/
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        /**                                                        //LIBERA AL REPRODUCTOR DE LA ACCION
        if(player.isPlaying()){
            player.stop();
        }
        player.release();**/
        doUnbindService();

    }

    private void doUnbindService() {
        if(isBound){
            unbindService(playerServiceConnection);
            isBound = false;
        }
    }


    private void userResponses() {
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
            fetchSongs();
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(permission)){
                new AlertDialog.Builder(this)
                        .setTitle("Solicitando permisos")
                        .setMessage("Buscando canciones dentro del dispositivo")
                        .setPositiveButton("Permiso", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        storagePermissionLauncher.launch(permission);
                    }
                    }).setNegativeButton("Cancelando", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "No se han podido mostrar las canciones", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    })
                        .show();
            }

        }
        else{
            Toast.makeText(this, "Se ha cancelado la lecturade canciones", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSongs() {
                                        //  DEFINE LA LISTA DE CANCIONES
        List<Song> songs = new ArrayList<>();
        Uri mediaStoreUri;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            mediaStoreUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        else{
            mediaStoreUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
                                        //ILUSTRACIÓN DE CANCIONES
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,

        };
                                        //ORDEN DE MUSICA
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";
                                        //OBTENER LAS CANCIONES
        try(Cursor cursor = getContentResolver().query(mediaStoreUri, projection, null, null, sortOrder)){
                                        //CACHE DEL INDICE DEL CURSOR
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumnn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                                        //LIMPIA LOS DATOS ANTERIORES ANTES DE INICIAR UNA NUEVA REPRODUCCION
            while(cursor.moveToNext()){
                                        //OBTIENE LOS VALORES DE LA COLUMNA PARA DARLAS A  ARCHIVO DE AUDIO
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumnn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);
                long albumid = cursor.getLong(albumIdColumn);

                                        //CANCION URI
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                                        //PORTADA DE DISCO URI
                Uri albumArtworkUri  = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumid);
                                        //BORRA LA EXTENCIÓN .MP3 DEL NOMBRE DE LA CANCION
                name = name.substring(0, name.lastIndexOf("."));
                                        //ITEM DE LA CANCION
                Song song = new Song(name, uri, albumArtworkUri, size, duration);

                                        //AÑADE EL ITEM DE LA CANCION A LA LISTA DE CANCIONES
                songs.add(song);
            }
                                        //MUESTREO DE CANCIONES
            showSongs(songs);



        }
    }

    private void showSongs(List<Song> songs) {

        if (songs.size() ==0){
            Toast.makeText(this, "No hay canciones", Toast.LENGTH_SHORT).show();
            return;
        }
                                        //GUARDAR CANCIONES

        allSongs.clear();
        allSongs.addAll(songs);
                                        //ACTUALIZAR LA BARRA DE TITULOS
        String title = getResources().getString(R.string.app_name) + " - " + songs.size();
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
                                        //LAYOUT MANAGER
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
                                        //ADAPTER DE CANCIONES
        songAdapter = new SongAdapter(this, songs, player, playerView);
                                        //SETEA EL ADAPTER AL RECYCLERVIEW (SIN ANIMACION)
        //recyclerview.setAdapter(songAdapter);

                                            //ANIMACIONES EN RECYCLERVIEW
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(songAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());

        scaleInAnimationAdapter.setFirstOnly(false);
                                            //SETEA EL ADAPTER AL RECYCLERVIEW (CON ANIMACION)
        recyclerview.setAdapter(scaleInAnimationAdapter);
    }

                                                //COMANDO DE SETEO DE MENU/BOTON DE BUSQUEDA
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_btn, menu);
                                             //ITEM DEL SEACHBTN
        MenuItem menuItem = menu.findItem(R.id.btn_seach);
        SearchView searchView = (SearchView) menuItem.getActionView();
                                            //METODO PARA BUSCAR CANCIONES
        SeachSong(searchView);
        return super.onCreateOptionsMenu(menu);
    }
    private void SeachSong(SearchView searchView){
                                                //LISTENER DEL SEARCHVIEW
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                                                //FILTRAR CACIONES
                filterSongs(newText.toLowerCase());
                return true;
            }
        });
    }

    private void filterSongs(String query) {
        List<Song> filteredList = new ArrayList<>();

        if(allSongs.size()>0){
            for (Song song : allSongs){
                if (song.getTitle().toLowerCase().contains(query)){
                    filteredList.add(song);
                }
            }
            if(songAdapter!= null){
                songAdapter.filterSongs(filteredList);

            }
        }

    }


}