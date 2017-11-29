package com.i76game.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.i76game.R;


public class SmoothListView2 extends ListView implements OnScrollListener {

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private ISmoothListViewListener mListViewListener;

    // -- header view
    private SmoothListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.

    /**
     * @param context
     */
    public SmoothListView2(Context context) {
        super(context);
        initWithContext(context);
    }

    public SmoothListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public SmoothListView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new SmoothListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.smoothlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.smoothlistview_header_time);
        addHeaderView(mHeaderView);

        // init footer view

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            //make sure "pull up" don't show a line in bottom when listview with one page
            setFooterDividersEnabled(false);
        } else {
            mPullLoading = false;
            //make sure "pull up" don't show a line in bottom when listview with one page
            setFooterDividersEnabled(true);
            // both "pull up" and "click" will invoke load more.
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnSmoothScrollListener) {
            OnSmoothScrollListener l = (OnSmoothScrollListener) mScrollListener;
            l.onSmoothScrolling(this);
        }
    }




    private void startLoadMore() {
        mPullLoading = true;
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0
                        && (deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    invokeOnScrolling();
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
            } else {
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setSmoothListViewListener(ISmoothListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onSmoothScrolling when header/footer scroll back.
     */
    public interface OnSmoothScrollListener extends OnScrollListener {
        void onSmoothScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface ISmoothListViewListener {
        void onRefresh();

        void onLoadMore();
    }
}