package com.steveq.gardenpieclient.dashboard.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.steveq.gardenpieclient.R;
import com.steveq.gardenpieclient.communication.models.Section;
import com.steveq.gardenpieclient.communication.models.Sensor;
import com.steveq.gardenpieclient.sections.adapters.SectionsRecyclerViewAdapter;
import com.steveq.gardenpieclient.sections.presentation.SectionsFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017-08-19.
 */

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.RCViewHolder>{
    private static final String TAG = DashboardRecyclerViewAdapter.class.getSimpleName();
    private List<Sensor> payload;
    private Activity activity;

    public DashboardRecyclerViewAdapter(Activity activity, List<Sensor> payload){
        this.payload = payload;
        this.activity = activity;
    }

    public List<Sensor> getPayload() {
        return payload;
    }

    public void setPayload(List<Sensor> payload) {
        this.payload = payload;
    }

    @Override
    public DashboardRecyclerViewAdapter.RCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_row, parent, false);
        return new DashboardRecyclerViewAdapter.RCViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DashboardRecyclerViewAdapter.RCViewHolder holder, int position) {
        Log.d(TAG, "BINDING VIEW HOLDER");
        holder.sensorNameTextView.setText(payload.get(position).getName());
        holder.sensorValueTextView.setText(payload.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return payload.size();
    }

    public static class RCViewHolder extends RecyclerView.ViewHolder {
        TextView sensorNameTextView;
        TextView sensorValueTextView;

        public RCViewHolder(View itemView) {
            super(itemView);

            sensorNameTextView = (TextView) itemView.findViewById(R.id.sensorNameTextView);
            sensorValueTextView = (TextView) itemView.findViewById(R.id.sensorValueTextView);
        }
    }

}
