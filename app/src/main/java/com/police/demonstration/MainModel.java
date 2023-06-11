package com.police.demonstration;

import android.content.Context;

import com.police.demonstration.database.DemonstrationDataBase;
import com.police.demonstration.database.DemonstrationInfo;

import java.util.ArrayList;

public class MainModel {

    private final ArrayList<DemonstrationInfo> demonstrationList = new ArrayList<>();

    public ArrayList<DemonstrationInfo> getDemonstrationList() {
        return this.demonstrationList;
    }

    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        DemonstrationDataBase db = DemonstrationDataBase.getInstance(context);
        db.demonstrationDao().addDemonstration(demonstrationInfo);
        demonstrationList.add(demonstrationInfo);
    }

    public void readDemonstration(Context context) {
        DemonstrationDataBase db = DemonstrationDataBase.getInstance(context);
        demonstrationList.addAll(db.demonstrationDao().getAll());
    }
}
