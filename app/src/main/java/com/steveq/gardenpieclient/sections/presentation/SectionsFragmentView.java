package com.steveq.gardenpieclient.sections.presentation;

import android.support.v7.widget.RecyclerView;

import com.steveq.gardenpieclient.communication.models.Section;

/**
 * Created by Adam on 2017-08-03.
 */

public interface SectionsFragmentView {
    void showRecyclerView();
    void hideRecyclerView();
    void showDaysDialog();
    void showSectionsRecyclerView();
    void showTimesDialog(Section section);
    void configRecyclerView(RecyclerView.Adapter adapter);
    SectionsFragmentPresenter getPresenter();
}
