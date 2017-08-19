package com.steveq.gardenpieclient.dashboard.presentation;

import android.support.v7.widget.RecyclerView;

import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenter;

/**
 * Created by Adam on 2017-08-19.
 */

public interface DashboardFragmentView {
    void showRecyclerView();
    void hideRecyclerView();
    void configRecyclerView(RecyclerView.Adapter adapter);
    DashboardFragmentPresenter getPresenter();
}
