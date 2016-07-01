package com.swifty.fillcolor.controller;

import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by Swifty.Wang on 2015/9/2.
 */
public class AppCompatBaseAcitivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    protected AppBarLayout appBarLayout;

    public void setmSwipeRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mSwipeRefreshLayout != null) {
            if (i == 0) {
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (appBarLayout != null) {
            appBarLayout.removeOnOffsetChangedListener(this);
        }
    }
}
