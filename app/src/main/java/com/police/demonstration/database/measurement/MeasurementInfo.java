package com.police.demonstration.database.measurement;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MeasurementInfo implements Parcelable {
    @PrimaryKey(autoGenerate = true) int id;
    @ColumnInfo(name = "demonstrationId") int demonstrationId;
    @ColumnInfo(name = "startTime") String startTime;
    @ColumnInfo(name = "endTime") String endTime;
    @ColumnInfo(name = "place") String place;
    @ColumnInfo(name = "detailPlace") String detailPlace;
    @ColumnInfo(name = "distance") String distance;
    @ColumnInfo(name = "winterSpeed") String winterSpeed;
    @ColumnInfo(name = "measurementEquivalent") String measurementEquivalent;
    @ColumnInfo(name = "measurementHighest") String measurementHighest;
    @ColumnInfo(name = "correctionEquivalent") String correctionEquivalent;
    @ColumnInfo(name = "correctionHighest") String correctionHighest;

    public MeasurementInfo(int demonstrationId, String startTime, String endTime, String place,
                           String detailPlace, String distance, String winterSpeed,
                           String measurementEquivalent, String measurementHighest,
                           String correctionEquivalent, String correctionHighest) {
        this.demonstrationId = demonstrationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.detailPlace = detailPlace;
        this.distance = distance;
        this.winterSpeed = winterSpeed;
        this.measurementEquivalent = measurementEquivalent;
        this.measurementHighest = measurementHighest;
        this.correctionEquivalent = correctionEquivalent;
        this.correctionHighest = correctionHighest;
    }

    protected MeasurementInfo(Parcel in) {
        id = in.readInt();
        demonstrationId = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        place = in.readString();
        detailPlace = in.readString();
        distance = in.readString();
        winterSpeed = in.readString();
        measurementEquivalent = in.readString();
        measurementHighest = in.readString();
        correctionEquivalent = in.readString();
        correctionHighest = in.readString();
    }

    public String getStartTime() { return this.startTime; }
    public String getPlace() { return this.place; }
    public String getCorrectionEquivalent() { return this.correctionEquivalent; }
    public String getCorrectionHighest() { return this.correctionHighest; }

    public void setDemonstrationId(int demonstrationId) { this.demonstrationId = demonstrationId; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(demonstrationId);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(place);
        dest.writeString(detailPlace);
        dest.writeString(distance);
        dest.writeString(winterSpeed);
        dest.writeString(measurementEquivalent);
        dest.writeString(measurementHighest);
        dest.writeString(correctionEquivalent);
        dest.writeString(correctionHighest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeasurementInfo> CREATOR = new Creator<MeasurementInfo>() {
        @Override
        public MeasurementInfo createFromParcel(Parcel in) {
            return new MeasurementInfo(in);
        }

        @Override
        public MeasurementInfo[] newArray(int size) {
            return new MeasurementInfo[size];
        }
    };
}