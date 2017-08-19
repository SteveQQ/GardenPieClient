package com.steveq.gardenpieclient.dashboard.presentation;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.communication.JsonProcessor;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.communication.models.Sensor;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.dashboard.adapters.DashboardRecyclerViewAdapter;
import com.steveq.gardenpieclient.main_view.presentation.MainActivity;
import com.steveq.gardenpieclient.main_view.presentation.MainActivityPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-19.
 */

public class DashboardFragmentPresenterImpl implements DashboardFragmentPresenter {
    private static final String TAG = DashboardFragmentPresenterImpl.class.getSimpleName();
    private static DashboardFragmentView mainView;
    private static BaseActivity parentActivity;
    private ConnectionHelper connectionHelper;
    public static RecyclerView.Adapter dashboardAdapter;

    public DashboardFragmentPresenterImpl(DashboardFragmentView mView){
        mainView = mView;
        parentActivity = (BaseActivity)((Fragment)this.mainView).getActivity();
    }

    @Override
    public void initView() {
        dashboardAdapter = new DashboardRecyclerViewAdapter(parentActivity, new ArrayList<Sensor>());
        mainView.configRecyclerView(dashboardAdapter);
        if(dashboardAdapter.getItemCount() > 0){
            mainView.showRecyclerView();
        } else if(dashboardAdapter.getItemCount() == 0){
            mainView.hideRecyclerView();
        }
    }

    @Override
    public void getSensorsInfo() {
        HandlerThread abortingThread = new HandlerThread("abortConnection");
        abortingThread.start();
        Handler handler = new Handler(abortingThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "ABORT RECEIVING MSG???");
                if(!MainActivity.receivedMsg){
                    Log.d(TAG, "ABORT RECEIVING MSG!!!");
                    parentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parentActivity.hideProgressBar();
                        }
                    });
                    return;
                }
            }
        }, 30000);
        connectionHelper = MainActivityPresenterImpl.connectionHelper;
        parentActivity.showProgressBar();
        String message = JsonProcessor.getInstance().createSensorsRequest();
        if(connectionHelper != null){
            connectionHelper.sendMessage(message);
        }
    }

    @Override
    public void acknowledgeSensorsData(List<Sensor> sensors) {
        ((DashboardRecyclerViewAdapter) dashboardAdapter).setPayload(sensors);
        dashboardAdapter.notifyDataSetChanged();
        if (dashboardAdapter.getItemCount() > 0) {
            mainView.showRecyclerView();
        }
    }
}
