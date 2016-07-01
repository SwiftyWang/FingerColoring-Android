package com.swifty.fillcolor.controller.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.listener.OnThemeListLoadListener;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.BaseFragment;
import com.swifty.fillcolor.controller.categorylist.GridViewActivity;
import com.swifty.fillcolor.model.OnRecycleViewItemClickListener;
import com.swifty.fillcolor.model.ThemeListFragmentModel;
import com.swifty.fillcolor.model.bean.ThemeBean;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.ListAnimationUtil;
import com.swifty.fillcolor.util.NetWorkUtil;
import com.swifty.fillcolor.util.UmengUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

/**
 * Created by Swifty.Wang on 2015/8/14.
 */
public class ThemeListFragment extends BaseFragment {
    private static final int ONEPAGENUMBER = 20;
    private static ThemeListFragment fragment;
    RecyclerView listView;
    FloatingActionButton floatingActionButton;
    Button footer;
    private int preLast;
    List<ThemeBean.Theme> themelist;
    private ThemeListAdapter adapter;
    private AlphaInAnimationAdapter alphaAdapter;
    int id = 0;
    private String search;
    LinearLayoutManager layoutManager;
    private boolean isLoading;
    private int page;
    SwipeRefreshLayout refreshLayout;

    public static ThemeListFragment getInstance() {
        if (fragment == null) {
            fragment = new ThemeListFragment();
        }
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_theme_list, container, false);
        initViews(rootView);
        if (themelist == null) {
            loadData(LoadModel.normal);
            L.e("normal");
        } else {
            L.e("nochange");
            loadData(LoadModel.nochange);
        }
        return rootView;
    }


    private void initViews(View rootView) {
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout = refreshLayout;
        listView = (RecyclerView) rootView.findViewById(R.id.theme_list);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floating);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        refreshLayout.setColorSchemeResources(R.color.red, R.color.orange, R.color.green, R.color.maincolor);
        footer = (Button) LayoutInflater.from(getActivity()).inflate(R.layout.textview_footer, null).findViewById(R.id.footer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(LoadModel.refresh);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listTop();
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) floatingActionButton.getLayoutParams();
            p.setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
            floatingActionButton.setLayoutParams(p);
        }
    }

    private void listTop() {
        listView.smoothScrollToPosition(0);
    }


    private void loadData(LoadModel model) {
        switch (model) {
            case nochange:
                addListListener();
                break;
            case normal:
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(true);
                    }
                });
                ThemeListFragmentModel.getInstance().loadData(getActivity(), new OnThemeListLoadListener() {
                    @Override
                    public void onLoadFinish(List<ThemeBean.Theme> themes) {
                        refreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        });
                        themelist = new ArrayList<ThemeBean.Theme>();
                        themelist.add(new ThemeBean.Theme(-1, getString(R.string.secretGarden), 0));
                        if (themes != null && !themes.isEmpty()) {
                            themelist.addAll(themes);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
                        }
                        page = themelist.size() / ONEPAGENUMBER - 1;
                        addListListener();
                    }
                });
                break;
            case refresh:
                isLoading = false;
                footer.setText(R.string.clickloadmore);
                if (NetWorkUtil.isNetworkConnected(getActivity())) {
                    ThemeListFragmentModel.getInstance().refreshListContent(getActivity(), new OnThemeListLoadListener() {
                        @Override
                        public void onLoadFinish(List<ThemeBean.Theme> themes) {
                            L.e("start refresh");
                            themelist.clear();
                            themelist.add(new ThemeBean.Theme(-1, getString(R.string.secretGarden), 0));
                            if (themes != null && !themes.isEmpty()) {
                                themelist.addAll(themes);
                                L.e(themelist.size() + "");
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.loadfailed), Toast.LENGTH_SHORT).show();
                            }
                            page = 0;
                            alphaAdapter.notifyDataSetChanged();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.network_notconnet), Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
                break;
            case loadmore:
                isLoading = true;
                footer.setText(R.string.loadmoretheme);
                L.e(isAdded() + "");
                ThemeListFragmentModel.getInstance().loadMoreData(getActivity(), ++page, new OnThemeListLoadListener() {
                    @Override
                    public void onLoadFinish(List<ThemeBean.Theme> names) {
                        if (names == null) {
                            --page;
                            isLoading = false;
                            footer.setText(R.string.loadfailed);
                        } else if (names.isEmpty()) {
                            --page;
                            footer.setText(R.string.nomoredata);
                        } else {
                            int presize = themelist.size();
                            themelist.addAll(names);
                            alphaAdapter.notifyItemInserted(presize);
                            footer.setText(R.string.clickloadmore);
                            isLoading = false;
                        }
                    }
                });
                break;
        }
    }

    private void addListListener() {
        adapter = new ThemeListAdapter(getActivity(), themelist, footer);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void recycleViewItemClickListener(View view, int i) {
                if (search == null || search.isEmpty()) {
                    gotoDetailGridActivity(i);
                } else {
                    int pos = getThemeIndex(search, i);
                    if (pos != -1) {
                        gotoDetailGridActivity(pos);
                    }
                }
            }
        });
        alphaAdapter = ListAnimationUtil.addAlphaAnim(adapter);
        listView.setAdapter(alphaAdapter);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && themelist == adapter.getList()) {
                    //filter dont show footer view and not loading more items
                    ((View) footer.getParent()).setVisibility(View.VISIBLE);
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        L.e("start loadmre");
                        loadData(LoadModel.loadmore);

                    }
                }
            }
        });
    }

    private int getThemeIndex(String search, int pos) {
        int index = 0;
        for (int i = 0; i < themelist.size(); i++) {
            if (themelist.get(i).getN().contains(search)) {
                if (pos == index) {
                    return i;
                }
                index++;
            }
        }
        return -1;
    }

    private void gotoDetailGridActivity(int i) {
        UmengUtil.analysitic(getActivity(), UmengUtil.THEMENAME, themelist.get(i).getN());
        Intent intent = new Intent(getActivity(), GridViewActivity.class);
        intent.putExtra(MyApplication.THEMEID, themelist.get(i).getC());
        intent.putExtra(MyApplication.THEMENAME, themelist.get(i).getN());
        startActivity(intent);
    }

    public void filterData(String filterStr) {
        try {
            ((View) footer.getParent()).setVisibility(View.GONE);
            search = filterStr;
            List<ThemeBean.Theme> filterDateList = new ArrayList<ThemeBean.Theme>();
            if (filterStr.isEmpty()) {
                refreshLayout.setEnabled(true);
                filterDateList = themelist;
            } else {
                refreshLayout.setEnabled(false);
                filterDateList.clear();
                for (ThemeBean.Theme theme : themelist) {
                    if (theme.getN().toLowerCase(Locale.getDefault()).contains(filterStr.toLowerCase(Locale.getDefault())))
                        filterDateList.add(theme);
                }
            }
            adapter.updateListView(filterDateList);
            alphaAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    public void finish() {
        L.e("Themefinish");
        fragment = null;
    }

    private enum LoadModel {
        normal,
        refresh,
        loadmore,
        nochange,
    }


}
