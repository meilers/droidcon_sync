package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.frankandoak.synchronization.database.BaseTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Michael on 2014-03-11.
 */
public abstract class RemoteObject implements Parcelable {

    public static enum SyncStatus
    {
        NO_CHANGES, QUEUED_TO_SYNC, TEMP;

        public static SyncStatus getSyncStatusFromCode(Integer code) {
            if (code == null || code < 0 || code > values().length - 1) {
                return NO_CHANGES;
            }

            return values()[code];
        }
    }


    private Long mId;

    @Expose
    @SerializedName("created_at")
    private String mCreatedAt;

    @Expose
    @SerializedName("updated_at")
    private String mUpdatedAt;

    private SyncStatus mSyncStatus;
    private Boolean mIsDeleted;


    public RemoteObject(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(BaseTable._ID)));
        setCreatedAt(cursor.getString(cursor.getColumnIndex(BaseTable.CREATED_AT)));
        setUpdatedAt(cursor.getString(cursor.getColumnIndex(BaseTable.UPDATED_AT)));
        setSyncStatus(SyncStatus.getSyncStatusFromCode(cursor.getInt(cursor.getColumnIndex(BaseTable.SYNC_STATUS))));
        setIsDeleted(cursor.getInt(cursor.getColumnIndex(BaseTable.IS_DELETED)) == 1);
    }

    protected RemoteObject(Long id, String createdAt, String updatedAt, SyncStatus syncStatus, Boolean isDeleted) {
        mId = id;
        mCreatedAt = createdAt;
        mUpdatedAt = updatedAt;
        mSyncStatus = syncStatus;
        mIsDeleted = isDeleted;
    }



    public RemoteObject(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.getId() != null ? 1:0);

        if( this.getId() != null )
            dest.writeLong(this.getId());



        dest.writeInt(this.getCreatedAt() != null ? 1:0);

        if( this.getCreatedAt() != null )
            dest.writeString(this.getCreatedAt());



        dest.writeInt(this.getUpdatedAt() != null ? 1:0);

        if( this.getUpdatedAt() != null )
            dest.writeString(this.getUpdatedAt());


        dest.writeInt(this.getSyncStatus() != null ? 1:0);

        if( this.getSyncStatus() != null )
            dest.writeInt(this.getSyncStatus().ordinal());


        dest.writeInt(this.getIsDeleted() != null ? 1:0);

        if( this.getIsDeleted() != null )
            dest.writeInt(this.getIsDeleted() ? 1:0);

    }


    public void readFromParcel(Parcel in) {

        if( in.readInt() == 1 )
            setId(in.readLong());

        if( in.readInt() == 1 )
            setCreatedAt(in.readString());

        if( in.readInt() == 1 )
            setUpdatedAt(in.readString());

        if( in.readInt() == 1 )
            setSyncStatus(SyncStatus.getSyncStatusFromCode(in.readInt()));

        if( in.readInt() == 1 )
            setIsDeleted(in.readInt() == 1);
    }



    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public SyncStatus getSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        mSyncStatus = syncStatus;
    }

    public Boolean getIsDeleted() {
        return mIsDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        mIsDeleted = isDeleted;
    }

    public LinkedHashMap<String,String> getIdentifiers()
    {
        LinkedHashMap<String, String> identifiers = new LinkedHashMap<>();
        return identifiers;
    }

    public void populateContentValues(ContentValues values) {

        values.put(BaseTable.CREATED_AT, getCreatedAt());
        values.put(BaseTable.UPDATED_AT, getUpdatedAt());
        values.put(BaseTable.SYNC_STATUS, getSyncStatus() != null ? getSyncStatus().ordinal() : null);
        values.put(BaseTable.IS_DELETED, getIsDeleted());
    };
}

