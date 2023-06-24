package com.police.demonstration.manage_demonstration.notification.create_notification;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;

/**
 * NotificationActivity 에서 사용하는 ViewModel
 * 1. 고지서 이미지 URI 를 MutableLiveData 로 관리
 */
public class NotificationDocumentViewModel extends ViewModel {
    // NotificationDocumentViewModel Model
    private final NotificationDocumentModel model;

    // 고지 이미지 Live Data
    private final MutableLiveData<Uri> notificationUri = new MutableLiveData<>();

    public NotificationDocumentViewModel() {
        model = new NotificationDocumentModel();

        // callback listener 등록
        // Rxjava(비동기) - Retrofit(Api 통신) 완료일 경우 LiveData 에 고지서 URI 반영
        model.setCallbackListener(() -> notificationUri.setValue(model.getNotificationUri()));
    }

    public MutableLiveData<Uri> getMeasurementList() {
        return notificationUri;
    }

    // 안내문 고지서 URI 가져오기 기능 -> model 에 고지서 URI 가져오기 함수 실행
    public void getNotificationUriOne(Context context, DemonstrationInfo demonstrationInfo) {
        model.getNotificationUriOne(context, demonstrationInfo);
    }

    // 최고소음위반(유지) 고지서 URI 가져오기 기능 -> model 에 고지서 URI 가져오기 함수 실행
    public void getNotificationUriTwo(Context context, DemonstrationInfo demonstrationInfo, MeasurementInfo measurementInfo) {
        model.getNotificationUriTwo(context, demonstrationInfo, measurementInfo);
    }

    // 등가소음위반(유지) 고지서 URI 가져오기 기능 -> model 에 고지서 URI 가져오기 함수 실행
    public void getNotificationThree(Context context, DemonstrationInfo demonstrationInfo, MeasurementInfo measurementInfo) {
        model.getNotificationThree(context, demonstrationInfo, measurementInfo);
    }
}
