package com.swifty.fillcolor.controller.categorylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.BaseActivity;
import com.swifty.fillcolor.controller.paint.PaintActivity;
import com.swifty.fillcolor.model.GridViewActivityModel;
import com.swifty.fillcolor.model.OnRecycleViewItemClickListener;
import com.swifty.fillcolor.model.bean.PictureBean;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.ListAnimationUtil;
import com.swifty.fillcolor.util.NetWorkUtil;
import com.swifty.fillcolor.util.UmengUtil;
import com.swifty.fillcolor.view.EmptyRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/7/31.
 */
public class GridViewActivity extends BaseActivity {
    private int categoryId;
    private EmptyRecyclerView gridView;
    List<PictureBean.Picture> pictureBeans;
    GirdRecyclerViewAdapter gridViewAdapter;
    private TextView titleView;
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = getIntent().getExtras().getInt(MyApplication.THEMEID);
        initViews();
    }

    private void loadLocaldata() {
        try {
            pictureBeans = getSecretGardenBean(new ArrayList<>(Arrays.asList(getAssets().list("SecretGarden"))));
            L.e(pictureBeans.size() + "");
            if (pictureBeans == null) {
                Toast.makeText(GridViewActivity.this, getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
            } else {
                showGrid(true);
            }
        } catch (IOException e) {
            L.e(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * put sercet garden uri into PictureBean
     *
     * @param secretGarden
     * @return
     */
    private List<PictureBean.Picture> getSecretGardenBean(ArrayList<String> secretGarden) {
        List<PictureBean.Picture> pictureBeans = new ArrayList<>();
        for (String s : secretGarden) {
            pictureBeans.add(new PictureBean.Picture(s));
        }
        return pictureBeans;
    }

    private void loadPicsInthisTheme(int anInt) {
        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });
        GridViewActivityModel.getInstance().loadPictureData(this, anInt, new GridViewActivityModel.OnLoadPicFinishListener() {
            @Override
            public void LoadPicFinish(List<PictureBean.Picture> pictures) {
                swipeView.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                    }
                });
                if (pictures != null && !pictures.isEmpty()) {
                    pictureBeans = pictures;
                    showGrid(false);
                } else {
                    Toast.makeText(GridViewActivity.this, getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void LoadPicFailed(String error) {
                swipeView.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                    }
                });
                Toast.makeText(GridViewActivity.this, getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
            }
        });
//        } else {
//            showGrid(false);
//        }
    }

    private void initViews() {
        setContentView(R.layout.activity_gridview);
        titleView = (TextView) findViewById(R.id.toolbar_title);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        gridView = (EmptyRecyclerView) findViewById(R.id.detail_gird);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridView.setLayoutManager(layoutManager);
        titleView.setText(getIntent().getStringExtra(MyApplication.THEMENAME));
        swipeView.setColorSchemeResources(R.color.red, R.color.orange, R.color.green, R.color.maincolor);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtil.isNetworkConnected(GridViewActivity.this)) {
                    GridViewActivityModel.getInstance().refreshPictureData(GridViewActivity.this, categoryId, new GridViewActivityModel.OnLoadPicFinishListener() {
                        @Override
                        public void LoadPicFinish(List<PictureBean.Picture> pictureBeans) {
                            swipeView.setRefreshing(false);
                            showGrid(false);
                        }

                        @Override
                        public void LoadPicFailed(String error) {
                            swipeView.setRefreshing(false);
                            Toast.makeText(GridViewActivity.this, getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    swipeView.setRefreshing(false);
                    Toast.makeText(GridViewActivity.this, getString(R.string.network_notconnet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoPaintActivity(String s) {
        UmengUtil.analysitic(this, UmengUtil.MODELNUMBER, getIntent().getStringExtra(MyApplication.THEMENAME) + categoryId);
        Intent intent = new Intent(this, PaintActivity.class);
        if (s.contains(MyApplication.MainUrl)) {
            intent.putExtra(MyApplication.BIGPIC, s);
        } else {
            intent.putExtra(MyApplication.BIGPIC, MyApplication.SECRETGARDENLOCATION + s);
        }
        startActivity(intent);
    }


    private void showGrid(final boolean isLocal) {
        gridViewAdapter = new GirdRecyclerViewAdapter(this, pictureBeans, categoryId, isLocal);
        gridViewAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void recycleViewItemClickListener(View view, int i) {
                if (isLocal) {
                    gotoPaintActivity(pictureBeans.get(i).getUri());
                } else {
                    gotoPaintActivity(String.format(MyApplication.ImageLageUrl, categoryId, pictureBeans.get(i).getId()));
                }
            }
        });
        gridView.setAdapter(ListAnimationUtil.addScaleandAlphaAnim(gridViewAdapter));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (categoryId == -1) {
            loadLocaldata();
        } else {
            loadPicsInthisTheme(categoryId);
        }
    }

}
