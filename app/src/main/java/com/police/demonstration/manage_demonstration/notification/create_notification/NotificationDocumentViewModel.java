package com.police.demonstration.manage_demonstration.notification.create_notification;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;

public class NotificationDocumentViewModel extends ViewModel {
    // NotificationDocumentViewModel Model
    private final NotificationDocumentModel model;

    // 고지 이미지 Live Data
    private final MutableLiveData<Uri> notificationUri = new MutableLiveData<>();

    public NotificationDocumentViewModel() {
        model = new NotificationDocumentModel();
        model.setCallbackListener(() -> notificationUri.setValue(model.getNotificationUri()));
    }

    public MutableLiveData<Uri> getMeasurementList() { return notificationUri; }

    public void getNotificationUri(Context context, DemonstrationInfo demonstrationInfo, MeasurementInfo measurementInfo) {
        model.getNotificationUri(context, demonstrationInfo, measurementInfo);
    }
}
