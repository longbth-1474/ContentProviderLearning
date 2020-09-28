package com.longhoang.providerlearning;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.longhoang.providerlearning.model.Song;

import java.util.ArrayList;

public class MediaController implements MediaPlayer.OnCompletionListener {
    private ArrayList<Song> songs;
    private Context context;
    private MediaPlayer player;
    private int index;

    public MediaController(ArrayList<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    public void create(int index) {
        release();
        this.index = index;
        Uri uri = Uri.parse(songs.get(index).getData());
        player = MediaPlayer.create(context, uri);
        start();
        player.setOnCompletionListener(this);
    }

    public void start() {
        if (player != null) {
            player.start();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void release() {
        if (player != null) {
            player.release();
        }
    }

    public void loop(boolean isLooping) {
        if (player != null) {
            player.setLooping(isLooping);
        }
    }

    public void seek(int position) {
        if (player != null) {
            player.seekTo(position);
        }
    }

    public int getDuration() {
        return player == null ? 0 : player.getDuration();
    }

    public int getPosition() {
        return player == null ? 0 : player.getCurrentPosition();
    }

    // 1: next
    // -1: prev
    public void change(int value) {
        index += value;
        if (index < 0) {
            index = songs.size() - 1;
        } else if (index >= songs.size()) {
            index = 0;
        }
        create(index);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        change(1);
    }
}
