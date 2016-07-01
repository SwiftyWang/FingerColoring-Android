package com.swifty.fillcolor.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.broadcast.LoginSuccessBroadcast;
import com.swifty.fillcolor.factory.MyDialogFactory;
import com.swifty.fillcolor.factory.SharedPreferencesFactory;
import com.swifty.fillcolor.listener.OnLoadCacheImageListener;
import com.swifty.fillcolor.listener.OnLoadUserPaintListener;
import com.swifty.fillcolor.listener.OnLoginSuccessListener;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.BaseFragment;
import com.swifty.fillcolor.model.UserFragmentModel;
import com.swifty.fillcolor.model.bean.CacheImageBean;
import com.swifty.fillcolor.model.bean.LocalImageBean;
import com.swifty.fillcolor.model.bean.UserBean;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.ListAnimationUtil;
import com.swifty.fillcolor.util.UmengLoginUtil;
import com.swifty.fillcolor.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Swifty.Wang on 2015/8/18.
 */
public class UserFragment extends BaseFragment implements OnLoginSuccessListener {
    private static UserFragment fragment;
    @Bind(R.id.userpaintlist)
    EmptyRecyclerView userpaintlist;
    @Bind(R.id.swiperefresh)
    SwipeRefreshLayout refreshLayout;
    RecyclerView.Adapter adapter;
    @Bind(R.id.emptylay_paintlist)
    LinearLayout emptylayPaintlist;
    List<LocalImageBean> localImageBeans;
    @Bind(R.id.tab_imagecache)
    RadioButton tabImagecache;
    @Bind(R.id.tab_local)
    RadioButton tabLocal;
    @Bind(R.id.tab_cloud)
    RadioButton tabCloud;
    @Bind(R.id.usertabs)
    RadioGroup usertabs;

    MyDialogFactory myDialogFactory;

    public static UserFragment getInstance() {
        if (fragment == null) {
            fragment = new UserFragment();
        }
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        addEvents();
        return rootView;
    }

    private void initViews() {
        myDialogFactory = new MyDialogFactory(getActivity());
        userpaintlist.setEmptyView(emptylayPaintlist);
        refreshLayout.setColorSchemeResources(R.color.red, R.color.orange, R.color.green, R.color.maincolor);
        loadLocalPaints();
    }

    private void showUserLoginxDialog() {
        myDialogFactory.showLoginDialog(this);
    }

    private void addEvents() {
        swipeRefreshLayout = refreshLayout;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                L.e("load");
                if (usertabs.getCheckedRadioButtonId() == R.id.tab_local) {
                    //change vertical recycleview to listview
                    userpaintlist.setLayoutManager(new LinearLayoutManager(getActivity()));
                    //clear recycleview
                    userpaintlist.setAdapter(new LocalPaintAdapter(getActivity(), localImageBeans));
                    OnLoadUserPaintListener onLoadUserPaintListener = new OnLoadUserPaintListener() {
                        @Override
                        public void loadUserPaintFinished(List<LocalImageBean> list) {
                            if (list != null) {
                                localImageBeans = list;
                                adapter = new LocalPaintAdapter(getActivity(), localImageBeans);
                                userpaintlist.setAdapter(ListAnimationUtil.addScaleandAlphaAnim(adapter));
                            }
                            refreshLayout.setRefreshing(false);
                        }
                    };
                    UserFragmentModel.getInstance(getActivity()).obtainLocalPaintList(onLoadUserPaintListener);
                } else if (usertabs.getCheckedRadioButtonId() == R.id.tab_imagecache) {
                    loadCacheImages();
                } else if (usertabs.getCheckedRadioButtonId() == R.id.tab_cloud) {
                    //load cloud paints
                }

            }
        });
        usertabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.tab_local) {
                    loadLocalPaints();
                } else if (i == R.id.tab_imagecache) {
                    loadCacheImages();
                } else {
                    //load cloud paints
                }
            }
        });
        tabCloud.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    if (MyApplication.user != null) {
//                        return false;
//                    } else {
//                        myDialogFactory.showLoginDialog(UserFragment.this);
//                        return true;
//                    }
//                }
//                return false;
                Toast.makeText(getActivity(), getString(R.string.comingsoon), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void loadCacheImages() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        //change vertical recycleview to gridview
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        userpaintlist.setLayoutManager(layoutManager);
        //clear recycleview
        userpaintlist.scrollToPosition(0);
        userpaintlist.setAdapter(new CacheImageAdapter(getActivity(), new ArrayList<CacheImageBean>()));
        //load cache paints
        OnLoadCacheImageListener loadUserPaintListener = new OnLoadCacheImageListener() {
            @Override
            public void loadCacheImageSuccess(List<CacheImageBean> cacheImageBeans) {
                refreshLayout.setRefreshing(false);
                if (cacheImageBeans != null) {
                    adapter = new CacheImageAdapter(getActivity(), cacheImageBeans);
                    userpaintlist.setAdapter(ListAnimationUtil.addScaleandAlphaAnim(adapter));
                }
            }

        };
        UserFragmentModel.getInstance(getActivity()).obtainCacheImageList(getActivity(), loadUserPaintListener);
    }

    private void loadLocalPaints() {
        //change vertical recycleview to listview
        userpaintlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        //clear recycleview
        userpaintlist.setAdapter(new LocalPaintAdapter(getActivity(), localImageBeans));
        //load local paints
        OnLoadUserPaintListener onLoadUserPaintListener = new OnLoadUserPaintListener() {
            @Override
            public void loadUserPaintFinished(List<LocalImageBean> list) {
                if (list != null) {
                    adapter = new LocalPaintAdapter(getActivity(), list);
                    userpaintlist.setAdapter(adapter);
                }
            }
        };
        UserFragmentModel.getInstance(getActivity()).obtainLocalPaintList(onLoadUserPaintListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        L.e("resume  " + isAdded());
        //awlays refreshlist when resume
        if (isAdded()) {
            if (tabLocal.isChecked()) {
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            } else {
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengLoginUtil.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    public void finish() {
        L.e("Userfinish");
        fragment = null;
    }

    @Override
    public void onLoginSuccess(UserBean userBean) {
        UmengLoginUtil.getInstance().loginSuccessEvent(getActivity(), userBean, myDialogFactory);
    }
}
