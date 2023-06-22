package com.police.demonstration.manage_demonstration.notification.record_list;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.measurement.MeasurementInfo;

import java.util.ArrayList;

public class RecordListViewModel extends ViewModel {

    // RecordListViewModel Model
    private final RecordListModel model;

    // 측정 기록 리스트 Live Data
    private final MutableLiveData<ArrayList<MeasurementInfo>> measurementList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<MeasurementInfo>> getMeasurementList() { return measurementList; }

    public RecordListViewModel() {
        // model 에서 DB 에 관련된 비동기 작업이 완료 되었을 경우
        // LiveDate 에 해당 값을 반영함

        model = new RecordListModel();
        model.setOnDatabaseListener(() -> measurementList.setValue(model.getMeasurementList()));
    }

    // 측정 기록 읽기 기능 -> model 에 측정 기록 읽기 함수 실행
    public void readRecordList(Context context, int demonstrationId) {
        model.readMeasurement(context, demonstrationId);
    }
}
