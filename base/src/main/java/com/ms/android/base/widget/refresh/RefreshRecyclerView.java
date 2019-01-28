package com.ms.android.base.widget.refresh;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.ms.android.base.R;
import com.ms.android.base.fragment.CMPtrlRecyclerViewFragment;
import com.ms.android.base.widget.autolayout.AutoLinearLayout;
import com.ms.android.base.widget.autolayout.utils.AutoUtils;
import com.ms.android.base.widget.refresh.LoadMoreContainer;
import com.ms.android.base.widget.refresh.LoadMoreHandler;
import com.ms.android.base.widget.refresh.LoadMoreRecyclerViewContainer;
import com.orhanobut.logger.Logger;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class RefreshRecyclerView extends AutoLinearLayout {

    LoadMoreRecyclerViewContainer loadMoreRecyclerViewContainer;
    PtrFrameLayout ptrFrameLayout;
    public RecyclerView recyclerView;
    protected OnRefreshListener mRefreshListener;
    private boolean isReady;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        inflate(getContext(), R.layout.frag_cm_ptrl_recyclerview, this);
        loadMoreRecyclerViewContainer = (LoadMoreRecyclerViewContainer) findViewById(R.id.load_more_recycler_view);
        ptrFrameLayout = (PtrFrameLayout)findViewById(R.id.load_more_list_view_ptr_frame);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    /**
     * 必须先初始化recyclerView.setAdapter
     */
    public void initReady(){
        isReady=true;
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
                if (mRefreshListener!=null)mRefreshListener.onPullDownToRefresh();

            }
        });

        loadMoreRecyclerViewContainer.useDefaultFooter();
        loadMoreRecyclerViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                if (mRefreshListener!=null)mRefreshListener.onPullUpToRefresh();
            }
        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout){
        recyclerView.setLayoutManager(layout);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        if (!isReady)initReady();

    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }



    public void setDividerHeight(final int height) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = AutoUtils.getPercentHeightSize(height);
            }
        });
    }

    public void setShowLoadingForFirstPage(boolean showLoading) {
        loadMoreRecyclerViewContainer.setShowLoadingForFirstPage(showLoading);
    }

    public void refreshComplete() {
        ptrFrameLayout.refreshComplete();
    }

    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        loadMoreRecyclerViewContainer.loadMoreFinish(emptyResult, hasMore);
    }


    public interface OnRefreshListener {

        /**
         * 下拉松手后会被调用
         */
        void onPullDownToRefresh();

        /**
         * 加载更多时会被调用或上拉时调用
         */
        void onPullUpToRefresh();
    }
}
