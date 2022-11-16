package com.example.newapp.ui.music;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapp.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
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
        viewHolder.durationHolder.setText(String.valueOf(song.getDuration()));
        viewHolder.sizeHolder.setText(String.valueOf(song.getSize()));
        //artwork
        Uri artworkUri = song.getArtworkuri();
        if(artworkUri!= null){
            viewHolder.artworkHolder.setImageURI(artworkUri);
            //make sure that uri has an artwork
            if(viewHolder.artworkHolder.getDrawable() ==null){
                viewHolder.artworkHolder.setImageResource(R.drawable.cascos);
            }
        }
        //on item click
        viewHolder.itemView.setOnClickListener(view -> Toast.makeText(context, song.getTitle(), Toast.LENGTH_SHORT).show());

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

        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    //Filter songs/ search results
    public void filterSongs(List<Song>filteredList){
        songs = filteredList;
        notifyDataSetChanged();
    }
}
