package com.steveq.gardenpieclient.sections.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenterImpl;
import com.steveq.gardenpieclient.communication.models.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-05.
 */

public class SectionsRecyclerViewAdapter extends RecyclerView.Adapter<SectionsRecyclerViewAdapter.RCViewHolder>{
    private static final String TAG = SectionsRecyclerViewAdapter.class.getSimpleName();
    private static final String NO_REPEATING = "no repeating...";
    public List<Integer> scannedSectionsNums;
    private List<Section> payload;
    private Activity activity;

    public SectionsRecyclerViewAdapter(Activity activity, List<Section> payload){
        this.payload = payload;
        this.activity = activity;
    }

    public void setPayload(List<Section> payload) {
        this.payload = payload;
    }

    public List<Integer> getScannedSectionsNums() {
        return scannedSectionsNums;
    }

    public void setScannedSectionsNums(List<Integer> scannedSectionsNums) {
        this.scannedSectionsNums = scannedSectionsNums;
    }

    @Override
    public RCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_row, parent, false);
        return new RCViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RCViewHolder holder, int position) {
        Log.d(TAG, "BINDING VIEW HOLDER");
        holder.sectionNumberTextView.setText(String.valueOf(payload.get(position).getNumber()));
        SectionsFragmentPresenterImpl.RowClickListener listener = new SectionsFragmentPresenterImpl.RowClickListener();
        holder.plusImageView.setOnClickListener(listener);
        holder.minusImageView.setOnClickListener(listener);
        holder.alarmsTextView.setText(createTimesDescription(payload.get(position).getTimes()));
        holder.daysImageView.setOnClickListener(listener);
        holder.daysTextView.setText(createDaysDescription(payload.get(position).getDays()));
        holder.daysTextView.setOnClickListener(listener);
        holder.activeCompatSwitch.setChecked(payload.get(position).getActive());
        holder.activeCompatSwitch.setOnClickListener(listener);

        listener.setSection(payload.get(position));
    }

    @Override
    public int getItemCount() {
        return payload.size();
    }

    public static class RCViewHolder extends RecyclerView.ViewHolder {
        TextView sectionNumberTextView;
        SwitchCompat activeCompatSwitch;
        ImageView plusImageView;
        ImageView minusImageView;
        TextView alarmsTextView;
        ImageView daysImageView;
        TextView daysTextView;

        public RCViewHolder(View itemView) {
            super(itemView);

            sectionNumberTextView = (TextView) itemView.findViewById(R.id.sectionNumberTextView);
            activeCompatSwitch = (SwitchCompat) itemView.findViewById(R.id.activeCompatSwitch);
            plusImageView = (ImageView) itemView.findViewById(R.id.plusImageView);
            minusImageView = (ImageView) itemView.findViewById(R.id.minusImageView);
            alarmsTextView = (TextView) itemView.findViewById(R.id.alarmsTextView);
            daysImageView = (ImageView) itemView.findViewById(R.id.daysImageView);
            daysTextView = (TextView) itemView.findViewById(R.id.repeatDaysTextView);
        }
    }

    private String createTimesDescription(List<String> times){
        if(times.size() > 0){
            StringBuilder builder = new StringBuilder();
            for(String t : times){
                builder.append(t);
                builder.append(" |");
            }
            builder.deleteCharAt(builder.length()-1);
            return builder.toString();
        } else {
            return activity.getResources().getString(R.string.empty_time_str);
        }
    }

    private String createDaysDescription(List<String> days){
        List<String> alignedDays = alignDays(days);
        if(alignedDays.isEmpty()){
            return NO_REPEATING;
        } else {
            List<String> result = new ArrayList<>();
            Log.d(TAG, "DAYS TO PROCESS : " + alignedDays);
            for(String day : alignedDays){
                result.add(day.substring(0, 3).toLowerCase() + ".");
            }
            return joinList(result);
        }
    }

    private List<String> alignDays(List<String> rawDays){
        List<String> alignedDays = new ArrayList<>();
        String[] configuredDays = activity.getResources().getStringArray(R.array.days);
        for(String configuredDay : configuredDays){
            if(rawDays.contains(configuredDay)){
                alignedDays.add(configuredDay);
            }
        }
        return alignedDays;
    }

    private String joinList(List<String> list){
        StringBuilder builder = new StringBuilder();
        for(String el : list){
            builder.append(el);
            if(!(list.lastIndexOf(el) == list.size()-1)){
                builder.append(",");
            }
        }
        return builder.toString();
    }
}
