package com.ms.android.newlife.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.android.base.adapter.BaseViewHolder;
import com.ms.android.base.adapter.QuickRecyclerAdapter;
import com.ms.android.base.fragment.BaseFragment;
import com.ms.android.base.fragment.CMPtrlRecyclerViewFragment;
import com.ms.android.base.utils.task.TaskHelper;
import com.ms.android.base.widget.autolayout.utils.AutoUtils;
import com.ms.android.base.widget.lazy.LazyFragmentPagerAdapter;
import com.ms.android.newlife.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * 任务页面
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class TaskFragment extends CMPtrlRecyclerViewFragment implements LazyFragmentPagerAdapter.Laziable{
//    @Override
//    public int getLayoutRes() {
//        return R.layout.fragment_task;
//    }
//
//    @Override
//    protected void init(View contentView, Bundle savedInstanceState) {
//        Logger.e(this.getClass().getName());
//    }

    private QuickRecyclerAdapter<String> adapter;
    List list;
    @Override
    protected void outerInit() {
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
        recyclerView.setAdapter(adapter);
        list=new ArrayList();
        for (int i = 0; i <12 ; i++) {
            list.add("第" +i+"设置提下次");
        }
        adapter.setDataList(list);
    }

    @Override
    protected void onRefreshBegin() {
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
        setShowLoadingForFirstPage(true);
        refreshComplete();
    }

    @Override
    protected void onLoadMore() {
        TaskHelper.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPage < 3) {
                    loadMoreFinish(true, true);
                    list.clear();
                    for (int i = 0; i < 5; i++) {
                        list.add("地方地方似懂非懂");
                    }
                    adapter.clear();
                    adapter.addAll(list);
                } else {
                    loadMoreFinish(false, false);
                }
                mPage++;
            }
        }, 1000);
    }
}
