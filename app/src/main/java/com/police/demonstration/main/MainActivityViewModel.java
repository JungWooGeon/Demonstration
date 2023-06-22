package com.police.demonstration.main;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.demonstration.DemonstrationInfo;

import java.util.ArrayList;

/**
 * MainActivity 에서 사용하는 ViewModel
 * 1. 시위 데이터를 MutableLiveData 로 관리
 */
public class MainActivityViewModel extends ViewModel {
    // MainActivityViewModel Model
    private final MainModel model;

    // 시위 리스트 Live Data
    private final MutableLiveData<ArrayList<DemonstrationInfo>> demonstrationList = new MutableLiveData<>();

    public MainActivityViewModel() {
        model = new MainModel();

        // model 에서 DB 에 관련된 비동기 작업이 완료 되었을 경우
        // LiveDate 에 해당 값을 반영함
        // 현재 readDemonstration() 에서만 listener 를 사용하고,
        // 다른 DB 에 관련된 작업들은 data 반영과 DB 비동기 작업을 따로 실행
        model.setOnDatabaseListener(() -> demonstrationList.setValue(model.getDemonstrationList()));
    }

    public MutableLiveData<ArrayList<DemonstrationInfo>> getDemonstrationList() {
        return this.demonstrationList;
    }

    // 시위 읽기 기능 -> model 에 시위 읽기 함수 실행
    public void readDemonstration(Context context) {
        model.readDemonstration(context);
    }

    // 시위 추가 기능 -> model 에 시위 추가 함수 실행 후 LiveData 에 반영
    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        model.addDemonstration(context, demonstrationInfo);
        demonstrationList.setValue(model.getDemonstrationList());
    }

    // 배경 소음도 수정 -> model 에 배경 소음도 update 함수 실행 후 LiveData 에 반영
    public void updateBackgroundNoise(Context context, DemonstrationInfo demonstrationInfo) {
        model.updateBackgroundNoise(context, demonstrationInfo);
        demonstrationList.setValue(model.getDemonstrationList());
    }
}
