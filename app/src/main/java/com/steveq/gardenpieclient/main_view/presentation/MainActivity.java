package com.steveq.gardenpieclient.main_view.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;
import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.communication.JsonProcessor;
import com.steveq.gardenpieclient.communication.models.BasicResponse;
import com.steveq.gardenpieclient.communication.models.FromServerResponse;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.communication.models.WeatherResponse;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;
import com.steveq.gardenpieclient.sections.adapters.SectionsRecyclerViewAdapter;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenterImpl;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentView;
import com.steveq.gardenpieclient.settings.presentation.activities.SettingsActivity;
import com.steveq.gardenpieclient.main_view.adapters.MyPagerAdapter;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragment;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenter;
import com.steveq.gardenpieclient.weather.presentation.WeatherFragmentView;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements MainView, android.os.Handler.Callback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mainToolbar;
    private LinearLayout rootView;
    private ProgressBar connectionProgressBar;
    private MainActivityPresenter presenter;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;
    private Snackbar warningSnackbar;
    public static Boolean receivedMsg = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseActivity.mainHandler = new Handler(Looper.getMainLooper(), this);

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        rootView = (LinearLayout) findViewById(R.id.rootLinearLayout);
        connectionProgressBar = (ProgressBar) findViewById(R.id.connectionProgressBar);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setViewPager(viewPager);
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
        connectionProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWarningSnackbar(String warningMessage) {
        warningSnackbar = Snackbar.make(rootView, warningMessage, BaseTransientBottomBar.LENGTH_LONG);
        warningSnackbar.show();
    }

    @Override
    public void showWarningSnackbarWithAction(String warningMessage, String actionString, View.OnClickListener listener) {
        warningSnackbar = Snackbar.make(rootView, warningMessage, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(actionString, listener);
        warningSnackbar.show();
    }


    @Override
    public void dismissWarningSnackbar(){
        if(warningSnackbar != null){
            warningSnackbar.dismiss();
        }
    }

    @Override
    public MainActivityPresenter getPresenter() {
        return this.presenter;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "HANDLE MESSAGE");
        if(msg.what == BluetoothConnectionHelper.BT_MSG){
            receivedMsg = true;
            Log.d(TAG, "Message : " + new String((byte[])msg.obj));
            FromServerResponse response = JsonProcessor.getInstance().deserializeServerResponse(new String((byte[])msg.obj));
            switch(JsonProcessor.Method.valueOf(response.getMethod())){
                case SCAN :
                    if(response.getSections().size() > 0) {
                        List<Integer> sectionNums = new ArrayList<>();
                        for(Section section : response.getSections()){
                            if(section.getNumber() == -1){
                                this.showWarningSnackbar("Scanning not possible now");
                                ((SectionsFragmentView)MyPagerAdapter.fragmentsPoll.get(1)).getPresenter().presentSections(((SectionsRecyclerViewAdapter) SectionsFragmentPresenterImpl.sectionsAdapter).getPayload());
                                this.hideProgressBar();
                                return true;
                            }
                            sectionNums.add(section.getNumber());
                        }

                        ((SectionsRecyclerViewAdapter)SectionsFragmentPresenterImpl.sectionsAdapter).setScannedSectionsNums(sectionNums);
                        ((SectionsFragmentView)MyPagerAdapter.fragmentsPoll.get(1)).getPresenter().presentSections(response.getSections());
                    }
                    break;
                case UPLOAD :
                    StringBuilder syncedSections = new StringBuilder();
                    for(Section section : response.getSections()){
                        syncedSections.append(section.getNumber());
                        syncedSections.append(",");
                    }
                    syncedSections.deleteCharAt(syncedSections.length()-1);
                    this.showWarningSnackbar("Synced sections : " + syncedSections.toString());
                    break;
                case DOWNLOAD :
                    ((SectionsFragmentView)MyPagerAdapter.fragmentsPoll.get(1)).getPresenter().acknowledgeDownloadedData(response.getSections());
                    //((SectionsRecyclerViewAdapter)SectionsFragmentPresenterImpl.sectionsAdapter).setPayload(response.getSections());
                    SectionsFragmentPresenterImpl.reloadDataInAdapter();
                    break;
                case WEATHER :
                    ((WeatherFragmentView)MyPagerAdapter.fragmentsPoll.get(2)).getPresenter().acknowledgeWeatherData(response.getWeather());
                    ((WeatherFragmentView)MyPagerAdapter.fragmentsPoll.get(2)).getPresenter().presentWeatherInfo();
                    break;
                default :
                    break;
            }
            this.hideProgressBar();
            return true;
        }
        return false;
    }
}
