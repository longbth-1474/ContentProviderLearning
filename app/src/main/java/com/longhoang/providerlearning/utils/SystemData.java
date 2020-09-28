package com.longhoang.providerlearning.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.longhoang.providerlearning.model.Song;

import java.util.ArrayList;

public class SystemData {

    private ContentResolver contentResolver;

    public SystemData(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    public ArrayList<Song> getDataFromContentProvider() {
        ArrayList<Song> arr = new ArrayList<>();

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();

            int indexId = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID);
            int indexData = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            int indexSize = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE);
            int indexTitle = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            int indexDuration = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
            int indexArtist = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
            int indexAlbum = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);

            while (!cursor.isAfterLast()) {
                String data = cursor.getString(indexData);
                int size = cursor.getInt(indexSize);
                int duration = cursor.getInt(indexDuration);
                String title = cursor.getString(indexTitle);
                String artist = cursor.getString(indexArtist);
                String album = cursor.getString(indexAlbum);
                long id = cursor.getLong(indexId);
                Song song = new Song();
                song.setId(id);
                song.setAlbum(album);
                song.setArtist(artist);
                song.setData(data);
                song.setDuration(duration);
                song.setSize(size);
                song.setTitle(title);
                arr.add(song);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return arr;
    }
}
