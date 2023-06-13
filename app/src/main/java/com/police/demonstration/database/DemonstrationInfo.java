package com.police.demonstration.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class DemonstrationInfo {
    @PrimaryKey(autoGenerate = true) int id;
    @ColumnInfo(name = "name") String name;
    @ColumnInfo (name = "groupName") String groupName;
    @ColumnInfo (name = "startDate") String startDate;
    @ColumnInfo (name = "endDate") String endDate;
    @ColumnInfo (name = "timeZone") String timeZone;
    @ColumnInfo (name = "place") String place;
    @ColumnInfo (name = "placeZone") String placeZone;
    @ColumnInfo (name = "organizerName") String organizerName;
    @ColumnInfo (name = "organizerPhoneNumber") String organizerPhoneNumber;
    @ColumnInfo (name = "organizerPosition") String organizerPosition;
    @ColumnInfo (name = "backgroundNoiseLevel") String backgroundNoiseLevel;

    public String getName() { return name; }
    public String getPlace() { return place; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getGroupName() { return groupName; }

    public DemonstrationInfo(String name, String groupName, String startDate, String endDate, String timeZone, String place, String placeZone, String organizerName, String organizerPosition, String organizerPhoneNumber, String backgroundNoiseLevel) {
        this.name = name;
        this.groupName = groupName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeZone = timeZone;
        this.place = place;
        this.placeZone = placeZone;
        this.organizerName = organizerName;
        this.organizerPhoneNumber = organizerPhoneNumber;
        this.organizerPosition = organizerPosition;
        this.backgroundNoiseLevel = backgroundNoiseLevel;
    }
}