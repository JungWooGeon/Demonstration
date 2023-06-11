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

public class DemonstrationAdapter extends RecyclerView.Adapter<DemonstrationAdapter.ViewHolder>{

    private ArrayList<DemonstrationInfo> demonstrationList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView statusImage;
        private TextView place;
        private TextView placeDetail;
        private AppCompatImageView inputBackNoiseButton;
        private AppCompatImageView demonstrationDetailButton;

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