package com.police.demonstration.main;

import android.content.Context;

import com.police.demonstration.database.DemonstrationDataBase;
import com.police.demonstration.database.DemonstrationInfo;

import java.util.ArrayList;

/**
 * MainActivityViewModel Model
 * room db 를 사용하여 시위 데이터 관리
 */
public class MainModel {

    // 시위 리스트
    private final ArrayList<DemonstrationInfo> demonstrationList = new ArrayList<>();

    public ArrayList<DemonstrationInfo> getDemonstrationList() {
        return this.demonstrationList;
    }

    // room db 사용하여 시위 정보 추가
    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        DemonstrationDataBase db = DemonstrationDataBase.getInstance(context);
        db.demonstrationDao().addDemonstration(demonstrationInfo);
        demonstrationList.add(demonstrationInfo);
    }

    // room db 사용하여 시위 리스트 읽기
    public void readDemonstration(Context context) {
        DemonstrationDataBase db = DemonstrationDataBase.getInstance(context);
        demonstrationList.addAll(db.demonstrationDao().getAll());
    }
}
