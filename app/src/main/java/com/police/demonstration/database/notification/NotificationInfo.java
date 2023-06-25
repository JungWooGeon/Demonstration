package com.police.demonstration.database.notification;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotificationInfo implements Parcelable {
    @PrimaryKey(autoGenerate = true) int id;
    @ColumnInfo(name = "measurementId") int measurementId;
    @ColumnInfo(name = "notificationTime") String notificationTime;
    @ColumnInfo(name = "notificationTypeName") String notificationTypeName;
    @ColumnInfo(name = "imageUri") String imageUri;

    public NotificationInfo() {}

    public NotificationInfo(int measurementId, String notificationTime, String notificationTypeName, Uri imageUri) {
        this.measurementId = measurementId;
        this.notificationTime = notificationTime;
        this.notificationTypeName = notificationTypeName;
        this.imageUri = imageUri.toString();
    }

    protected NotificationInfo(Parcel in) {
        id = in.readInt();
        measurementId = in.readInt();
        notificationTime = in.readString();
        notificationTypeName = in.readString();
        imageUri = in.readString();
    }

    public int getId() { return this.id; }
    public int getMeasurementId() { return this.measurementId; }
    public String getNotificationTime() { return this.notificationTime; }
    public String getNotificationTypeName() { return this.notificationTypeName; }
    public Uri getImageUri() { return Uri.parse(this.imageUri); }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(measurementId);
        dest.writeString(notificationTime);
        dest.writeString(notificationTypeName);
        dest.writeString(imageUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationInfo> CREATOR = new Creator<NotificationInfo>() {
        @Override
        public NotificationInfo createFromParcel(Parcel in) {
            return new NotificationInfo(in);
        }

        @Override
        public NotificationInfo[] newArray(int size) {
            return new NotificationInfo[size];
        }
    };
}
