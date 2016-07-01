package com.swifty.fillcolor.controller.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.paint.PaintActivity;
import com.swifty.fillcolor.model.AsynImageLoader;
import com.swifty.fillcolor.model.bean.LocalImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
public class LocalPaintAdapter extends RecyclerView.Adapter<LocalPaintAdapter.ViewHolder> {
    List<LocalImageBean> localImageListBean;
    Context context;

    public LocalPaintAdapter(Context context, List<LocalImageBean> localImageListBean) {
        if (localImageListBean == null) {
            localImageListBean = new ArrayList<>();
        }
        this.localImageListBean = localImageListBean;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.view_localimage_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AsynImageLoader.showImageAsynWithoutCache(holder.image, "file://" + localImageListBean.get(position).getImageUrl());
        holder.image.setLayoutParams(new LinearLayout.LayoutParams(MyApplication.getScreenWidth(context) / 5 * 3, (int) (MyApplication.getScreenWidth(context) / 5 * 3 / localImageListBean.get(position).getWvHRadio())));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPaintActivity("file://" + localImageListBean.get(position).getImageUrl(), localImageListBean.get(position).getImageName());
            }
        });
        holder.lastModifyTime.setText(context.getString(R.string.lastModifty) + " " + localImageListBean.get(position).getLastModDate());
    }

    private void gotoPaintActivity(String uri, String filename) {
        Intent intent = new Intent(context, PaintActivity.class);
        intent.putExtra(MyApplication.BIGPICFROMUSER, uri);
        int formatName = Integer.valueOf(filename.replace(".png", ""));
        intent.putExtra(MyApplication.BIGPICFROMUSERPAINTNAME, formatName);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return localImageListBean.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView lastModifyTime;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            lastModifyTime = (TextView) itemView.findViewById(R.id.lastModify);
        }

    }
}
