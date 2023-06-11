package com.police.demonstration.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DemonstrationDao {
    @Query("SELECT * FROM demonstrationInfo")
    List<DemonstrationInfo> getAll();

    @Insert
    void addDemonstration(DemonstrationInfo demonstrationInfo);
}