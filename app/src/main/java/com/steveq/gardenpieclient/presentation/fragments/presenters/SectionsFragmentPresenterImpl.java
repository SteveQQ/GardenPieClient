package com.steveq.gardenpieclient.presentation.fragments.presenters;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;
import com.steveq.gardenpieclient.presentation.activities.presenters.MainActivityPresenterImpl;
import com.steveq.gardenpieclient.presentation.fragments.interfaces.SectionsFragmentPresenter;
import com.steveq.gardenpieclient.presentation.fragments.interfaces.SectionsFragmentView;
import com.steveq.gardenpieclient.requests.body_builder.JsonProcessor;

/**
 * Created by Adam on 2017-08-03.
 */

public class SectionsFragmentPresenterImpl implements SectionsFragmentPresenter {
    private static final String TAG = SectionsFragmentPresenterImpl.class.getSimpleName();
    private ConnectionHelper connectionHelper;
    private SectionsFragmentView mainView;

    public SectionsFragmentPresenterImpl(SectionsFragmentView mainView){
        this.mainView = mainView;
        connectionHelper = ((MainActivityPresenterImpl)((MainView)((Fragment)mainView).getActivity()).getPresenter()).getConnectionHelper();
    }

    @Override
    public void scanForSections() {
        mainView.showProgressBar();
        String message = JsonProcessor.getInstance().createScanRequest();
        connectionHelper.sendMessage(message);
    }
}
