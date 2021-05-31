package com.innowireless.xcal.mobile4g;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    private TextView tv_title;
    private RelativeLayout rl_layout;
    private ImageView iv_layout;
    private VideoView vv_layout;

    DataPage data;

    ViewHolderPage(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_title);
        rl_layout = itemView.findViewById(R.id.rl_layout);
        iv_layout = itemView.findViewById(R.id.iv_layout);
        vv_layout = itemView.findViewById(R.id.vv_layout);
    }

    public void onBind(DataPage data){
        this.data = data;

//        tv_title.setText(data.getTitle());
//        rl_layout.setBackgroundResource(data.getColor());
        if(data.getmedia_type().equals("IMAGE")){
            Log.d("ViewHolderPage", "_media_type - "+data.getmedia_url());

            iv_layout.setVisibility(View.VISIBLE);
            vv_layout.setVisibility(View.GONE);
            Glide.with(itemView).load(data.getmedia_url()).into(iv_layout);
        }
        else if(data.getmedia_type().equals("VIDEO")){
            Log.d("ViewHolderPage", "_media_url - "+data.getmedia_url());

            iv_layout.setVisibility(View.GONE);
            vv_layout.setVisibility(View.VISIBLE);
            Uri vidurl = Uri.parse(data.getmedia_url());
            vv_layout.setVideoURI(vidurl);
            vv_layout.start();
            vv_layout.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    vv_layout.start();
                }
            });
        }
//        Glide.with(itemView).load(data.getColor()).into(iv_layout);
    }
}