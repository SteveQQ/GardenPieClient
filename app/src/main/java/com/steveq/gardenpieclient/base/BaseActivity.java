package com.steveq.gardenpieclient.base;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.steveq.gardenpieclient.main_view.adapters.MyPagerAdapter;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragment;

public abstract class BaseActivity extends AppCompatActivity {

    public static Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void hideProgressBar();
    public abstract void showWarningSnackbar(String warningMessage);
    public abstract void showWarningSnackbarWithAction(String warningMessage, String actionString, View.OnClickListener listener);
    public abstract void showProgressBar();
    public abstract void dismissWarningSnackbar();
}
