package com.police.demonstration.main;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.DemonstrationInfo;

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
        model.setOnDatabaseListener(() -> demonstrationList.setValue(model.getDemonstrationList()));
    }

    public MutableLiveData<ArrayList<DemonstrationInfo>> getDemonstrationList() {
        return this.demonstrationList;
    }

    // 시위 읽기 기능 동작 후 LiveData update
    public void readDemonstration(Context context) {
        model.readDemonstration(context);
    }

    // 시위 추가 기능 동작 후 LiveData update
    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        model.addDemonstration(context, demonstrationInfo);
        demonstrationList.setValue(model.getDemonstrationList());
    }

    // 배경 소음도 수정
    public void updateBackgroundNoise(Context context, DemonstrationInfo demonstrationInfo) {
        model.updateBackgroundNoise(context, demonstrationInfo);
        demonstrationList.setValue(model.getDemonstrationList());
    }
}
