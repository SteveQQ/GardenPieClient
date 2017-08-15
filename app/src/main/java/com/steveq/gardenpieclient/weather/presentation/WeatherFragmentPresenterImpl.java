package com.steveq.gardenpieclient.weather.presentation;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.communication.JsonProcessor;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.main_view.presentation.MainActivity;
import com.steveq.gardenpieclient.main_view.presentation.MainActivityPresenterImpl;
import com.steveq.gardenpieclient.weather.model.WeatherModel;
import com.steveq.gardenpieclient.weather.model.WeatherOutputModel;

/**
 * Created by Adam on 2017-08-13.
 */

public class WeatherFragmentPresenterImpl implements WeatherFragmentPresenter {
    private static final String TAG = WeatherFragmentPresenterImpl.class.getSimpleName();
    private WeatherFragmentView mainView;
    private static BaseActivity parentActivity;
    private ConnectionHelper connectionHelper;
    private WeatherOutputModel currentWeather;


    public WeatherFragmentPresenterImpl(WeatherFragmentView mainView){
        this.mainView = mainView;
        this.parentActivity = (BaseActivity)((Fragment)this.mainView).getActivity();
    }

    @Override
    public void initView() {
    }

    @Override
    public void getWeatherInfo() {
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
        }, 7000);
        connectionHelper = MainActivityPresenterImpl.connectionHelper;
        parentActivity.showProgressBar();
        String message = JsonProcessor.getInstance().createWeatherRequest(parentActivity);
        if(connectionHelper != null){
            connectionHelper.sendMessage(message);
        }
    }

    @Override
    public void presentWeatherInfo() {
        if(currentWeather != null){
            mainView.refreshWeatherData(currentWeather);
        }
    }

    @Override
    public void acknowledgeWeatherData(WeatherOutputModel data) {
        currentWeather = data;
    }
}
