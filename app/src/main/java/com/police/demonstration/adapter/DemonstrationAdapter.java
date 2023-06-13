package com.police.demonstration.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.database.DemonstrationInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 메인 화면 시위 리스트 RecyclerView Adapter
 */
public class DemonstrationAdapter extends RecyclerView.Adapter<DemonstrationAdapter.ViewHolder> {

    private final static int STATUS_PRE = 0;
    private final static int STATUS_ING = 1;
    private final static int STATUS_POST = 2;

    private final ArrayList<DemonstrationInfo> demonstrationList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatImageView statusImage;
        private final TextView place;
        private final TextView placeDetail;
        private final AppCompatImageView inputBackNoiseButton;
        private final AppCompatImageView demonstrationDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusImage = itemView.findViewById(R.id.statusImage);
            place = itemView.findViewById(R.id.place);
            placeDetail = itemView.findViewById(R.id.placeDetail);
            inputBackNoiseButton = itemView.findViewById(R.id.inputBackNoiseButton);
            demonstrationDetailButton = itemView.findViewById(R.id.demonstrationDetailButton);
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

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (overCurrentDate(demonstrationList.get(position).getStartDate(), demonstrationList.get(position).getEndDate())) {
            case STATUS_PRE:
                holder.statusImage.setImageResource(R.drawable.shape_circle_green);
                break;
            case STATUS_ING:
                holder.statusImage.setImageResource(R.drawable.shape_circle_red);
                break;
            case STATUS_POST:
                holder.statusImage.setImageResource(R.drawable.shape_circle_gray);
                break;
        }

        String[] startDate = demonstrationList.get(position).getStartDate().split("-");
        String placeDetail = startDate[0] + "." + startDate[1] + "." + startDate[2] + " / " + demonstrationList.get(position).getGroupName() + " [ 단체 ]";
        String placeText = "[ " + demonstrationList.get(position).getPlace() + " ]";

        holder.placeDetail.setText(placeDetail);
        holder.place.setText(placeText);
    }

    @Override
    public int getItemCount() {
        return demonstrationList.size();
    }

    private int overCurrentDate(String sD, String eD) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd-hh-mm");

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