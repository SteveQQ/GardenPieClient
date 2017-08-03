package com.steveq.gardenpieclient.presentation;

/**
 * Created by Adam on 2017-07-27.
 */

public interface SuperView {
    void showProgressBar();
    void hideProgressBar();
    void showWarningSnackbar(String warningMessage);
    void dismissWarningSnackbar();
    Presenter getPresenter();
}
