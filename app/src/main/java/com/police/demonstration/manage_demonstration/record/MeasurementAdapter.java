package com.police.demonstration.manage_demonstration.record;

import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;
import static com.police.demonstration.Constants.NOTIFICATION_MAINTENANCE;
import static com.police.demonstration.Constants.NOTIFICATION_NOT;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_NOT;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.databinding.RecyclerviewRecordListV2Binding;

import java.util.ArrayList;

/**
 * 기록 리스트 RecyclerView Adapter
 */
public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.ViewHolder> {

    // RecyclerView 를 표시하기 위한 ArrayList -> 측정 기록 정보
    private final ArrayList<MeasurementInfo> measurementList;
    private final DemonstrationInfo demonstrationInfo;


    // 화면에서 설정된 고지 타입 (해당 기록 선택 비활성화 여부를 정하기 위한 변수)


    // string.xml 을 사용하기 위한 resources
    private Resources resources;

    public MeasurementAdapter(ArrayList<MeasurementInfo> dataSet, DemonstrationInfo demonstrationInfo) {
        measurementList = dataSet;
        this.demonstrationInfo = demonstrationInfo;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewRecordListV2Binding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = RecyclerviewRecordListV2Binding.bind(itemView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_record_list_v2, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        resources = parent.getResources();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 번호 TextView 반영
        String numberText = position + 1 + resources.getString(R.string.dot);
        holder.binding.numberTextView.setText(numberText);

        // 시간, 장소 TextView 반영
        String[] times = measurementList.get(position).getStartTime().split(resources.getString(R.string.split_space));
        String contentText = times[0] + resources.getString(R.string.hour) + resources.getString(R.string.space) + times[2] + resources.getString(R.string.minute)
                + resources.getString(R.string.space) + resources.getString(R.string.slash) + resources.getString(R.string.space) + measurementList.get(position).getPlace();
        holder.binding.content.setText(contentText);

        // 이 기록의 고지 타입과 유지 상태를 표시
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
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_EQUIVALENT_NOISE:
                text = "[ 등가, 최고 소음 초과, ";
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

        MeasurementInfo measurementInfo = measurementList.get(position);

        holder.binding.measurementItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, record_check.class);
                intent.putExtra(INTENT_NAME_PARCELABLE_MEASUREMENT, measurementInfo);
                intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return measurementList.size();
    }
}
