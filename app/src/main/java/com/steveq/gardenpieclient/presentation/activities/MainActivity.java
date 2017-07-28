package com.steveq.gardenpieclient.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainActivityPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;
import com.steveq.gardenpieclient.presentation.activities.presenters.MainActivityPresenterImpl;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button sendMessageButton;
    private Toolbar mainToolbar;
    private LinearLayout rootView;
    private ProgressBar connectionProgressBar;
    private MainActivityPresenter presenter;
    public SwipeRefreshLayout swipeRefreshLayout;

    private View.OnClickListener sendMessageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.sendMessageToServer();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.establishConnection();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        rootView = (LinearLayout) findViewById(R.id.rootLinearLayout);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(sendMessageClickListener);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        connectionProgressBar = (ProgressBar) findViewById(R.id.connectionProgressBar);
        presenter = MainActivityPresenterImpl.getInstance(this);
        presenter.initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.initConnection();
        presenter.establishConnection();
        Log.d(TAG, "ON RESUME");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settingsItem :
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothConnectionHelper.REQUEST_ENABLE_BT && resultCode == RESULT_OK){
            Log.d(TAG, "BLUETOOTH ENABLED");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopConnection();
    }

    @Override
    public void prepareViews() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void showProgressBar() {
        connectionProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
        connectionProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWarningSnackbar(String warningMessage) {
        Snackbar snackbar = Snackbar.make(rootView, warningMessage, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void connectionProcessingFinished() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
