package com.police.demonstration.main;

import static com.police.demonstration.Constants.SIMPLE_DATE_FORMAT;
import static com.police.demonstration.Constants.STATUS_ING;
import static com.police.demonstration.Constants.STATUS_POST;
import static com.police.demonstration.Constants.STATUS_PRE;

import android.annotation.SuppressLint;
import android.content.Context;

import com.police.demonstration.database.DemonstrationDataBase;
import com.police.demonstration.database.DemonstrationInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * MainActivityViewModel Model
 * room db 를 사용하여 시위 데이터 관리
 */
public class MainModel {

    // 시위 리스트
    private final ArrayList<DemonstrationInfo> demonstrationList = new ArrayList<>();
    private DatabaseListener databaseListener;

    public ArrayList<DemonstrationInfo> getDemonstrationList() {
        return this.demonstrationList;
    }

    public void setOnDatabaseListener(DatabaseListener databaseListener) {
        this.databaseListener = databaseListener;
    }

    // room db 사용하여 시위 정보 추가
    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        // DB 에 add (Rxjava 비동기)
        DemonstrationDataBase.getInstance(context)
                .demonstrationDao()
                .addDemonstration(demonstrationInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        // demonstrationList 에 add 반영
        demonstrationList.add(demonstrationInfo);
        sortDemonstrationList();
    }

    // room db 사용하여 시위 리스트 읽기
    public void readDemonstration(Context context) {
        // DB 에서 read (Rxjava 비동기) 후 listener 를 통해 변경 알림 (onChanged())
        DemonstrationDataBase.getInstance(context)
                .demonstrationDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> {
                    demonstrationList.addAll(list);
                    sortDemonstrationList();
                    databaseListener.onChanged();
                }).subscribe();
    }

    public void updateBackgroundNoise(Context context, DemonstrationInfo demonstrationInfo) {
        // DB 에 update (Rxjava 비동기)
        DemonstrationDataBase.getInstance(context)
                .demonstrationDao()
                .updateDemonstration(demonstrationInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        // demonstrationList 에 update 반영
        for (int i = 0; i < demonstrationList.size(); i++) {
            if (demonstrationList.get(i).getId() == demonstrationInfo.getId()) {
                demonstrationList.get(i).setBackgroundNoiseLevel(demonstrationInfo.getBackgroundNoiseLevel());
            }
        }
    }

    // 시위 리스트 순서에 맞게 정렬
    private void sortDemonstrationList() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        demonstrationList.sort((info1, info2) -> {
            Date startDate1;
            Date endDate1;
            Date startDate2;
            Date endDate2;

            try {
                startDate1 = formatter.parse(info1.getStartDate());
                endDate1 = formatter.parse(info1.getEndDate());
                startDate2 = formatter.parse(info2.getStartDate());
                endDate2 = formatter.parse(info2.getEndDate());

                int infoStatus1 = getCompareToCurrentDateStatus(startDate1, endDate1);
                int infoStatus2 = getCompareToCurrentDateStatus(startDate2, endDate2);

                if (infoStatus1 < infoStatus2) {
                    return -1;
                } else if (infoStatus1 > infoStatus2) {
                    return 1;
                } else {
                    // 같다면 start date 가 최근인 순서로 정렬
                    assert startDate1 != null;

                    if (infoStatus1 == STATUS_ING) {
                        // 진행 중인 것은 날짜가 큰 값을 위로
                        return -startDate1.compareTo(startDate2);
                    } else {
                        // 진행 중이 아닌 것은 날짜가 작은 값을 위로
                        return startDate1.compareTo(startDate2);
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 현재 날짜와 비교 후 진행 예정, 진행 중, 종료 상태 중 return
    private int getCompareToCurrentDateStatus(Date startDate, Date endDate) {
        Date currentDate = new Date(System.currentTimeMillis());
        if (currentDate.compareTo(startDate) < 0) {
            // 진행 예정
            return STATUS_PRE;
        } else if (currentDate.compareTo(endDate) < 0) {
            // 진행 중
            return STATUS_ING;
        } else {
            // 종료
            return STATUS_POST;
        }
    }

    // DB thread 완료를 전달하는 listener 역할
    public interface DatabaseListener {
        void onChanged();
    }
}
