package com.steveq.gardenpieclient.presentation.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.presentation.Presenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;
import com.steveq.gardenpieclient.presentation.activities.presenters.MainActivityPresenterImpl;
import com.steveq.gardenpieclient.presentation.fragments.interfaces.SectionsFragmentPresenter;
import com.steveq.gardenpieclient.presentation.fragments.interfaces.SectionsFragmentView;
import com.steveq.gardenpieclient.presentation.fragments.presenters.SectionsFragmentPresenterImpl;
import com.steveq.gardenpieclient.requests.body_builder.JsonProcessor;

/**
 * A simple {@link Fragment} subclass.
 */
public class SectionsFragment extends Fragment implements SectionsFragmentView, Handler.Callback{
    private static final String TAG = SectionsFragment.class.getSimpleName();
    public Handler mainHandler;
    private SectionsFragmentPresenter presenter;

    public SectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ON CREATE FRAGMENT");
        presenter = new SectionsFragmentPresenterImpl(this);
        mainHandler = new Handler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "ON CREATE VIEW FRAGMENT");
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sections, container, false);
        return viewGroup;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "ON PAUSE FRAGMENT");
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "Message : " + new String((byte[])msg.obj));
        hideProgressBar();
        return true;
    }

    @Override
    public void showSectionsRecyclerView() {
    }

    @Override
    public void showProgressBar() {
        ((MainView)getActivity()).showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        ((MainView)getActivity()).hideProgressBar();
    }

    @Override
    public void showWarningSnackbar(String warningMessage) {
        ((MainView)getActivity()).showWarningSnackbar(warningMessage);
    }

    @Override
    public void dismissWarningSnackbar() {
        ((MainView)getActivity()).dismissWarningSnackbar();
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }
}
