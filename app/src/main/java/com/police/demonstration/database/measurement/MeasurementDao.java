package com.police.demonstration.database.measurement;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MeasurementDao {
    @Query("SELECT * FROM measurementInfo")
    Single<List<MeasurementInfo>> getAll();

    @Insert
    Completable addMeasurement(MeasurementInfo measurementInfo);

    @Query("SELECT * FROM measurementinfo WHERE demonstrationId == :demonstrationId")
    Single<List<MeasurementInfo>> getMeasurement(int demonstrationId);
}
