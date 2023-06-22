package com.police.demonstration.database.demonstration;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DemonstrationDao {
    @Query("SELECT * FROM demonstrationInfo")
    Single<List<DemonstrationInfo>> getAll();

    @Insert
    Completable addDemonstration(DemonstrationInfo demonstrationInfo);

    @Update
    Completable updateDemonstration(DemonstrationInfo demonstrationInfo);
}