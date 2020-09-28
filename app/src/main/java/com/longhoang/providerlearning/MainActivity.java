package com.longhoang.providerlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.longhoang.providerlearning.adapter.SongAdapter;
import com.longhoang.providerlearning.model.Song;
import com.longhoang.providerlearning.utils.SystemData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainListener, SearchView.OnQueryTextListener, View.OnClickListener {

    private SongAdapter songAdapter;
    private MediaController mediaController;

    private ArrayList<Song> songs;

    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private boolean isPlaying;

    SearchView searchView;
    RecyclerView rvSongs;
    ImageView btnPrev, btnPlay, btnNext;
    TextView tvTitle;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvSongs = findViewById(R.id.lv_song);
        tvTitle = findViewById(R.id.tv_title);
        btnPrev = findViewById(R.id.btn_pre);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        seekBar = findViewById(R.id.sb_time);

        if (checkPermission()) {
            init();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, 0);
            }
        }

        setLister();
    }

    private void setLister() {
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
    }

    private void init() {
        songAdapter = new SongAdapter(getLayoutInflater());
        rvSongs.setAdapter(songAdapter);

        SystemData systemData = new SystemData(this);
        songs = systemData.getDataFromContentProvider();
        songAdapter.setData(songs);

        mediaController = new MediaController(songs, this) {
            @Override
            public void create(int index) {
                super.create(index);
                tvTitle.setText(songs.get(index).getTitle());
            }

            @Override
            public void start() {
                super.start();
                isPlaying = true;
            }

            @Override
            public void pause() {
                super.pause();
                isPlaying = false;
            }
        };

        searchView.setOnQueryTextListener(this);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : PERMISSIONS) {
                if (checkSelfPermission(p) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            init();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        songAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onNext() {
        mediaController.change(1);
    }

    @Override
    public void onPrev() {
        mediaController.change(-1);
    }

    @Override
    public void onMediaPause() {
        if (isPlaying) mediaController.pause();
        else mediaController.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                onNext();
                break;
            case R.id.btn_play:
                onMediaPause();
            case R.id.btn_pre:
                onPrev();
            default:
                onMediaPause();
                break;
        }
    }
}