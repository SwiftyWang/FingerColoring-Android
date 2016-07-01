package com.swifty.fillcolor.controller.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.paint.PaintActivity;
import com.swifty.fillcolor.model.AsynImageLoader;
import com.swifty.fillcolor.model.bean.CacheImageBean;
import com.swifty.fillcolor.util.UmengUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/9/9.
 */
public class CacheImageAdapter extends RecyclerView.Adapter<CacheImageAdapter.ViewHolder> {
    List<CacheImageBean> cacheImageBeans;
    Context context;

    public CacheImageAdapter(Context context, List<CacheImageBean> cacheImageBeans) {
        if (cacheImageBeans == null) {
            cacheImageBeans = new ArrayList<>();
        }
        this.cacheImageBeans = cacheImageBeans;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.view_cacheimage_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (cacheImageBeans.get(position).getWvHRadio() != 0) {
            holder.image.setLayoutParams(new LinearLayout.LayoutParams(MyApplication.getScreenWidth(context) / 2, (int) (MyApplication.getScreenWidth(context) / 2 / cacheImageBeans.get(position).getWvHRadio())));
        } else {
            holder.image.setLayoutParams(new LinearLayout.LayoutParams(MyApplication.getScreenWidth(context) / 2, (int) (MyApplication.getScreenWidth(context) / 2 / 0.71)));
        }
        AsynImageLoader.showImageAsynWithoutCache(holder.image, cacheImageBeans.get(position).getUrl());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPaintActivity(cacheImageBeans.get(position).getUrl());
            }
        });
    }

    private void gotoPaintActivity(String s) {
        UmengUtil.analysitic(context, UmengUtil.MODELNUMBER, s);
        Intent intent = new Intent(context, PaintActivity.class);
        intent.putExtra(MyApplication.BIGPIC, s);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return cacheImageBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}
