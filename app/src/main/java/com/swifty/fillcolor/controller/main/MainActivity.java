package com.swifty.fillcolor.controller.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.broadcast.LoginSuccessBroadcast;
import com.swifty.fillcolor.controller.AppCompatBaseAcitivity;
import com.swifty.fillcolor.factory.MyDialogFactory;
import com.swifty.fillcolor.factory.SharedPreferencesFactory;
import com.swifty.fillcolor.listener.OnLoginSuccessListener;
import com.swifty.fillcolor.model.bean.UserBean;
import com.swifty.fillcolor.receiver.UserLoginReceiver;
import com.swifty.fillcolor.util.CommentUtil;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.SNSUtil;
import com.swifty.fillcolor.util.UmengLoginUtil;
import com.swifty.fillcolor.util.UmengUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/7/31.
 */
public class MainActivity extends AppCompatBaseAcitivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private long exitTime;
    UserLoginReceiver receiver;
    IntentFilter filter;
    public static MenuItem logout;
    MyDialogFactory myDialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        UmengUtil.pushNotification(this);
        //autoLogin();
        initViews();
        showMarketCommentDialog();
        receiver = new UserLoginReceiver();
        filter = new IntentFilter();
        filter.addAction("userLoginAction");
    }

    private void autoLogin() {
        MyApplication.userToken = SharedPreferencesFactory.grabString(this, SharedPreferencesFactory.USERSESSION);
        L.e(MyApplication.userToken);
        if (MyApplication.userToken != null && !MyApplication.userToken.isEmpty()) {
            Toast.makeText(this, getString(R.string.loginbg), Toast.LENGTH_SHORT).show();
            UmengLoginUtil.getInstance().serverBackgroundLogin(new OnLoginSuccessListener() {
                @Override
                public void onLoginSuccess(UserBean userBean) {
                    if (userBean != null && userBean.getUsers() != null)
                        LoginSuccessBroadcast.getInstance().sendBroadcast(MainActivity.this);
                }
            });
        }
    }

    private void showMarketCommentDialog() {
        if (Math.random() < 0.15 && SharedPreferencesFactory.getBoolean(this, SharedPreferencesFactory.CommentEnableKey)) {
            myDialogFactory.showCommentDialog();
        } else if (Math.random() > 0.15 && Math.random() < 0.25 && SharedPreferencesFactory.getBoolean(this, SharedPreferencesFactory.AddQQGroupEnable)) {
            myDialogFactory.showAddQQgroup();
        }
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        myDialogFactory = new MyDialogFactory(this);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        List<String> tabs = new ArrayList<String>();
        tabs.add(getString(R.string.themelist));
        //       tabs.add(getString(R.string.imagewall));
        tabs.add(getString(R.string.userlogin));
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabs);
        //initial all fragment
        sectionsPagerAdapter.destroyAllFragment();
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    //showFirstTimeLoginDialog();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_collections_white_24dp);
        //      tabLayout.getTabAt(1).setIcon(R.drawable.ic_wallpaper_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_face_white_24dp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void showFirstTimeLoginDialog() {
        if (MyApplication.user == null && SharedPreferencesFactory.getBoolean(this, SharedPreferencesFactory.IsFirstTimeShowLoginDialog, true)) {
            myDialogFactory.showFirstTimeLoginDialog(new OnLoginSuccessListener() {
                @Override
                public void onLoginSuccess(UserBean userBean) {
                    UmengLoginUtil.getInstance().loginSuccessEvent(MainActivity.this, userBean, myDialogFactory);
                }
            });
            SharedPreferencesFactory.saveBoolean(this, SharedPreferencesFactory.IsFirstTimeShowLoginDialog, false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.action_search));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ThemeListFragment.getInstance().filterData(newText);
                return true;
            }
        });
        logout = menu.findItem(R.id.action_logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            SNSUtil.shareApp(this);
        } else if (id == R.id.action_comment) {
            CommentUtil.commentApp(this);
        } else if (id == R.id.about) {
            myDialogFactory.showAboutDialog();
        } else if (id == R.id.action_setting) {
            myDialogFactory.showSettingDialog();
        } else if (id == R.id.action_logout) {
            UmengLoginUtil.getInstance().logout(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, getString(R.string.pleasepressexit), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengLoginUtil.getInstance().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(receiver);
    }
}
