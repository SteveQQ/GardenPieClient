package com.steveq.gardenpieclient.sections.presentation;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.communication.body_builder.JsonProcessor;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;

import java.util.List;
import java.util.logging.Handler;

/**
 * A simple {@link Fragment} subclass.
 */
public class SectionsFragment extends Fragment implements SectionsFragmentView, android.os.Handler.Callback{
    private static final String TAG = SectionsFragment.class.getSimpleName();
    private static final Integer MAX_TIMES = 6;
    private SectionsFragmentPresenter presenter;
    private RecyclerView sectionsRecycler;
    private FloatingActionButton uploadFab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyTextView;
    public static Boolean receivedMsg = false;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            presenter.scanForSections();
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    private View.OnClickListener fabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "UPLOAD DATA TO SERVER");
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
        uploadFab = (FloatingActionButton) viewGroup.findViewById(R.id.updateAlarmsFab);
        emptyTextView = (TextView) viewGroup.findViewById(R.id.emptyRecyclerViewReplacement);
        presenter.initView();
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
        if(isVisibleToUser){
            presenter.scanForSections();
        }
    }

    @Override
    public void showTimesDialog(Section section) {
        List<String> timesToCheck = section.getTimes();
        boolean[] checkedItems = new boolean[MAX_TIMES];

        for(int i=0; i < MAX_TIMES; i++){
            if(timesToCheck.get(i) != null){
                checkedItems[i] = true;
            } else {
                timesToCheck.add(getString(R.string.empty_time_str));
                checkedItems[i] = false;
            }
        }

        while(timesToCheck.size() < MAX_TIMES){
            timesToCheck.add(getString(R.string.empty_time_str));
        }

        final SectionsFragmentPresenterImpl.DaysListener listener = new SectionsFragmentPresenterImpl.DaysListener();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.set_time_str))
                .setMultiChoiceItems(getResources().getStringArray(R.array.days), checkedItems, listener)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "COLLECT SELECTED TIMES");
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
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "HANDLE MESSAGE");
        if(msg.what == BluetoothConnectionHelper.BT_MSG){
            receivedMsg = true;
            Log.d(TAG, "Message : " + new String((byte[])msg.obj));
            List<Integer> sectionsNums = JsonProcessor.getInstance().deserializeSectionsInformation(new String((byte[])msg.obj));
            if(sectionsNums.size() > 0) {
                presenter.presentSections(sectionsNums);
            }
            ((BaseActivity)getActivity()).hideProgressBar();
            return true;
        }
        return false;
    }

    @Override
    public void showDaysDialog() {

    }

    @Override
    public void showSectionsRecyclerView() {
    }

    @Override
    public SectionsFragmentPresenter getPresenter() {
        return presenter;
    }
}
