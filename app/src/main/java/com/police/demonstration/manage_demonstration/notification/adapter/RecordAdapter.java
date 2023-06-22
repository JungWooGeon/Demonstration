package com.police.demonstration.manage_demonstration.notification.adapter;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.databinding.RecyclerviewRecordListBinding;
import com.police.demonstration.database.measurement.MeasurementInfo;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private final ArrayList<MeasurementInfo> measurementList;
    private final ArrayList<Boolean> checkList;

    // string.xml 을 사용하기 위한 resources
    private Resources resources;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewRecordListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // data binding
            this.binding = RecyclerviewRecordListBinding.bind(itemView);
        }
    }

    public RecordAdapter(ArrayList<MeasurementInfo> dataSet) {
        measurementList = dataSet;
        checkList = new ArrayList<>();
        for (int i = 0; i < measurementList.size(); i++) {
            checkList.add(false);
        }
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_record_list, parent, false);
        RecordAdapter.ViewHolder viewHolder = new RecordAdapter.ViewHolder(view);

        resources = parent.getResources();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String numberText = position + 1 + resources.getString(R.string.dot);
        holder.binding.numberTextView.setText(numberText);

        holder.binding.checkbox.setChecked(checkList.get(position));
        holder.binding.checkbox.setOnClickListener(e -> checkList.set(position, !checkList.get(position)));

        String measurementTime = measurementList.get(position).getMeasurementTime();
        Log.d("테스트", measurementTime);
        String[] times = measurementTime.split(resources.getString(R.string.dash));
        Log.d("테스트", String.valueOf(times.length));
        String contentText = times[3] + resources.getString(R.string.hour) + resources.getString(R.string.space) + times[4] + resources.getString(R.string.minute)
               + resources.getString(R.string.space) + resources.getString(R.string.slash) + resources.getString(R.string.space) + measurementList.get(position).getPlace();
        Log.d("테스트", contentText);
        holder.binding.content.setText(contentText);
    }

    @Override
    public int getItemCount() {
        return measurementList.size();
    }
}
