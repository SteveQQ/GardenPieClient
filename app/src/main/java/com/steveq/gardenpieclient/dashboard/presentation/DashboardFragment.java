package com.steveq.gardenpieclient.dashboard.presentation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements DashboardFragmentView{
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private DashboardFragmentPresenter presenter;
    private boolean isViewShown = false;

    private SwipeRefreshLayout dashboardSwipe;
    private RecyclerView dashboardRecycler;
    private TextView emptyDashboard;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(isVisible()){
                presenter.getSensorsInfo();
                dashboardSwipe.setRefreshing(false);
            }
        }
    };

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DashboardFragmentPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardSwipe = (SwipeRefreshLayout) viewGroup.findViewById(R.id.swipeDashboardRefreshLayout);
        dashboardSwipe.setOnRefreshListener(onRefreshListener);

        dashboardRecycler = (RecyclerView) viewGroup.findViewById(R.id.dashboardRecyclerView);
        emptyDashboard = (TextView) viewGroup.findViewById(R.id.emptySensorsRecyclerViewReplacement);

        presenter.initView();

        if(isViewShown){
            presenter.getSensorsInfo();
        }

        return viewGroup;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "IS VISIBLE" + isVisibleToUser);
        Log.d(TAG, "VIEW" + getView());
//        if(getView() != null){
//            isViewShown = true;
//            presenter.getSensorsInfo();
//        }
        if(isVisibleToUser){
            isViewShown = true;
            if(getView() != null){
                presenter.getSensorsInfo();
            }
        } else {
            isViewShown = false;
        }
    }

    @Override
    public void showRecyclerView() {
        emptyDashboard.setVisibility(View.INVISIBLE);
        dashboardRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        emptyDashboard.setVisibility(View.VISIBLE);
        dashboardRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    public void configRecyclerView(RecyclerView.Adapter adapter) {
        dashboardRecycler.setHasFixedSize(true);
        dashboardRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        dashboardRecycler.setAdapter(adapter);
    }

    @Override
    public DashboardFragmentPresenter getPresenter() {
        return presenter;
    }
}
