package com.ms.android.base.widget.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.ms.android.base.adapter.QuickRecyclerAdapter;



public class LoadMoreRecyclerViewContainer extends LinearLayout implements LoadMoreContainer {
    private RecyclerView.OnScrollListener mOnScrollListener;
    private LoadMoreUIHandler mLoadMoreUIHandler;
    private LoadMoreHandler mLoadMoreHandler;
    private boolean mIsLoading;
    private boolean mHasMore = false;
    private boolean mAutoLoadMore = true;
    private boolean mLoadError = false;
    private boolean mListEmpty = true;
    private boolean mShowLoadingForFirstPage = false;
    private View mFooterView;
    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public LoadMoreRecyclerViewContainer(Context context) {
        super(context);
    }

    public LoadMoreRecyclerViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.recyclerView = retrieveAbsListView();
        setOrientation(VERTICAL);
        this.init();
    }

    public void useDefaultFooter() {
        LoadMoreDefaultFooterView footerView = new LoadMoreDefaultFooterView(this.getContext());
        footerView.setVisibility(GONE);
        this.setLoadMoreView(footerView);
        this.setLoadMoreUIHandler(footerView);
    }

    private void init() {
        if (this.mFooterView != null) {
            this.addFooterView(this.mFooterView);
        }
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean mIsEnd = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null != LoadMoreRecyclerViewContainer.this.mOnScrollListener) {
                    LoadMoreRecyclerViewContainer.this.mOnScrollListener.onScrollStateChanged(recyclerView, newState);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && this.mIsEnd) {
                    LoadMoreRecyclerViewContainer.this.onReachBottom();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != LoadMoreRecyclerViewContainer.this.mOnScrollListener) {
                    LoadMoreRecyclerViewContainer.this.mOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
                mIsEnd = isSlideToBottom(recyclerView);
            }
        });
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void tryToPerformLoadMore() {
        if (mIsLoading) {
            return;
        }

        // no more content and also not load for first page
        if (!mHasMore && !(mListEmpty && mShowLoadingForFirstPage)) {
            return;
        }

        mIsLoading = true;

        if (mLoadMoreUIHandler != null) {
            mLoadMoreUIHandler.onLoading(this);
        }
        if (null != mLoadMoreHandler) {
            mLoadMoreHandler.onLoadMore(this);
        }
    }

    private void onReachBottom() {
        if (!this.mLoadError) {
            if (this.mAutoLoadMore) {
                this.tryToPerformLoadMore();
            } else if (this.mHasMore) {
                this.mLoadMoreUIHandler.onWaitToLoadMore(this);
            }

        }
    }

    /**
     * 是否加载首页数据
     *
     * @param showLoading 默认为false,下拉刷新数据有值时需要配置为true
     */
    public void setShowLoadingForFirstPage(boolean showLoading) {
        this.mShowLoadingForFirstPage = showLoading;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.mAutoLoadMore = autoLoadMore;
    }

    @Deprecated
    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {

    }

    public void setOnScrollListener(RecyclerView.OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    public void setLoadMoreView(View view) {
        if (this.recyclerView == null) {
            this.mFooterView = view;
        } else {
            if (this.mFooterView != null && this.mFooterView != view) {
                this.removeFooterView(view);
            }

            this.mFooterView = view;
            this.mFooterView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    LoadMoreRecyclerViewContainer.this.tryToPerformLoadMore();
                }
            });
            this.addFooterView(view);
        }
    }

    public void setLoadMoreUIHandler(LoadMoreUIHandler handler) {
        this.mLoadMoreUIHandler = handler;
    }

    public void setLoadMoreHandler(LoadMoreHandler handler) {
        this.mLoadMoreHandler = handler;
    }

    /**
     * 是否还有下一页数据
     *
     * @param emptyResult
     * @param hasMore
     */
    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        this.mLoadError = false;
        this.mListEmpty = emptyResult;
        this.mIsLoading = false;
        this.mHasMore = hasMore;
        if (this.mLoadMoreUIHandler != null) {
            this.mLoadMoreUIHandler.onLoadFinish(this, emptyResult, hasMore);
        }
    }

    public void loadMoreError(int errorCode, String errorMessage) {
        this.mIsLoading = false;
        this.mLoadError = true;
        if (this.mLoadMoreUIHandler != null) {
            this.mLoadMoreUIHandler.onLoadError(this, errorCode, errorMessage);
        }

    }

    protected void addFooterView(View var1) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof QuickRecyclerAdapter) {
            ((QuickRecyclerAdapter) adapter).setFooterView(var1);
        }
    }

    protected void removeFooterView(View var1) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof QuickRecyclerAdapter) {
            ((QuickRecyclerAdapter) adapter).setFooterView(null);
        }
    }

    protected RecyclerView retrieveAbsListView() {
        View childAt = getChildAt(0);
        if (childAt != null && childAt instanceof RecyclerView) {
            return (RecyclerView) childAt;
        } else {
            RecyclerView recyclerView = new RecyclerView(getContext());
            addView(recyclerView, 0, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return recyclerView;
        }
    }
}