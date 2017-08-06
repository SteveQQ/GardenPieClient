package com.steveq.gardenpieclient.sections.presentation;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.connection.bluetooth.BluetoothConnectionHelper;
import com.steveq.gardenpieclient.database.Repository;
import com.steveq.gardenpieclient.database.SectionsRepository;
import com.steveq.gardenpieclient.main_view.presentation.MainActivityPresenterImpl;
import com.steveq.gardenpieclient.communication.body_builder.JsonProcessor;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.sections.adapters.SectionsRecyclerViewAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Adam on 2017-08-03.
 */

public class SectionsFragmentPresenterImpl implements SectionsFragmentPresenter {
    private static final String TAG = SectionsFragmentPresenterImpl.class.getSimpleName();
    private ConnectionHelper connectionHelper;
    private static SectionsFragmentView mainView;
    private static BaseActivity parentActivity;
    private static RecyclerView.Adapter sectionsAdapter;
    private static Repository repository;

    public SectionsFragmentPresenterImpl(SectionsFragmentView mainView){
        this.mainView = mainView;
        this.parentActivity = (BaseActivity)((Fragment)this.mainView).getActivity();
        repository = new SectionsRepository(parentActivity);
    }

    @Override
    public void initView() {
        sectionsAdapter = new SectionsRecyclerViewAdapter(parentActivity, new ArrayList<Section>());
        mainView.configRecyclerView(sectionsAdapter);
        if(sectionsAdapter.getItemCount() > 0){
            mainView.showRecyclerView();
        } else if(sectionsAdapter.getItemCount() == 0){
            mainView.hideRecyclerView();
        }
    }

    @Override
    public void collectTimes() {

    }

    @Override
    public void scanForSections() {
        HandlerThread abortingThread = new HandlerThread("abortConnection");
        abortingThread.start();
        Handler handler = new Handler(abortingThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "ABORT RECEIVING MSG???");
                if(!SectionsFragment.receivedMsg){
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
        String message = JsonProcessor.getInstance().createScanRequest();
        connectionHelper.sendMessage(message);
    }

    @Override
    public void presentSections(List<Integer> sectionsNums) {
        Set<Integer> allSectionsIds = repository.getSections().keySet();
        for(Integer i : allSectionsIds){
            if(!sectionsNums.contains(i)){
                repository.deleteSection(i);
            }
        }
        for(Integer s : sectionsNums){
            Section sec = repository.getSectionById(s);
            if(sec.getNumber() == null){
                sec.setNumber(s);
                sec.setActive(false);
                sec.setTimes(new ArrayList<String>());
                sec.setDays(new ArrayList<String>());
                Log.d(TAG, "CREATE SECTION : " + sec);
                boolean created = repository.createSection(sec);
                Log.d(TAG, "SECTION CREATED IN DB : " + created);
            }

        }
        reloadDataInAdapter();
    }

    public static void reloadDataInAdapter(){
        Map<Integer, Section> sectionsMap = repository.getSections();
        List<Section> values = new ArrayList<Section>();
        for(Integer i : sectionsMap.keySet()){
            values.add(sectionsMap.get(i));
        }
        Log.d(TAG, "SET SECTIONS TO ADAPTER : " + values);
        ((SectionsRecyclerViewAdapter)sectionsAdapter).setPayload(values);
        sectionsAdapter.notifyDataSetChanged();
        if(sectionsAdapter.getItemCount() > 0){
            mainView.showRecyclerView();
        }
    }

    public static class RowClickListener implements View.OnClickListener{

        public RowClickListener(){}

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.alarmsImageView:
                     break;
                case R.id.daysImageView:
                    break;
                case R.id.activeCompatSwitch:
                    break;
                default:
                    break;
            }
            reloadDataInAdapter();
        }
    }

    //inner class bound to specific instance
    public static class DaysListener implements DialogInterface.OnMultiChoiceClickListener{
        public DaysListener(){

        }

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            Log.d(TAG, "CHOSEN DAY INDEX : " + which);
            if(isChecked){SimpleDateFormat sdfHours = new SimpleDateFormat("HH");
                SimpleDateFormat sdfMinutes = new SimpleDateFormat("mm");
                TimePickerDialog.OnTimeSetListener listener = new SectionsFragmentPresenterImpl.TimeListener();
                TimePickerDialog tmd = new TimePickerDialog(((Fragment)mainView).getActivity(), listener, Integer.valueOf(sdfHours.format(new Date())) + 1, Integer.valueOf(sdfMinutes.format(new Date())), true);
                tmd.getWindow().setBackgroundDrawableResource(R.color.material_blue_grey_200);
                tmd.show();
            } else {
                Log.d(TAG, "TIME UNCHECKED");
            }
        }
    }

    public static class TimeListener implements TimePickerDialog.OnTimeSetListener{
        //alarm which was clicked when editing row or null object alarm when cerating new alarm

        public TimeListener(){
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeFormat = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
            Log.d(TAG, "TIME SET : " + timeFormat);
        }

    }
}
