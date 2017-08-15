package com.steveq.gardenpieclient.weather.presentation;


import android.icu.text.DecimalFormat;
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
import com.steveq.gardenpieclient.weather.model.WeatherModel;
import com.steveq.gardenpieclient.weather.model.WeatherOutputModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements WeatherFragmentView{
    public static String TAG = WeatherFragment.class.getSimpleName();
    private WeatherFragmentPresenter presenter;
    private boolean isViewShown = false;
    private SwipeRefreshLayout weatherSwipeRefresh;

    private TextView currentlySummaryTextView;
    private TextView currentlyTemperatureTextView;
    private TextView currentlyHumidityTextView;

    private TextView forecastSummaryTextView;
    private TextView forecastTemperatureTextView;
    private TextView forecastHumidityTextView;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.getWeatherInfo();
            weatherSwipeRefresh.setRefreshing(false);
        }
    };

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "ON CREATE");
        super.onCreate(savedInstanceState);
        presenter = new WeatherFragmentPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "ON CREATE VIEW");
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_weather, container, false);

        weatherSwipeRefresh = (SwipeRefreshLayout) viewGroup.findViewById(R.id.swipeRefreshWeather);
        weatherSwipeRefresh.setOnRefreshListener(onRefreshListener);

        currentlySummaryTextView = (TextView) viewGroup.findViewById(R.id.currentlySummaryTextView);
        currentlyTemperatureTextView = (TextView) viewGroup.findViewById(R.id.currentlyTemperatureTextView);
        currentlyHumidityTextView = (TextView) viewGroup.findViewById(R.id.currentlyHumidityTextView);
        forecastSummaryTextView = (TextView) viewGroup.findViewById(R.id.forecastSummaryTextView);
        forecastTemperatureTextView = (TextView) viewGroup.findViewById(R.id.forecastTemperatureTextView);
        forecastHumidityTextView = (TextView) viewGroup.findViewById(R.id.forecastHumidityTextView);

        if(!isViewShown){
            presenter.getWeatherInfo();
        }

        return viewGroup;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "IS VISIBLE" + isVisibleToUser);
        if(getView() != null){
            isViewShown = true;
            presenter.getWeatherInfo();
        }
        if(isVisibleToUser){
            isViewShown = false;
        }
    }

    @Override
    public void refreshWeatherData(WeatherOutputModel data) {
        currentlySummaryTextView.setText(data.getCurrently().getSummary());
        currentlyTemperatureTextView.setText(String.format("%.2f", fahrToCels(data.getCurrently().getTemperature())) + "\u00b0" + "C");
        currentlyHumidityTextView.setText(String.valueOf(Math.round(data.getCurrently().getHumidity() * 100)) + " %");

        forecastSummaryTextView.setText(data.getHourly().getData().getSummary());
        forecastTemperatureTextView.setText(String.format("%.2f", fahrToCels(data.getCurrently().getTemperature())) + "\u00b0" + "C");
        forecastHumidityTextView.setText(String.valueOf(Math.round(data.getHourly().getData().getHumidity() * 100)) + " %");
    }

    @Override
    public WeatherFragmentPresenter getPresenter() {
        return presenter;
    }

    private double fahrToCels(double cels){
        return (cels - 32)/1.8;
    }
}
