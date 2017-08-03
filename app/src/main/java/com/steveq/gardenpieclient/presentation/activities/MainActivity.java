package com.steveq.gardenpieclient.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.astuetz.PagerSlidingTabStrip;
import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;
import com.steveq.gardenpieclient.presentation.Presenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainActivityPresenter;
import com.steveq.gardenpieclient.presentation.activities.interfaces.MainView;
import com.steveq.gardenpieclient.presentation.activities.presenters.MainActivityPresenterImpl;
import com.steveq.gardenpieclient.presentation.adapters.MyPagerAdapter;
import com.steveq.gardenpieclient.presentation.fragments.SectionsFragment;
import com.steveq.gardenpieclient.presentation.fragments.interfaces.SectionsFragmentPresenter;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mainToolbar;
    private LinearLayout rootView;
    private ProgressBar connectionProgressBar;
    private MainActivityPresenter presenter;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;
    private Snackbar warningSnackbar;
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch(position){
                case 0:
                    break;
                case 1:
                    ((SectionsFragmentPresenter)((SectionsFragment)MyPagerAdapter.fragmentsPoll.get(position)).getPresenter()).scanForSections();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        rootView = (LinearLayout) findViewById(R.id.rootLinearLayout);
        connectionProgressBar = (ProgressBar) findViewById(R.id.connectionProgressBar);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
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
        warningSnackbar = Snackbar.make(rootView, warningMessage, BaseTransientBottomBar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.go_online_str), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            presenter.establishConnection();
                                        }
                                    });
        warningSnackbar.show();
    }



    @Override
    public void dismissWarningSnackbar(){
        if(warningSnackbar != null){
            warningSnackbar.dismiss();
        }
    }

    @Override
    public Presenter getPresenter() {
        return this.presenter;
    }
}
