package com.ms.android.base.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ms.android.base.R;


public class LoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIHandler {
    private TextView mTextView;

    public LoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.refresh_default_footer, this);
        this.mTextView = (TextView) this.findViewById(R.id.cube_views_load_more_default_footer_text_view);
    }

    public void onLoading(LoadMoreContainer container) {
        this.setVisibility(VISIBLE);
        this.mTextView.setText(R.string.cube_views_load_more_loading);
    }

    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (!hasMore) {
            this.setVisibility(VISIBLE);
            if (empty) {
                this.mTextView.setText(R.string.cube_views_load_more_loaded_empty);
            } else {
                this.mTextView.setText(R.string.cube_views_load_more_loaded_no_more);
            }
        } else {
            this.setVisibility(INVISIBLE);
        }
    }

    public void onWaitToLoadMore(LoadMoreContainer container) {
        this.setVisibility(VISIBLE);
        this.mTextView.setText(R.string.cube_views_load_more_click_to_load_more);
    }

    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        this.mTextView.setText(R.string.cube_views_load_more_error);
    }
}
