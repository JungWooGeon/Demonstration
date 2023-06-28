package com.police.demonstration.manage_demonstration.record;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstration.R;
import com.police.demonstration.database.notification.NotificationInfo;
import com.police.demonstration.databinding.RecyclerviewNotifyBinding;
import com.police.demonstration.manage_demonstration.notification.create_notification.NotificationDocumentV2;


import java.util.ArrayList;

public class NotifyAdaptor extends RecyclerView.Adapter<NotifyAdaptor.ViewHolder> {


    private final ArrayList<NotificationInfo> notificationInfo;
    private Resources resources;


    public NotifyAdaptor(ArrayList<NotificationInfo> notificationInfo) {
        this.notificationInfo = notificationInfo;

        Log.e("이","dakdakdad");
    }

    @NonNull
    @Override
    public NotifyAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_notify, parent, false);
        NotifyAdaptor.ViewHolder viewHolder = new NotifyAdaptor.ViewHolder(view);

        resources = parent.getResources();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdaptor.ViewHolder holder, int position) {
        String numberText = position + 1 + resources.getString(R.string.dot);
        holder.binding.numberTextView.setText(numberText);

        // 시간 TextView 반영

        String text = "[";
        text += notificationInfo.get(position).getNotificationTime()+"] / [";
        text += notificationInfo.get(position).getNotificationTypeName()+"]";
        holder.binding.content4.setText(text);

        Uri MI = notificationInfo.get(position).getImageUri();
        int pos = position+1;

        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context  = v.getContext();
                Intent intent = new Intent(context, NotificationDocumentV2.class);
                intent.putExtra("Image_file_url", MI.toString());
                intent.putExtra("number",pos);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewNotifyBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = RecyclerviewNotifyBinding.bind(itemView);
        }




    }






}
