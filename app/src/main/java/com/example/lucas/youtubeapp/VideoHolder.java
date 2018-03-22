package com.example.lucas.youtubeapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by lucas on 22/03/2018.
 */

public class VideoHolder extends RecyclerView.ViewHolder{
    private TextView title;
    private ImageView thumbnail;
    private CardView cardView;

    public VideoHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        cardView = itemView.findViewById(R.id.cardView);
    }

    public void bind(final Item video, final Link clickListener) {
        title.setText(video.getSnippet().getTitle());
        Picasso.get().load(video.getSnippet().getThumbnails().getDefault().getUrl()).into(thumbnail);
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clickListener.onVideoClickListener(video);
            }
        });
    }
}
