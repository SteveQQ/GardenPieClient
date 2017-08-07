package com.steveq.gardenpieclient.sections.presentation;

import com.steveq.gardenpieclient.communication.models.Section;

import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public interface SectionsFragmentPresenter {
    void initView();
    void collectDays(Section section, SectionsFragmentPresenterImpl.DaysListener listener);
    void collectTimes(Section section, SectionsFragmentPresenterImpl.TimesListener listener);
    void scanForSections();
    void presentSections(List<Integer> sections);
}
