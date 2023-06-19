package com.police.demonstration.main.adapter;

import static com.police.demonstration.Constants.SIMPLE_DATE_FORMAT;
import static com.police.demonstration.Constants.STATUS_ING;
import static com.police.demonstration.Constants.STATUS_POST;
import static com.police.demonstration.Constants.STATUS_PRE;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.main.database.DemonstrationInfo;
import com.police.demonstration.databinding.RecyclerviewDemonstrationBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 메인 화면 시위 리스트 RecyclerView Adapter
 */
public class DemonstrationAdapter extends RecyclerView.Adapter<DemonstrationAdapter.ViewHolder> {

    // Adapter 에서 MainActivity 로 통신할 수 있는 listener
    public interface AdapterListener {
        void onDetailButtonClick(View view, DemonstrationInfo demonstrationInfo);

        void inputBackNoiseButtonClick(View view, DemonstrationInfo demonstrationInfo);
    }

    private AdapterListener listener = null;

    // RecyclerView 를 표시하기 위한 ArrayList -> 시위 정보
    private final ArrayList<DemonstrationInfo> demonstrationList;

    // string.xml 을 사용하기 위한 resources
    private Resources resources;

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewDemonstrationBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // data binding
            this.binding = RecyclerviewDemonstrationBinding.bind(itemView);
        }
    }

    public DemonstrationAdapter(ArrayList<DemonstrationInfo> dataSet) {
        demonstrationList = dataSet;
    }

    @NonNull
    @Override
    public DemonstrationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_demonstration, parent, false);
        DemonstrationAdapter.ViewHolder viewHolder = new DemonstrationAdapter.ViewHolder(view);

        resources = parent.getResources();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 상태 표시 그림 반영 (진행 중 -> 빨강, 진행 예정 -> 초록, 종료 -> 회색)
        switch (overCurrentDate(demonstrationList.get(position).getStartDate(), demonstrationList.get(position).getEndDate())) {
            case STATUS_PRE:
                holder.binding.statusImage.setImageResource(R.drawable.shape_circle_green);
                break;
            case STATUS_ING:
                holder.binding.statusImage.setImageResource(R.drawable.shape_circle_red);
                break;
            case STATUS_POST:
                holder.binding.statusImage.setImageResource(R.drawable.shape_circle_gray);
                break;
        }

        // 시위의 간단한 데이터들을 정제하여 화면에 표시
        String[] startDate = demonstrationList.get(position).getStartDate().split(resources.getString(R.string.dash));
        String placeDetail = startDate[0] + resources.getString(R.string.dot) + startDate[1] + resources.getString(R.string.dot) + startDate[2] + resources.getString(R.string.space) + resources.getString(R.string.slash) + resources.getString(R.string.space) + demonstrationList.get(position).getGroupName() + resources.getString(R.string.space) + resources.getString(R.string.open_bracket) + resources.getString(R.string.space) + resources.getString(R.string.organization) + resources.getString(R.string.space) + resources.getString(R.string.close_bracket);
        String placeText = resources.getString(R.string.open_bracket) + resources.getString(R.string.space) + demonstrationList.get(position).getPlace() + resources.getString(R.string.space) + resources.getString(R.string.close_bracket);

        holder.binding.placeDetail.setText(placeDetail);
        holder.binding.place.setText(placeText);

        // '배경 소음도' 버튼 클릭 이벤트 -> 배경 소음도 추가 화면으로 전환
        holder.binding.demonstrationDetailButton.setOnClickListener(e -> listener.onDetailButtonClick(holder.itemView, demonstrationList.get(position)));
        // '>' 버튼 클릭 이벤트 -> 시위 정보 관리 화면으로 전환
        holder.binding.inputBackNoiseButton.setOnClickListener(e -> listener.inputBackNoiseButtonClick(holder.itemView, demonstrationList.get(position)));
    }

    @Override
    public int getItemCount() {
        return demonstrationList.size();
    }

    // 현재 시간과 비교하여 시위의 상태 return (진행 중, 진행 예정, 종료)
    private int overCurrentDate(String sD, String eD) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

        Date startDate;
        Date endDate;
        try {
            startDate = formatter.parse(sD);
            endDate = formatter.parse(eD);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Date currentDate = new Date(System.currentTimeMillis());

        if (currentDate.compareTo(startDate) < 0) {
            return STATUS_PRE;
        } else if (currentDate.compareTo(endDate) < 0) {
            return STATUS_ING;
        } else {
            return STATUS_POST;
        }
    }
}