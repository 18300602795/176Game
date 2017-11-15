package com.i76game.view;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.i76game.R;


/**
 * Created by sunfusheng on 16/4/20.
 */
public class HeaderFilterView_home extends AbsHeaderView<Object> {
   public FilterView_home fakeFilterView;

    public HeaderFilterView_home(Activity context) {
        super(context);
    }

    @Override
    protected void getView(Object obj, ListView listView) {
        View view = mInflate.inflate(R.layout.header_filter_layout_home, listView, false);
        fakeFilterView = (FilterView_home) view.findViewById(R.id.fake_filterView);
        listView.addHeaderView(view);
    }

    public FilterView_home getFilterView() {
        return fakeFilterView;
    }

}
