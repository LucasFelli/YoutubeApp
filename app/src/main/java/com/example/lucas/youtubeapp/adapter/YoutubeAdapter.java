package com.example.lucas.youtubeapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.lucas.youtubeapp.viewholders.Link;
import com.example.lucas.youtubeapp.R;
import com.example.lucas.youtubeapp.models.Item;
import com.example.lucas.youtubeapp.viewholders.VideoHolder;

import java.util.List;

/**
 * Created by lucas on 22/03/2018.
 */

public class YoutubeAdapter extends RecyclerView.Adapter<VideoHolder> {
    private final List<Item> videos;
    private Link clickListener;

    public YoutubeAdapter(List<Item> videos, Link link) {
        this.videos = videos;
        this.clickListener = link;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        holder.bind(videos.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }
}
