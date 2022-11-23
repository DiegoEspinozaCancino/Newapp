package com.example.newapp.ui.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapp.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Song> songs;
    ExoPlayer player;
    ConstraintLayout playerView;

    public SongAdapter(Context context, List<Song> songs, ExoPlayer player, ConstraintLayout playerView) {
        this.context = context;
        this.songs = songs;
        this.player = player;
        this.playerView = playerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate song row item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row_item, parent, false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //current song and view holder
        Song song = songs.get(position);
        SongViewHolder viewHolder = (SongViewHolder) holder;

        //set values to views
        viewHolder.titleHolder.setText(song.getTitle());
        viewHolder.durationHolder.setText(getDuration(song.getDuration()));
        viewHolder.sizeHolder.setText(getSize(song.getSize()));
        //artwork
        Uri artworkUri = song.getArtworkuri();
        if(artworkUri!= null){
            viewHolder.artworkHolder.setImageURI(artworkUri);
            //make sure that uri has an artwork
            if(viewHolder.artworkHolder.getDrawable() ==null){
                viewHolder.artworkHolder.setImageResource(R.drawable.cascos);
            }
        }
                                                            // REPRODUCIR MUSICA EN ITEM ONCLICK
        viewHolder.itemView.setOnClickListener(view -> {
            //INICIA EL SERVICIO DE REPRODUCCION
            context.startService(new Intent(context.getApplicationContext(), MPlayerService.class));

            //ESTA LA CANCION REPRODUCIENDOCE DEL SEARCH VIEW?, SI LO ES, SE ESCONDE EL TECLADO ACLARANDO EL FOCO DE VISTA, VER EL METODO EN MAIN ACTIVITY
            SearchView searchView = ((MusicPlayerActivity)context).searchView;
            if(searchView != null)
                searchView.clearFocus();
            //MUESTRA LA VISTA DEL REPRODUCTOR
            playerView.setVisibility(View.VISIBLE);
                                                            //REPRODCIENDO LA MUSICA
            if(!player.isPlaying()) {
            player.setMediaItems(getMediaItems(), position,0 );
            }
            else{
                player.pause();
                player.seekTo(position, 0);
            }
                                                        //PREPARAR Y REPRODUCIR

            player.prepare();
            player.play();
            Toast.makeText(context, song.getTitle(), Toast.LENGTH_SHORT).show();
                                                        //MOSTRAR LA VISTA DEL REPRODUCTOR
            playerView.setVisibility(View.VISIBLE);

                                    //INSPECCIONA SI EL PERMISO DE GRABACION DE AUDIO ESTA "PERMITIDA"
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                                        //SOLICITAR EL PERMISO DE GRABACION DE AUDIO
                ((MusicPlayerActivity)context).recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }

        });

    }

    private List<MediaItem> getMediaItems() {
                                                        //DEFINIR LA LISTA DE MEDIOS DE ITEMS
        List<MediaItem> mediaItems = new ArrayList<>();
        for(Song song : songs){
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(song.getUri())
                    .setMediaMetadata(getMetaData(song))
                    .build();
            //AÑADIR EL ITEM DE MEDIA A LA LISTA DE MEDIA ITEMS
            mediaItems.add(mediaItem);
        }
        return  mediaItems;

    }

    private MediaMetadata getMetaData(Song song) {
        return new MediaMetadata.Builder()
                .setTitle(song.getTitle())
                .setArtworkUri(song.getArtworkuri())
                .build();
    }

    //View Holder
    public static class SongViewHolder extends RecyclerView.ViewHolder{
        ImageView artworkHolder;
        TextView titleHolder, durationHolder, sizeHolder;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            artworkHolder = itemView.findViewById(R.id.artWorkView);
            titleHolder = itemView.findViewById(R.id.songtitleView);
            durationHolder = itemView.findViewById(R.id.durationView);
            sizeHolder = itemView.findViewById(R.id.sizeview);

        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    //Filter songs/ search results
    @SuppressLint("NotifyDataSetChanged")
    public void filterSongs(List<Song>filteredList){
        songs = filteredList;
        notifyDataSetChanged();
    }

    private String getDuration(int totalDuration){
        String totalDurationText;
                                            //DURACIÓN DEL ARCHIVO DE AUDIO
        int hrs = totalDuration/(1000*60*60);
        int min = (totalDuration%(1000*60*60))/(1000*60);
        int secs = (((totalDuration%(1000*60*60))%(1000*60*60))%(1000*600))/1000;

        if(hrs <1){
           totalDurationText = String.format("%02d:%02d", min, secs);
        }
        else{
            totalDurationText = String.format("%1d:%02d:%02d", hrs, min, secs);
        }
        return  totalDurationText;
    }

                                            //TAMAÑO DEL ARCHIVO
    private String getSize(long bytes){
        String hrSize;

        double k = bytes/1024.0;
        double m = ((bytes/1024.0)/1024.0);
        double g = (((bytes/1024.0)/1024.0)/1024.0);
        double t = ((((bytes/1024.0)/1024.0)/1024.0)/1024.0);

                                    //FORMATO DE ARCHIVO
        DecimalFormat dec = new DecimalFormat("0.00");

        if(t>1){
            hrSize = dec.format(t).concat(" TB");
        }
        else if (g>1){
            hrSize = dec.format(g).concat(" GB");
        }
        else if (m>1){
            hrSize = dec.format(m).concat(" MB");
        }
        else if (k>1){
            hrSize = dec.format(k).concat(" KB");
        }
        else{
            hrSize = dec.format(g).concat(" Bytes");
        }
        return  hrSize;
    }

}
