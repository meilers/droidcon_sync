package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.FavoriteTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;

/**
 * Created by Michael on 2014-03-17.
 */
public class RemoteFavorite extends RemoteObject {

    public static final String[] IDENTIFIERS = new String[] {
            FavoriteTable.PRODUCT_ID
    };

    private Long mProductId;

    public RemoteFavorite(Long id, String createdAt, String updatedAt, SyncStatus syncStatus, Boolean isDeleted, Long productId) {
        super(id, createdAt, updatedAt, syncStatus, isDeleted);
        mProductId = productId;
    }

    public RemoteFavorite(final Cursor cursor) {

        if( !cursor.isNull(cursor.getColumnIndex(FavoriteTable._ID))) {
            setId(cursor.getLong(cursor.getColumnIndex(FavoriteTable._ID)));
            setCreatedAt(cursor.getString(cursor.getColumnIndex(FavoriteTable.CREATED_AT)));
            setUpdatedAt(cursor.getString(cursor.getColumnIndex(FavoriteTable.UPDATED_AT)));
            setSyncStatus(SyncStatus.getSyncStatusFromCode(cursor.getInt(cursor.getColumnIndex(FavoriteTable.SYNC_STATUS))));
            setIsDeleted(cursor.getInt(cursor.getColumnIndex(FavoriteTable.IS_DELETED)) == 1);
            setProductId(cursor.getLong(cursor.getColumnIndex(FavoriteTable.PRODUCT_ID)));
        }
    }

    @Override
    public LinkedHashMap<String, String> getIdentifiers() {

        LinkedHashMap<String, String> identifiers = super.getIdentifiers();
        identifiers.put(IDENTIFIERS[0], getProductId()+"");

        return identifiers;
    }

    @Override
    public void populateContentValues(ContentValues values) {
        values.put(FavoriteTable.CREATED_AT, getCreatedAt());
        values.put(FavoriteTable.UPDATED_AT, getUpdatedAt());
        values.put(FavoriteTable.SYNC_STATUS, getSyncStatus() != null ? getSyncStatus().ordinal() : null);
        values.put(FavoriteTable.IS_DELETED, getIsDeleted());
        values.put(FavoriteTable.PRODUCT_ID, getProductId());
    }

    public Long getProductId() {
        return mProductId;
    }

    public void setProductId(Long productId) {
        mProductId = productId;
    }
}