package com.police.demonstration;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.police.demonstration.database.DemonstrationInfo;

import java.util.ArrayList;
import java.util.Map;

public class MainActivityViewModel extends ViewModel {
    private final MainModel model = new MainModel();

    private final MutableLiveData<ArrayList<DemonstrationInfo>> demonstrationList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<DemonstrationInfo>> getDemonstrationList() {
        return this.demonstrationList;
    }

    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        model.addDemonstration(context, demonstrationInfo);
        demonstrationList.setValue(model.getDemonstrationList());
    }

    public void readDemonstration(Context context) {
        model.readDemonstration(context);
        demonstrationList.setValue(model.getDemonstrationList());
    }
}
