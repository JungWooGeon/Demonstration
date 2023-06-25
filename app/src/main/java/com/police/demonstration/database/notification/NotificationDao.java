package com.police.demonstration.database.notification;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NotificationDao {
    @Query("SELECT * FROM NotificationInfo")
    Single<List<NotificationInfo>> getAll();

    @Insert
    Completable addNotification(NotificationInfo notificationInfo);
}
