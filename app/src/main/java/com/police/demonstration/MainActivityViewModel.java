package com.police.demonstration;

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
    private final MainModel model = new MainModel();

    // 시위 리스트 Live Data
    private final MutableLiveData<ArrayList<DemonstrationInfo>> demonstrationList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<DemonstrationInfo>> getDemonstrationList() {
        return this.demonstrationList;
    }

    // 시위 추가 기능 동작 후 LiveData update
    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        model.addDemonstration(context, demonstrationInfo);
        demonstrationList.setValue(model.getDemonstrationList());
    }

    // 시위 읽기 기능 동작 후 LiveData update
    public void readDemonstration(Context context) {
        model.readDemonstration(context);
        demonstrationList.setValue(model.getDemonstrationList());
    }
}
