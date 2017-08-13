package com.steveq.gardenpieclient.sections.presentation;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.base.BaseActivity;
import com.steveq.gardenpieclient.connection.ConnectionHelper;
import com.steveq.gardenpieclient.database.Repository;
import com.steveq.gardenpieclient.database.SectionsRepository;
import com.steveq.gardenpieclient.main_view.presentation.MainActivity;
import com.steveq.gardenpieclient.main_view.presentation.MainActivityPresenterImpl;
import com.steveq.gardenpieclient.communication.JsonProcessor;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.sections.adapters.SectionsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 2017-08-03.
 */

public class SectionsFragmentPresenterImpl implements SectionsFragmentPresenter {
    private static final String TAG = SectionsFragmentPresenterImpl.class.getSimpleName();
    private static final Integer MAX_TIMES = 6;
    private ConnectionHelper connectionHelper;
    private static SectionsFragmentView mainView;
    private static BaseActivity parentActivity;
    public static RecyclerView.Adapter sectionsAdapter;
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
    public void collectDays(Section section, DaysListener listener) {
        RowClickListener.lastClickedSection.setDays(listener.mChosenDays);
        repository.updateSectionDays(section, listener.mChosenDays);

        reloadDataInAdapter();
    }

    @Override
    public void collectTimes(Section section, TimesListener listener) {
        RowClickListener.lastClickedSection.setTimes(listener.mChosenTimes);
        repository.updateSectionTimes(section, listener.mChosenTimes);

        reloadDataInAdapter();
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
        String message = JsonProcessor.getInstance().createScanRequest();
        if(connectionHelper != null){
            connectionHelper.sendMessage(message);
        }
    }

    @Override
    public void uploadScannedSectionsData() {
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
        parentActivity.showProgressBar();
        List<Integer> scanned = ((SectionsRecyclerViewAdapter)sectionsAdapter).getScannedSectionsNums();
        List<Section> sectionsToUpload = new ArrayList<>();
        for(Integer i : scanned){
            sectionsToUpload.add(repository.getSectionById(i));
        }
        String message = JsonProcessor.getInstance().createUploadRequest(sectionsToUpload);
        connectionHelper.sendMessage(message);
    }

    @Override
    public void downloadScannedSectionsData() {
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
        parentActivity.showProgressBar();
        List<Integer> scanned = ((SectionsRecyclerViewAdapter)sectionsAdapter).getScannedSectionsNums();
        String message = JsonProcessor.getInstance().createDownloadRequest(scanned);
        connectionHelper.sendMessage(message);
    }

    @Override
    public void acknowledgeDownloadedData(List<Section> sections) {
        for(Section section : sections){
            if(section.getDays() != null){
                Section fromDB = repository.getSectionById(section.getNumber());
                if(fromDB.getNumber() == null){
                    repository.createSection(section);
                    repository.createSectionDays(section, section.getDays());
                    repository.createSectionTimes(section, section.getTimes());
                } else {
                    repository.updateSection(section);
                    repository.updateSectionDays(section, section.getDays());
                    repository.createSectionTimes(section, section.getTimes());
                }
            }
        }
    }

    @Override
    public void presentSections(List<Section> sections) {
        Log.d(TAG, "PRESENT SECTIONS : " + sections);

        for(Section s : sections){
            Section sec = repository.getSectionById(s.getNumber());
            if(sec.getNumber() == null){
                sec.setNumber(s.getNumber());
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
        List<Section> values = new ArrayList<Section>();
        for(Integer i : ((SectionsRecyclerViewAdapter)sectionsAdapter).getScannedSectionsNums()){
            values.add(repository.getSectionById(i));
        }
        Log.d(TAG, "SET SECTIONS TO ADAPTER : " + values);
        ((SectionsRecyclerViewAdapter)sectionsAdapter).setPayload(values);
        sectionsAdapter.notifyDataSetChanged();
        if(sectionsAdapter.getItemCount() > 0){
            mainView.showRecyclerView();
        }
    }

    public static class RowClickListener implements View.OnClickListener{
        private static Section lastClickedSection;
        private Section section;

        public RowClickListener(){}

        public Section getSection(){
            return section;
        }

        public void setSection(Section section){
            this.section = section;
        }

        @Override
        public void onClick(View v) {
            lastClickedSection = section;
            Log.d(TAG, "CLICKED ROW");
            switch(v.getId()){
                case R.id.plusImageView:
                    mainView.showSetTimeDialog(section);
                    break;
                case R.id.minusImageView:
                    mainView.showDeleteTimesDialog(section);
                    break;
                case R.id.daysImageView:
                    mainView.showDaysDialog(section);
                    break;
                case R.id.activeCompatSwitch:
                    section.setActive(!section.getActive());
                    Log.d(TAG, "UPDATE SECTION " + section);
                    repository.updateSection(section);
                    break;
                default:
                    break;
            }
            reloadDataInAdapter();
        }
    }

    //inner class bound to specific instance
    public static class DaysListener implements DialogInterface.OnMultiChoiceClickListener{
        private List<String> mChosenDays;
        private List<String> days = new ArrayList<String>(Arrays.asList((parentActivity.getResources().getStringArray(R.array.days))));
        public DaysListener(){
            mChosenDays = new ArrayList<>();
        }

        public List<String> getChosenDays() {
            return mChosenDays;
        }

        public void setChosenDays(List<String> mChosenDays) {
            this.mChosenDays = mChosenDays;
        }

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            Log.d(TAG, "CHOSEN DAY INDEX : " + which);
            if(isChecked){
                mChosenDays.add(days.get(which));
            } else if (mChosenDays.contains(days.get(which))){
                mChosenDays.remove(mChosenDays.indexOf(days.get(which)));
            }
            Log.d(TAG, "CURRENT CHOSEN DAYS : " + mChosenDays);
        }
    }

    //inner class bound to specific instance
    public static class TimesListener implements DialogInterface.OnMultiChoiceClickListener{
        private Section section;
        private List<String> mChosenTimes;
        private List<String> times;

        public TimesListener(Section section){
            mChosenTimes = new ArrayList<>();
            this.section = section;
            times = section.getTimes();
        }

        public List<String> getChosenTimes() {
            return mChosenTimes;
        }

        public void setChosenTimes(List<String> mChosenTimes) {
            this.mChosenTimes = mChosenTimes;
        }

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            Log.d(TAG, "CHOSEN DAY INDEX : " + which);
            if(isChecked){
                mChosenTimes.add(times.get(which));
            } else {
                Log.d(TAG, "WHICH : " + which);
                Log.d(TAG, "CHOSEN TIMES : " + mChosenTimes);
                mChosenTimes.remove(mChosenTimes.indexOf(times.get(which)));
            }
        }
    }

    public static class TimeListener implements TimePickerDialog.OnTimeSetListener{
        //alarm which was clicked when editing row or null object alarm when cerating new alarm
        private Section section;

        public TimeListener(Section a){
            section = a;
        }

        public Section getAlarm() {
            return section;
        }

        public void setAlarm(Section section) {
            this.section = section;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeFormat = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
            Section sectionCheck = repository.getSectionById(section.getNumber());
            List<String> currentTimes = sectionCheck.getTimes();
            if(!currentTimes.contains(timeFormat)){
                if(currentTimes.size() == MAX_TIMES){
                    parentActivity.showWarningSnackbar(parentActivity.getString(R.string.max_times_warning_str));
                } else {
                    currentTimes.add(timeFormat);
                    Collections.sort(currentTimes);
                    repository.updateSectionTimes(sectionCheck, currentTimes);
                }
            }
            reloadDataInAdapter();
        }

    }
}
