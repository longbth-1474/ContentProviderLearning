package com.longhoang.providerlearning.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.longhoang.providerlearning.R;
import com.longhoang.providerlearning.model.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> implements Filterable {

    private SongFilter songFilter = new SongFilter();
    private LayoutInflater inflater;
    private ArrayList<Song> songs;
    ArrayList<Song> allSongs;

    public SongAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Song> songs) {
        this.songs.addAll(songs);
        allSongs.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return songFilter;
    }

    @NonNull
    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View songView = inflater.inflate(R.layout.item_song, parent);
        return new SongViewHolder(songView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongViewHolder holder, int position) {
        holder.setItem(songs.get(position));
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSong;
        TextView tvTitle;
        TextView tvSize;
        TextView tvArtist;
        TextView tvAlbum;
        TextView tvDuration;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSong = itemView.findViewById(R.id.imgSong);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvAlbum = itemView.findViewById(R.id.tvAlbum);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }

        public void setItem(Song song) {
            if (song != null) {
                Uri uri = Uri
                        .parse("content://media/external/audio/albumart/" + song.getId());
                Glide.with(imgSong).load(uri).into(imgSong);

                @SuppressLint("DefaultLocale") String str = String.format("%02d:%02d",
                        (song.getDuration() / 1000 % 3600) / 60, (song.getDuration() / 1000 % 60));
                tvDuration.setText(str);

                String strSize = Formatter.formatFileSize(tvSize.getContext(), song.getSize());
                tvSize.setText(strSize);

                tvTitle.setText(song.getTitle());
                tvArtist.setText(song.getArtist());
                tvAlbum.setText(song.getAlbum());
            }
        }
    }

    public class SongFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence key) {
            ArrayList<Song> result = new ArrayList<>();
            for (Song s : allSongs) {
                if (s.getTitle().toLowerCase().contains(key.toString().toLowerCase())) {
                    result.add(s);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = result.size();
            filterResults.values = result;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            songs = (ArrayList<Song>) results.values;
            notifyDataSetChanged();
        }
    }
}
