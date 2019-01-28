package com.ms.android.newlife.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.android.base.adapter.BaseViewHolder;
import com.ms.android.base.adapter.QuickRecyclerAdapter;
import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.base.utils.task.TaskHelper;
import com.ms.android.base.widget.RefreshRecyclerView;
import com.ms.android.base.widget.SpacesItemDecoration;
import com.ms.android.base.widget.autolayout.config.AutoLayoutConifg;
import com.ms.android.base.widget.autolayout.utils.AutoUtils;
import com.ms.android.base.widget.lazy.LazyFragmentPagerAdapter;
import com.ms.android.newlife.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class MainFragment extends BaseFragment implements LazyFragmentPagerAdapter.Laziable {

    @BindView(R.id.title)
    View title;
    @BindView(R.id.refresh)
    RefreshRecyclerView refresh;
    private int mPage=0;
    List list;
    private QuickRecyclerAdapter<String> adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(View contentView, Bundle savedInstanceState) {
        refresh.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));        //初始化监听事件
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(180)));
        emptyView.setText("empty result");
        emptyView.setGravity(Gravity.CENTER);

        adapter=new QuickRecyclerAdapter<String>(R.layout.demo_item_view) {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position, String item) {

                ImageView mg= (ImageView) holder.getView().findViewById(R.id.image);
//                //获取item宽度，计算图片等比例缩放后的高度，为imageView设置参数
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mg.getLayoutParams();
//                float itemWidth = (AutoLayoutConifg.getInstance().getScreenWidth() - 8*3) / 2;
//                layoutParams.width = (int) itemWidth;
////                float scale = (itemWidth+0f)/book.width;
//                layoutParams.height= 100;
//                mg.setLayoutParams(layoutParams);
                mg.setImageResource(R.mipmap.ic_launcher);

                TextView textName= (TextView) holder.getView().findViewById(R.id.text_name);
                textName.setText(item);
            }
        };
        adapter.setEmptyView(emptyView);

        refresh.setPadding(8,8,8,8);
        refresh.addItemDecoration(new SpacesItemDecoration(8));
        refresh.setAdapter(adapter);
        refresh.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                Logger.e("onPullDownToRefresh");
                mPage = 0;
                list.clear();
                list.add("大概打工的");
                list.add("地方地方似懂非懂");
//                list.add("大概打工的");
//                list.add("地方地方似懂非懂");
//                list.add("大概打工的");
//                list.add("地方地方似懂非懂");
//                list.add("大概打工的");
//                list.add("地方地方似懂非懂");
                adapter.clear();
                adapter.addAll(list);
                refresh.setShowLoadingForFirstPage(true);
                refresh.refreshComplete();
            }

            @Override
            public void onPullUpToRefresh() {
                Logger.e("onPullUpToRefresh");
                refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.refreshComplete();
                    }
                });

//                TaskHelper.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mPage < 3) {
//                            refresh.loadMoreFinish(true, true);
//                            list.clear();
//                            for (int i = 0; i < 10; i++) {
//                                list.add("地方地方似懂非懂");
//                            }
//                            adapter.clear();
//                            adapter.addAll(list);
//                        } else {
//                            refresh.loadMoreFinish(false, false);
//                        }
//                        mPage++;
//                    }
//                }, 1000);

            }
        });



        list=new ArrayList();
        String content="我的";
        for (int i = 0; i <11 ; i++) {
            content+=content;
            list.add("第" +i+"设置提下次"+content);
        }
        adapter.setDataList(list);




    }



}

