package com.police.demonstration.manage_demonstration.measurement;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.measurement.MeasurementInfo;

/**
 * MeasurementFragment 에서 사용하는 ViewModel
 */
public class MeasurementViewModel extends ViewModel {
    MeasurementModel model = new MeasurementModel();

    // 측정 데이터 추가 기능 -> model 에 측정 추가 함수 실행 후 LiveData 에 반영
    public void addMeasurement(Context context, MeasurementInfo measurementInfo) {
        model.addMeasurement(context, measurementInfo);
    }
}
