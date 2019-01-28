package com.ms.android.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.android.base.R;
import com.ms.android.base.widget.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/10/20.
 * 自定义TabHost
 */

public class MyTabHost extends LinearLayout {

    public interface TabSelectedChange {
        void onTabSelectChange(int index);
    }

    private TabSelectedChange tabSelectedChange;
    private int mLastSelectIndex = -1;

    public void setTabSelectedChange(TabSelectedChange tabSelectedChange) {
        this.tabSelectedChange = tabSelectedChange;
    }

    private List<TabWidget> tabWidgets = new ArrayList<>();

    public MyTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void clearCheck() {
        for (TabWidget tabWidget : tabWidgets) {
            tabWidget.setSelected(false);
        }
    }

    public void showUnReadUI(String unReadMsg, int index) {
        if (index < 0 || index > tabWidgets.size()) return;
        tabWidgets.get(index).setUnreadMsg(unReadMsg);
    }


    public void setCurrentTab(int index) {
        if (index < 0 || index > tabWidgets.size()) return;
        if (mLastSelectIndex == index) return;
        clearCheck();
        TabWidget tabWidget = tabWidgets.get(index);
        tabWidget.setSelected(true);
        if (tabSelectedChange != null) {
            tabSelectedChange.onTabSelectChange(index);
        }
        mLastSelectIndex = index;
    }

    public int getLastSelectIndex() {
        return mLastSelectIndex;
    }

    public void setLastSelectIndex(int mLastSelectIndex) {
        this.mLastSelectIndex = mLastSelectIndex;
    }

    public void hideUnReadUI(int index) {
        if (index < 0 || index > tabWidgets.size()) return;
        tabWidgets.get(index).hidePointView();
    }

    public void setup(List<String> names, List<Integer> iconResId) {
        if (names != null) {
            removeAllViews();
            tabWidgets.clear();
            for (int i = 0; i < names.size(); i++) {
                final TabWidget tabWidget = new TabWidget(getContext());
                tabWidget.setTextView(names.get(i));
                if (iconResId != null) {
                    tabWidget.setImageView(iconResId.get(i));
                }
                final int finalI = i;
                tabWidget.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setCurrentTab(finalI);
                    }
                });
                tabWidgets.add(tabWidget);
                addView(tabWidget, generateDefaultLayoutParams());
            }
        } else {
            throw new IllegalArgumentException("illegal argument that name or frags is null,else check the names length is equal with frags's length");
        }
    }

    public void addTab(final TabWidget tabWidget) {
        tabWidgets.add(tabWidget);
        tabWidget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentTab(tabWidgets.indexOf(tabWidget));
                addView(tabWidget, generateDefaultLayoutParams());
            }
        });
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        return params;
    }

    private class TabWidget extends AutoLinearLayout {
        private TextView textView;
        private TextView tvPoint;
        private ImageView imageView;
        private boolean isChecked;
        private View frameLayout;
        private static final int DURATION = 500;
        AccelerateDecelerateInterpolator interpolator;


        public TabWidget(Context context) {
            this(context, null);
        }

        public TabWidget(Context context, AttributeSet attrs) {
            super(context, attrs);
            inflate(context, R.layout.widget_tab_widget, this);
            tvPoint = (TextView) findViewById(R.id.tv_point);
            textView = (TextView) findViewById(R.id.tv_name);
            frameLayout = findViewById(R.id.frameLayout);
            imageView = (ImageView) findViewById(R.id.iv_logo);
//            setPadding(0, 0, 0, AutoLayout.getInstance().getOrientationAutoSize(12, false, true));
            interpolator = new AccelerateDecelerateInterpolator();
        }

        @Override
        public void setSelected(final boolean selected) {
            super.setSelected(selected);
//            textView.measure(0, 0);
//            int measuredHeight = textView.getMeasuredHeight();
            if (selected == isChecked) return;
//            if (selected) {
//                popAnim(measuredHeight / 2);
//            } else {
//                dismissAnim(measuredHeight / 2);
//            }
            if (selected) {
                textView.setTextColor(getResources().getColor(R.color.rgb_01_a60));
            } else {
                textView.setTextColor(getResources().getColor(R.color.rgb_01));
            }
            isChecked = selected;
        }

        /**
         * 显示位移距离
         *
         * @param measuredHeight 位移距离
         */
        private void popAnim(int measuredHeight) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -measuredHeight);
            animation.setDuration(DURATION);
            animation.setInterpolator(interpolator);
            animation.setFillAfter(true);
            frameLayout.startAnimation(animation);
            Animation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
            alphaAnim.setDuration(DURATION);
            alphaAnim.setInterpolator(interpolator);
            alphaAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    textView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            alphaAnim.setFillAfter(true);
            textView.startAnimation(alphaAnim);
        }

        /**
         * 取消选中动画
         *
         * @param measuredHeight 位移距离
         */
        private void dismissAnim(int measuredHeight) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, -measuredHeight, 0);

            animation.setDuration(DURATION);
            animation.setInterpolator(interpolator);
//            animation.setFillAfter(true);
            frameLayout.startAnimation(animation);
            Animation alphaAnim = new AlphaAnimation(1.0f, 0.0f);
            alphaAnim.setDuration(DURATION / 2);
            alphaAnim.setInterpolator(interpolator);
            alphaAnim.setFillAfter(true);
            alphaAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    textView.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            textView.startAnimation(alphaAnim);
        }

        public void setTextView(String textView) {
            this.textView.setText(textView);
        }

        public void setImageView(Integer imageView) {
            this.imageView.setImageResource(imageView);
        }

        public void setUnreadMsg(String unReadMsg) {
            tvPoint.setVisibility(VISIBLE);
//            tvPoint.setText(unReadMsg);
        }

        public void hidePointView() {
            tvPoint.setText("");
            tvPoint.setVisibility(GONE);
        }
    }
}
