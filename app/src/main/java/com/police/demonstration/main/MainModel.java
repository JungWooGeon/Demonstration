package com.police.demonstration.main;

import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_ETC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_HOME;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_DAY_HOME;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_DAY_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_ETC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_HOME;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.SIMPLE_DATE_FORMAT;
import static com.police.demonstration.Constants.STATUS_ING;
import static com.police.demonstration.Constants.STATUS_POST;
import static com.police.demonstration.Constants.STATUS_PRE;
import static com.police.demonstration.Constants.TIME_ZONE_DAY;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;

import android.annotation.SuppressLint;
import android.content.Context;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationDataBase;
import com.police.demonstration.database.demonstration.DemonstrationInfo;

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

    // room db 사용 - 시위 정보 추가
    public void addDemonstration(Context context, DemonstrationInfo demonstrationInfo) {
        // 등가 소음, 최고 소음 설정 (시간대와 장소대를 기준으로 설정)
        int timeZone = demonstrationInfo.getTimeZone();
        int placeZone = demonstrationInfo.getPlaceZone();

        String equivalentNoise = "";
        String highestNoise = "";

        if (timeZone == TIME_ZONE_DAY) {
            if (placeZone == PLACE_ZONE_HOME) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_DAY_HOME);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_DAY_HOME);
            } else if (placeZone == PLACE_ZONE_PUBLIC) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_DAY_PUBLIC);
            } else if (placeZone == PLACE_ZONE_ETC) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_DAY_ETC);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_ETC);
            }
        } else if (timeZone == TIME_ZONE_NIGHT) {
            if (placeZone == PLACE_ZONE_HOME) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_NIGHT_HOME);
            } else if (placeZone == PLACE_ZONE_PUBLIC) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC);
            } else if (placeZone == PLACE_ZONE_ETC) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_ETC);
            }
        } else if (timeZone == TIME_ZONE_LATE_NIGHT) {
            if (placeZone == PLACE_ZONE_HOME) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME);
            } else if (placeZone == PLACE_ZONE_PUBLIC) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC);
            } else if (placeZone == PLACE_ZONE_ETC) {
                equivalentNoise = String.valueOf(DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC);
                highestNoise = String.valueOf(DEFAULT_HIGHEST_NOISE_ETC);
            }
        }
        demonstrationInfo.setStandardEquivalent(equivalentNoise);
        demonstrationInfo.setStandardHighest(highestNoise);

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

    // room db 사용 - 시위 리스트 읽기 (listener 를 사용해 callback event 적용)
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

    // room db 사용 - 배경 소음도 update
    public void updateBackgroundNoise(Context context, DemonstrationInfo demonstrationInfo) {
        // DB 에 update (Rxjava 비동기)
        DemonstrationDataBase.getInstance(context)
                .demonstrationDao()
                .updateDemonstration(demonstrationInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(list -> {
                    // demonstrationList 에 update 반영
                    for (int i = 0; i < demonstrationList.size(); i++) {
                        if (demonstrationList.get(i).getId() == demonstrationInfo.getId()) {
                            demonstrationList.get(i).setBackgroundNoiseLevel(demonstrationInfo.getBackgroundNoiseLevel());
                        }
                    }
                    databaseListener.onChanged();
                }).subscribe();
    }

    // 시위 리스트 순서에 맞게 정렬
    // 1. 진행 중, 진행 예정, 종료 순서
    // 2. 현재 시간과 가까운 순서
    private void sortDemonstrationList() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

        // 시위 리스트 정렬
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

                // 날짜의 진행 중, 진행 예정, 종료 상태를 저장
                int infoStatus1 = getCompareToCurrentDateStatus(startDate1, endDate1);
                int infoStatus2 = getCompareToCurrentDateStatus(startDate2, endDate2);

                // 진행 중, 진행 예정, 종료 상태 순으로 정렬
                if (infoStatus1 < infoStatus2) {
                    return -1;
                } else if (infoStatus1 > infoStatus2) {
                    return 1;
                } else {
                    // 상태가 같다면 start date 기준 현재 시간과 가까운 순으로 정렬
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

    // DB 비동기 작업 완료를 알리는 listener 역할
    public interface DatabaseListener {
        void onChanged();
    }
}
