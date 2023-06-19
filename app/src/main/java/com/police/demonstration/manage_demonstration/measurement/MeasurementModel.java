package com.police.demonstration.manage_demonstration.measurement;

import android.content.Context;

import com.police.demonstration.manage_demonstration.measurement.database.MeasurementDataBase;
import com.police.demonstration.manage_demonstration.measurement.database.MeasurementInfo;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * MeasurementViewModel 에서 사용하는 모델
 * room db 를 사용하여 측정 데이터 추가
 */
public class MeasurementModel {

    // room db 사용 - 측정 정보 추가
    public void addDemonstration(Context context, MeasurementInfo measurementInfo) {
        // DB 에 add (Rxjava 비동기)
        MeasurementDataBase.getInstance(context)
                .measurementDao()
                .addMeasurement(measurementInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
