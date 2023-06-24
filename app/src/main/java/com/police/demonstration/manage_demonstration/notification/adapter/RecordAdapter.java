package com.police.demonstration.manage_demonstration.notification.adapter;

import static com.police.demonstration.Constants.NOTIFICATION_MAINTENANCE;
import static com.police.demonstration.Constants.NOTIFICATION_NOT;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_NOT;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.databinding.RecyclerviewRecordListBinding;
import com.police.demonstration.database.measurement.MeasurementInfo;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private final ArrayList<MeasurementInfo> measurementList;
    private final ArrayList<Boolean> checkList;

    private final int notificationType;

    // string.xml 을 사용하기 위한 resources
    private Resources resources;

    public ArrayList<Boolean> getCheckList() { return this.checkList; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewRecordListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // data binding
            this.binding = RecyclerviewRecordListBinding.bind(itemView);
        }
    }

    public RecordAdapter(ArrayList<MeasurementInfo> dataSet, int notificationType) {
        measurementList = dataSet;
        checkList = new ArrayList<>();
        for (int i = 0; i < measurementList.size(); i++) {
            checkList.add(false);
        }

        this.notificationType = notificationType;
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
        // 번호 TextView 반영
        String numberText = position + 1 + resources.getString(R.string.dot);
        holder.binding.numberTextView.setText(numberText);

        // init checkbox
        holder.binding.checkbox.setChecked(checkList.get(position));
        holder.binding.checkbox.setOnClickListener(e -> checkList.set(position, !checkList.get(position)));

        // 시간, 장소 TextView 반영
        String[] times = measurementList.get(position).getStartTime().split(resources.getString(R.string.split_space));
        String contentText = times[0] + resources.getString(R.string.hour) + resources.getString(R.string.space) + times[2] + resources.getString(R.string.minute)
                + resources.getString(R.string.space) + resources.getString(R.string.slash) + resources.getString(R.string.space) + measurementList.get(position).getPlace();
        holder.binding.content.setText(contentText);

        String text = "";

        switch (measurementList.get(position).getNotificationType()) {
            case NOTIFICATION_TYPE_NOT:
                // 위반 사항 TextView 반영
                text = resources.getString(R.string.open_bracket) + resources.getString(R.string.space) + resources.getString(R.string.example_content_detail) + resources.getString(R.string.comma)  + resources.getString(R.string.space);
                break;
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE:
                text = resources.getString(R.string.open_bracket) + resources.getString(R.string.space) + resources.getString(R.string.exceed_highest_noise) + resources.getString(R.string.comma) + resources.getString(R.string.space);
                break;
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE:
                text = resources.getString(R.string.open_bracket) + resources.getString(R.string.space) + resources.getString(R.string.exceed_equivalent_noise) + resources.getString(R.string.comma) + resources.getString(R.string.space);
                break;
            default:
                break;
        }

        switch(measurementList.get(position).getNotificationState()) {
            case NOTIFICATION_NOT:
                text += resources.getString(R.string.notification_not);
                break;
            case NOTIFICATION_MAINTENANCE:
                text += resources.getString(R.string.notification_maintenance);
                break;
            default:
                break;
        }

        text += resources.getString(R.string.space) + resources.getString(R.string.close_bracket);
        holder.binding.contentDetail.setText(text);

        // 위반사항에 따른 레이아웃 비활성화
        if (notificationType != measurementList.get(position).getNotificationType()) {
            holder.binding.layout.setBackgroundColor(Color.parseColor("#FEFEFE"));
            holder.binding.numberTextView.setTextColor(Color.parseColor("#9A9A9A"));
            holder.binding.content.setTextColor(Color.parseColor("#9A9A9A"));
            holder.binding.contentDetail.setTextColor(Color.parseColor("#9A9A9A"));
            holder.binding.checkbox.setEnabled(false);
        } else {
            // 위반 사항 없을 경우 클릭 가능
            // layout 클릭 이벤트
            holder.binding.layout.setOnClickListener(e -> {
                checkList.set(position, !checkList.get(position));
                holder.binding.checkbox.setChecked(!holder.binding.checkbox.isChecked());
            });
        }
    }

    @Override
    public int getItemCount() {
        return measurementList.size();
    }
}
