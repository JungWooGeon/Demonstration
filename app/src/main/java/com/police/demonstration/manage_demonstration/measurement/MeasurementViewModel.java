package com.police.demonstration.manage_demonstration.measurement;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.police.demonstration.manage_demonstration.measurement.database.MeasurementInfo;

/**
 * MeasurementFragment 에서 사용하는 ViewModel
 */
public class MeasurementViewModel extends ViewModel {
    MeasurementModel model = new MeasurementModel();

    // 시위 추가 기능 -> model 에 시위 추가 함수 실행 후 LiveData 에 반영
    public void addDemonstration(Context context, MeasurementInfo measurementInfo) {
        model.addDemonstration(context, measurementInfo);
    }
}
