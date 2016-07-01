package com.swifty.fillcolor.controller.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.swifty.fillcolor.R;
import com.umeng.message.proguard.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/20.
 */
public class ImageWallListAdapter extends RecyclerView.Adapter<ImageWallListAdapter.ViewHolder> {

    private Context mContext;
    List<T> imagewall;

    public ImageWallListAdapter(Context context, List<T> imagewall) {
        mContext = context;
        if(imagewall!=null) {
            this.imagewall = imagewall;
        }else{
            this.imagewall = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.view_imagewall_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imagewall.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView imagename;
        TextView author;
        ImageView image;
        Button like;
        Button dontlike;
        Button comment;

        public ViewHolder(View itemView) {
            super(itemView);
            imagename = (TextView) itemView.findViewById(R.id.imagename);
            author = (TextView) itemView.findViewById(R.id.author);
            image = (ImageView) itemView.findViewById(R.id.image);
            like = (Button) itemView.findViewById(R.id.like);
            dontlike = (Button) itemView.findViewById(R.id.dontlike);
            comment = (Button) itemView.findViewById(R.id.comment);
        }

    }
}
