package com.ms.android.base.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.ms.android.base.R;
import com.ms.android.base.widget.autolayout.AutoLinearLayout;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * 上下刷新RecyclerView
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class RefreshRecyclerView extends AutoLinearLayout {


    PtrClassicFrameLayout ptrFrameLayout;
    public RecyclerView recyclerView;
    private OnRefreshListener mRefreshListener;
    private boolean showLoading;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.widget_refresh_recycler_view, this);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    /**
     * 必须先初始化recyclerView.setAdapter
     */
    public void initReady() {
        // header
        final MaterialHeader header = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(ptrFrameLayout);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);

        //footer
//        StoreHouseHeader footer = new StoreHouseHeader(getContext());
//        footer.setPadding(0, PtrLocalDisplay.dp2px(20), 0, PtrLocalDisplay.dp2px(20));
//        footer.initWithString("Ultra Footer");
//
//        ptrFrameLayout.setFooterView(footer);
//        ptrFrameLayout.addPtrUIHandler(footer);

        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (mRefreshListener != null) mRefreshListener.onPullUpToRefresh();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mRefreshListener != null) mRefreshListener.onPullDownToRefresh();
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return showLoading && super.checkCanDoLoadMore(frame, recyclerView, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, recyclerView, header);
            }
        });

        // the following are default settings
        ptrFrameLayout.setResistance(1.7f); // you can also set foot and header separately
        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrameLayout.setDurationToClose(1000);  // you can also set foot and header separately
        // default is false
        ptrFrameLayout.setPullToRefresh(false);

        // default is true
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
    }

    public void setShowLoadingForFirstPage(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public void refreshComplete() {
        ptrFrameLayout.refreshComplete();
    }

    public void postDelayed(Runnable action) {
        ptrFrameLayout.postDelayed(action, 1000);
    }

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        return super.postDelayed(action, delayMillis);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        initReady();
    }

    public void setPadding(int start, int top, int end, int bottom) {
        recyclerView.setPadding(start,start,end,bottom);

    }


    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView .addOnScrollListener(listener);
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

    /**
     * 监听测试demo
     */
    private void listenere(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean loading = true;
            private int pastVisibleItems, visibleItemCount, totalItemCount;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
                int aa[] = layoutManager.findFirstVisibleItemPositions(null);
                float sum= recyclerView.getChildAt(0).getY();
                sum=Math.abs(sum);
                if (dy>0){//时为手指向上滚动,列表滚动显示下面的内容
//                    title.setVisibility(View.GONE);
                }else {//时为手指向下滚动,列表滚动显示上面的内容
                    if (aa[0]==0&&sum<100){//顶部第一个和小于Y:100
//                        title.setVisibility(View.GONE);
                    }else {
//                        title.setVisibility(View.VISIBLE);
                    }
                }

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }
                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                    }
                }
            }
        });
    }

}
