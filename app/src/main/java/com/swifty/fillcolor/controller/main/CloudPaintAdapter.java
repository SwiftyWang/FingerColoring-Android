package com.swifty.fillcolor.controller.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swifty.fillcolor.R;
import com.swifty.fillcolor.model.bean.LocalImageBean;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
public class CloudPaintAdapter extends RecyclerView.Adapter<CloudPaintAdapter.ViewHolder> {
    List<LocalImageBean> localImageListBean;
    Context context;

    public CloudPaintAdapter(Context context, List<LocalImageBean> localImageListBean) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return localImageListBean.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}
