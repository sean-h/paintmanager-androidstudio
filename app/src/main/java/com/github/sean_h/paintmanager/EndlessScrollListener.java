package com.github.sean_h.paintmanager;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int itemLoadPadding;

    public EndlessScrollListener(int itemLoadPadding) {
        this.itemLoadPadding = itemLoadPadding;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (firstVisibleItem + visibleItemCount + itemLoadPadding >= totalItemCount)
        {
            onLoadMore(totalItemCount);
        }
    }

    public abstract void onLoadMore(int itemOffset);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
