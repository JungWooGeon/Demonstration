package com.police.demonstration.database.notification;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NotificationInfo.class}, version = 1, exportSchema = false)
public abstract class NotificationDataBase extends RoomDatabase {
    public abstract NotificationDao notificationDao();

    private static NotificationDataBase database;
    private static final String NOTIFICATION_DATABASE_NAME = "notificationDatabaseName";

    public synchronized static NotificationDataBase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), NotificationDataBase.class, NOTIFICATION_DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
