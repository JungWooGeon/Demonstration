package com.police.demonstration.database.demonstration;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DemonstrationInfo implements Parcelable {
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
    @ColumnInfo(name = "standardEquivalent") String standardEquivalent;
    @ColumnInfo(name = "standardHighest") String standardHighest;

    public DemonstrationInfo(String name, String groupName, String startDate, String endDate, String timeZone, String place, String placeZone, String organizerName, String organizerPhoneNumber, String organizerPosition, String backgroundNoiseLevel) {
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

    protected DemonstrationInfo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        groupName = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        timeZone = in.readString();
        place = in.readString();
        placeZone = in.readString();
        organizerName = in.readString();
        organizerPhoneNumber = in.readString();
        organizerPosition = in.readString();
        backgroundNoiseLevel = in.readString();
        standardEquivalent = in.readString();
        standardHighest = in.readString();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPlace() { return place; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getGroupName() { return groupName; }
    public int getTimeZone() { return Integer.parseInt(timeZone); }
    public int getPlaceZone() { return Integer.parseInt(placeZone); }
    public String getOrganizerName() { return organizerName; }
    public String getOrganizerPhoneNumber() { return organizerPhoneNumber; }
    public String getOrganizerPosition() { return organizerPosition; }
    public String getBackgroundNoiseLevel() { return backgroundNoiseLevel; }
    public String getStandardEquivalent() { return standardEquivalent; }
    public String getStandardHighest() { return standardHighest; }

    public void setBackgroundNoiseLevel(String backgroundNoiseLevel) { this.backgroundNoiseLevel = backgroundNoiseLevel;}
    public void setStandardEquivalent(String standardEquivalent) { this.standardEquivalent = standardEquivalent; }
    public void setStandardHighest(String standardHighest) { this.standardHighest = standardHighest; }

    public static final Creator<DemonstrationInfo> CREATOR = new Creator<DemonstrationInfo>() {
        @Override
        public DemonstrationInfo createFromParcel(Parcel in) {
            return new DemonstrationInfo(in);
        }

        @Override
        public DemonstrationInfo[] newArray(int size) {
            return new DemonstrationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(groupName);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(timeZone);
        parcel.writeString(place);
        parcel.writeString(placeZone);
        parcel.writeString(organizerName);
        parcel.writeString(organizerPhoneNumber);
        parcel.writeString(organizerPosition);
        parcel.writeString(backgroundNoiseLevel);
        parcel.writeString(standardEquivalent);
        parcel.writeString(standardHighest);
    }
}