package com.police.demonstration.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.database.DemonstrationInfo;

import java.util.ArrayList;

/**
 * 메인 화면 시위 리스트 RecyclerView Adapter
 */
public class DemonstrationAdapter extends RecyclerView.Adapter<DemonstrationAdapter.ViewHolder>{

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

    public DemonstrationAdapter (ArrayList<DemonstrationInfo> dataSet) {
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
        holder.statusImage.setImageResource(R.drawable.shape_circle_red);
        holder.place.setText(demonstrationList.get(position).getPlace());
        holder.placeDetail.setText(demonstrationList.get(position).getStartDate());
    }

    @Override
    public int getItemCount() {
        return demonstrationList.size();
    }
}