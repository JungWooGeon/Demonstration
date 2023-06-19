package com.police.demonstration.manage_demonstration.measurement.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MeasurementInfo.class}, version = 1, exportSchema = false)
public abstract class MeasurementDataBase extends RoomDatabase {
    public abstract MeasurementDao measurementDao();

    private static MeasurementDataBase database;
    private static final String MEASUREMENT_DATABASE_NAME = "measurementDatabaseName";

    public synchronized static MeasurementDataBase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), MeasurementDataBase.class, MEASUREMENT_DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
