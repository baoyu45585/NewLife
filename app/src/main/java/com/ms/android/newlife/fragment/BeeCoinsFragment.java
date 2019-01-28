package com.ms.android.newlife.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.android.base.adapter.BaseViewHolder;
import com.ms.android.base.adapter.QuickRecyclerAdapter;
import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.base.utils.task.TaskHelper;

import com.ms.android.base.widget.RefreshRecyclerView;
import com.ms.android.base.widget.autolayout.utils.AutoUtils;
import com.ms.android.base.widget.lazy.LazyFragmentPagerAdapter;
import com.ms.android.newlife.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 蜂币页面
 *
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class BeeCoinsFragment extends BaseFragment implements LazyFragmentPagerAdapter.Laziable {


    @BindView(R.id.refresh)
    RefreshRecyclerView refresh;
    private int mPage=0;
    List list;
    private QuickRecyclerAdapter<String> adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_bee_coins;
    }

    @Override
    protected void init(View contentView, Bundle savedInstanceState) {
        refresh.setLayoutManager(new LinearLayoutManager(getContext()));        //初始化监听事件
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(180)));
        emptyView.setText("empty result");
        emptyView.setGravity(Gravity.CENTER);
        adapter=new QuickRecyclerAdapter<String>(R.layout.demo_item_view) {
            @Override
            protected void onBindView(BaseViewHolder holder, final int position, String item) {
                ImageView mg= (ImageView) holder.getView().findViewById(R.id.image);
                TextView textName= (TextView) holder.getView().findViewById(R.id.text_name);
                mg.setImageResource(R.mipmap.ic_launcher);
                textName.setText(item);
            }
        };
        adapter.setEmptyView(emptyView);
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
                        refresh.setShowLoadingForFirstPage(false);
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
        for (int i = 0; i <12 ; i++) {
            list.add("第" +i+"设置提下次");
        }
        adapter.setDataList(list);

    }


}
