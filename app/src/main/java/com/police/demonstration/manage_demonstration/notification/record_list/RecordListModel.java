package com.police.demonstration.manage_demonstration.notification.record_list;

import android.content.Context;

import com.police.demonstration.database.measurement.MeasurementDataBase;
import com.police.demonstration.database.measurement.MeasurementInfo;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecordListModel {

    // 측정 기록 리스트
    private final ArrayList<MeasurementInfo> measurementList = new ArrayList<>();

    private DatabaseListener databaseListener;

    public ArrayList<MeasurementInfo> getMeasurementList() {
        return measurementList;
    }

    public void setOnDatabaseListener(DatabaseListener databaseListener) {
        this.databaseListener = databaseListener;
    }

    // room db 사용 - 측정 기록 리스트 읽기 (listener 를 사용해 callback event 적용)
    public void readMeasurement(Context context, int demonstrationId) {
        // DB 에서 read (Rxjava 비동기) 후 listener 를 통해 변경 알림 (onChanged())
        MeasurementDataBase.getInstance(context)
                .measurementDao()
                .getMeasurement(demonstrationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> {
                    measurementList.addAll(list);
                    databaseListener.onChanged();
                }).subscribe();
    }

    // DB 비동기 작업 완료를 알리는 listener 역할
    public interface DatabaseListener {
        void onChanged();
    }
}
