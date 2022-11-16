package com.example.newapp.ui.music;

import android.net.Uri;

public class Song {
    private String title;
    Uri uri;
    Uri artworkUri;
    private int size;
    private int duration;

    public Song(String title, Uri uri, Uri artworkuri, int size, int duration) {
        this.title = title;
        this.uri = uri;
        this.artworkUri = artworkuri;
        this.size = size;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

    public Uri getArtworkuri() {
        return artworkUri;
    }

    public int getSize() {
        return size;
    }

    public int getDuration() {
        return duration;
    }
}
