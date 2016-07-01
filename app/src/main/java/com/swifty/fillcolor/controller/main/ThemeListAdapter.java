package com.swifty.fillcolor.controller.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.model.AsynImageLoader;
import com.swifty.fillcolor.model.OnRecycleViewItemClickListener;
import com.swifty.fillcolor.model.bean.ThemeBean;

import java.util.List;

/**
 * Created by Swifty.Wang on 2015/8/14.
 */
public class ThemeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    FrameLayout footer;
    List<ThemeBean.Theme> themelist;
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    private OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public ThemeListAdapter(Context context, List<ThemeBean.Theme> themelist, View footer) {
        this.context = context;
        this.themelist = themelist;
        this.footer = (FrameLayout) footer.getParent();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == themelist.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == TYPE_ITEM) {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.view_list_item, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_FOOTER) {
            return new VHFooter(footer);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VHItem) {
            if (themelist.get(position) != null) {
                ((VHItem) holder).name.setText(themelist.get(position).getN());
                if (themelist.get(position).getC() == -1) {
                    ((VHItem) holder).image.setImageResource(R.mipmap.secretgraden);
                } else {
                    AsynImageLoader.showImageAsyn(((VHItem) holder).image, String.format(MyApplication.ThemeThumbUrl, themelist.get(position).getC()));
                }
                ((VHItem) holder).parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onRecycleViewItemClickListener != null)
                            onRecycleViewItemClickListener.recycleViewItemClickListener(((VHItem) holder).parent, position);
                    }
                });
            }
        } else if (holder instanceof VHFooter) {
            ((VHFooter) holder).footer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    @Override
    public int getItemCount() {
        return themelist.size() + 1;
    }

    public List<ThemeBean.Theme> getList() {
        return themelist;
    }


    class VHFooter extends RecyclerView.ViewHolder {
        FrameLayout footer;

        public VHFooter(View itemView) {
            super(itemView);
            footer = (FrameLayout) itemView;
        }
    }

    static class VHItem extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public View parent;

        public VHItem(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            parent = itemView;
        }

    }


    public void updateListView(List<ThemeBean.Theme> list) {
        this.themelist = list;
        notifyDataSetChanged();
    }
}
