package com.steveq.gardenpieclient.main_view.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.connection.ConnectionFactory;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.main_view.adapters.MyPagerAdapter;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragment;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenterImpl;

/**
 * Created by Adam on 2017-07-19.
 */

public class MainActivityPresenterImpl implements MainActivityPresenter {
    private static final String TAG = MainActivityPresenterImpl.class.getSimpleName();
    private static MainActivityPresenterImpl instance;
    private MainView mainView;
    public static ConnectionHelper connectionHelper;
    private ConnectionFactory connectionFactory;

    private MainActivityPresenterImpl(MainView mainActivity){
        this.mainView = mainActivity;
    }

    public static MainActivityPresenterImpl getInstance(){
        if(instance == null){
            throw new IllegalStateException("Presenter need to be first instantiated with proper context, use getInstance( MainView );");
        }
        return instance;
    }

    public static MainActivityPresenterImpl getInstance(MainView mainView){
        if(instance == null){
            instance = new MainActivityPresenterImpl(mainView);
        }
        return instance;
    }

    @Override
    public void initView() {
        mainView.prepareViews();
    }

    @Override
    public void initConnection() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)mainView);
        String prefConnection = sharedPreferences.getString(((Context)mainView).getString(R.string.connection_option_pref_key_str), "");

        connectionFactory = new ConnectionFactory((BaseActivity) mainView);
        ConnectionHelper connectionHelperTmp = connectionFactory.getConnectionHelper(prefConnection);

        if(connectionHelper == null){
            connectionHelper = connectionHelperTmp;
        } else if (!connectionHelperTmp.getClass().equals(connectionHelper.getClass())){
            Log.d(TAG, "SWAP CONNECTION SOURCE");
            connectionHelper.disconnect();
            connectionHelper = connectionHelperTmp;
        }
    }

    @Override
    public void establishConnection() {
        ((BaseActivity)mainView).dismissWarningSnackbar();
        ((BaseActivity)mainView).showProgressBar();
        if(connectionHelper != null){
            Log.d(TAG, "ESTABLISHING CONNECTION");
            Log.d(TAG, connectionHelper.toString());
            //connectionHelper.connect(new Handler(((SectionsFragmentPresenterImpl)((SectionsFragment)MyPagerAdapter.fragmentsPoll.get(1)).getPresenter())));
            connectionHelper.connect(BaseActivity.mainHandler);
        } else {
            ((BaseActivity)mainView).hideProgressBar();
            ((BaseActivity)mainView).showWarningSnackbar(((Activity)mainView).getString(R.string.no_connection_warning_str));
        }
    }

    @Override
    public void sendMessageToServer() {
        if(connectionHelper != null && connectionHelper.isConnected()){
            connectionHelper.sendMessage("hello world");
        } else {
            ((BaseActivity)mainView).showWarningSnackbar(((Activity)mainView).getString(R.string.no_connection_warning_str));
        }
    }

    @Override
    public void stopConnection() {
        if(connectionHelper != null){
            connectionHelper.disconnect();
        }
    }

    @Override
    public ConnectionHelper getConnectionHelper() {
        return connectionHelper;
    }
}
