package com.steveq.gardenpieclient.sections.presentation;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
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

import com.github.clans.fab.FloatingActionMenu;
import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.communication.JsonProcessor;
import com.steveq.gardenpieclient.communication.models.FromServerResponse;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;
import com.steveq.gardenpieclient.sections.adapters.SectionsRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SectionsFragment extends Fragment implements SectionsFragmentView{
    private static final String TAG = SectionsFragment.class.getSimpleName();
    private static final Integer MAX_TIMES = 6;
    private SectionsFragmentPresenter presenter;
    private RecyclerView sectionsRecycler;
    private boolean isViewShown = false;

    private FloatingActionMenu sectionsMenuFab;
    private com.github.clans.fab.FloatingActionButton uploadFab;
    private com.github.clans.fab.FloatingActionButton downloadFab;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyTextView;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(isVisible()){
                presenter.scanForSections();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };
    private View.OnClickListener uploadFabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "UPLOAD DATA TO SERVER");
            presenter.uploadScannedSectionsData();
        }
    };

    private View.OnClickListener downloadFabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "DOWNLOAD DATA FROM SERVER");
            presenter.downloadScannedSectionsData();
        }
    };

    public SectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ON CREATE FRAGMENT");
        presenter = new SectionsFragmentPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "ON CREATE VIEW FRAGMENT");
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sections, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) viewGroup.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.material_amber_A700, null));
        sectionsRecycler = (RecyclerView) viewGroup.findViewById(R.id.sectionsRecyclerView);

        sectionsMenuFab = (FloatingActionMenu) viewGroup.findViewById(R.id.sectionsFloatingMenu);
        uploadFab = (com.github.clans.fab.FloatingActionButton) viewGroup.findViewById(R.id.sectionsUploadFloatingButton);
        uploadFab.setOnClickListener(uploadFabClick);
        downloadFab = (com.github.clans.fab.FloatingActionButton) viewGroup.findViewById(R.id.sectionsDownloadFloatingButton);
        downloadFab.setOnClickListener(downloadFabClick);

        emptyTextView = (TextView) viewGroup.findViewById(R.id.emptyRecyclerViewReplacement);
        presenter.initView();

        if(isViewShown){
            presenter.scanForSections();
        }

        return viewGroup;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "ON PAUSE FRAGMENT");
    }

    @Override
    public void configRecyclerView(RecyclerView.Adapter adapter) {
        sectionsRecycler.setHasFixedSize(true);
        sectionsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        sectionsRecycler.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "IS VISIBLE" + isVisibleToUser);
        Log.d(TAG, "VIEW" + getView());
//        if(getView() != null){
//            isViewShown = true;
//            presenter.scanForSections();
//        }
        if(isVisibleToUser){
            isViewShown = true;
            if(getView() != null) {
                presenter.scanForSections();
            }
        } else {
            isViewShown = false;
        }
    }

    @Override
    public void showSetTimeDialog(Section section) {
        SimpleDateFormat sdfHours = new SimpleDateFormat("HH");
        SimpleDateFormat sdfMinutes = new SimpleDateFormat("mm");
        TimePickerDialog.OnTimeSetListener listener = new SectionsFragmentPresenterImpl.TimeListener(section);
        TimePickerDialog tmd = new TimePickerDialog(getActivity(), listener, Integer.valueOf(sdfHours.format(new Date())) + 1, Integer.valueOf(sdfMinutes.format(new Date())), true);
        tmd.getWindow().setBackgroundDrawableResource(R.color.material_blue_grey_200);
        tmd.show();
    }

    @Override
    public void showDeleteTimesDialog(final Section section) {
        List<String> timesToCheck = section.getTimes();
        boolean[] checkedItems = new boolean[section.getTimes().size()];

        int counter = 0;
        for(String time : timesToCheck){
            checkedItems[counter] = true;
            counter++;
        }

        final SectionsFragmentPresenterImpl.TimesListener listener = new SectionsFragmentPresenterImpl.TimesListener(section);
        listener.setChosenTimes(timesToCheck);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.set_time_str))
                .setMultiChoiceItems(timesToCheck.toArray(new String[]{}), checkedItems, listener)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.collectTimes(section, listener);
                    }
                })
                .setNegativeButton("CANCEL", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showRecyclerView() {
        emptyTextView.setVisibility(View.INVISIBLE);
        sectionsRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        emptyTextView.setVisibility(View.VISIBLE);
        sectionsRecycler.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDaysDialog(final Section section) {
        List<String> daysToCheck = section.getDays();
        String[] configuredDays = getResources().getStringArray(R.array.days);
        boolean[] checkedItems = new boolean[configuredDays.length];
        int i = 0;
        for(String day : configuredDays){
            if(daysToCheck.contains(day)){
                checkedItems[i] = true;
            } else {
                checkedItems[i] = false;
            }
            i++;

        }
        final SectionsFragmentPresenterImpl.DaysListener listener = new SectionsFragmentPresenterImpl.DaysListener();
        Log.d(TAG, "DAYS TO CHECK : " + daysToCheck);
        listener.setChosenDays(daysToCheck);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.days_choose))
                .setMultiChoiceItems(getResources().getStringArray(R.array.days), checkedItems, listener)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.collectDays(section, listener);
                    }
                })
                .setNegativeButton("CANCEL", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showSectionsRecyclerView() {
    }

    @Override
    public SectionsFragmentPresenter getPresenter() {
        return presenter;
    }
}
