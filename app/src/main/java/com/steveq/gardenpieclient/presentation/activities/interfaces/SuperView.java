package com.steveq.gardenpieclient.presentation.activities.interfaces;

/**
 * Created by Adam on 2017-07-27.
 */

public interface SuperView {
    void showProgressBar();
    void hideProgressBar();
    void showWarningSnackbar(String warningMessage);
}
