package com.steveq.gardenpieclient.sections.presentation;

import com.steveq.gardenpieclient.communication.models.Section;

import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public interface SectionsFragmentPresenter {
    void initView();
    void collectTimes();
    void scanForSections();
    void presentSections(List<Integer> sections);
}
