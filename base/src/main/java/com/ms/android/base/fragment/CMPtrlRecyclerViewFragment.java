package com.ms.android.base.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.ms.android.base.R;
import com.ms.android.base.widget.autolayout.utils.AutoUtils;
import com.ms.android.base.widget.refresh.LoadMoreContainer;
import com.ms.android.base.widget.refresh.LoadMoreHandler;
import com.ms.android.base.widget.refresh.LoadMoreRecyclerViewContainer;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by Wenxc on 2017/8/30.
 * 通用RecyclerView下来刷新上拉加载片段
 */
public abstract class CMPtrlRecyclerViewFragment extends BaseFragment {
    LoadMoreRecyclerViewContainer loadMoreRecyclerViewContainer;

    PtrFrameLayout ptrFrameLayout;

    protected RecyclerView recyclerView;
    private static final int DEFAULT_PAGE_START = 1;
    protected int mPage = DEFAULT_PAGE_START;
    private static final int DEFAULT_PAGE_SIZE = 10;
    protected View empty_view;

    @Override
    public final int getLayoutRes() {
        return R.layout.frag_cm_ptrl_recyclerview;
    }

    @Override
    protected final void init(View contentView, Bundle savedInstanceState) {
        loadMoreRecyclerViewContainer = (LoadMoreRecyclerViewContainer) contentView.findViewById(R.id.load_more_recycler_view);
        ptrFrameLayout = (PtrFrameLayout) contentView.findViewById(R.id.load_more_list_view_ptr_frame);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //外部数据初始化
        outerInit();

        //初始化监听事件
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                RecyclerView recyclerView = loadMoreRecyclerViewContainer.getRecyclerView();
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    return firstVisibleItemPosition == 0 && recyclerView.getChildAt(0).getTop() == 0;
                } else {
                    return super.checkCanDoRefresh(frame, content, header);
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                CMPtrlRecyclerViewFragment.this.onRefreshBegin();
            }
        });

        loadMoreRecyclerViewContainer.useDefaultFooter();
        loadMoreRecyclerViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                CMPtrlRecyclerViewFragment.this.onLoadMore();
            }
        });
    }
    
    protected abstract void outerInit();

    protected abstract void onRefreshBegin();

    protected abstract void onLoadMore();

    public void setDividerHeight(final int height) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = AutoUtils.getPercentHeightSize(height);
            }
        });
    }

    protected void setShowLoadingForFirstPage(boolean showLoading) {
        loadMoreRecyclerViewContainer.setShowLoadingForFirstPage(showLoading);
    }

    public void refreshComplete() {
        ptrFrameLayout.refreshComplete();
    }

    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        loadMoreRecyclerViewContainer.loadMoreFinish(emptyResult, hasMore);
    }
}
