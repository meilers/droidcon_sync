package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;

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
public abstract class RemoteObject {

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

