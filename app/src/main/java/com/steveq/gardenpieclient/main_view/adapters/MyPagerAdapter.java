package com.steveq.gardenpieclient.main_view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.dashboard.presentation.DashboardFragment;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragment;
import com.steveq.gardenpieclient.weather.presentation.WeatherFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adam on 2017-07-28.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private static int NUM_FRAGMENTS = 3;
    private int icons[] = {R.drawable.dashboard_vec, R.drawable.section_vec, R.drawable.weather_vec};
    public static List<Fragment> fragmentsPoll = new ArrayList<Fragment>(Arrays.asList(
            new DashboardFragment(),
            new SectionsFragment(),
            new WeatherFragment()
    ));



    public MyPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsPoll.get(position);
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }

    @Override
    public int getPageIconResId(int position) {
        return icons[position];
    }
}
