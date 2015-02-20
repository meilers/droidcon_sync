package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.frankandoak.synchronization.database.CategoryTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class RemoteCategory extends RemoteObject implements Parcelable {

    public static final String[] IDENTIFIERS = new String[] {
            CategoryTable.CATEGORY_ID
    };


    @Expose
    @SerializedName("store_id")
    private Long mCategoryId;

    @Expose
    @SerializedName("store_name")
    private String mName;

    @Expose
    @SerializedName("store_image_url")
    private String mImageUrl;

    public RemoteCategory(final Cursor cursor) {

        setId(cursor.getLong(cursor.getColumnIndex(CategoryTable._ID)));
        setCreatedAt(cursor.getString(cursor.getColumnIndex(CategoryTable.CREATED_AT)));
        setUpdatedAt(cursor.getString(cursor.getColumnIndex(CategoryTable.UPDATED_AT)));
        setSyncStatus(SyncStatus.getSyncStatusFromCode(cursor.getInt(cursor.getColumnIndex(CategoryTable.SYNC_STATUS))));
        setIsDeleted(cursor.getInt(cursor.getColumnIndex(CategoryTable.IS_DELETED)) == 1);
        setCategoryId(cursor.getLong(cursor.getColumnIndex(CategoryTable.CATEGORY_ID)));
        setName(cursor.getString(cursor.getColumnIndex(CategoryTable.NAME)));
        setImageUrl(cursor.getString(cursor.getColumnIndex(CategoryTable.IMAGE_URL)));
    }


    public RemoteCategory(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);


        dest.writeInt(this.getCategoryId() != null ? 1:0);

        if( this.getCategoryId() != null )
            dest.writeLong(this.getCategoryId());


        dest.writeInt(this.getName() != null ? 1:0);

        if( this.getName() != null )
            dest.writeString(this.getName());



        dest.writeInt(this.getImageUrl() != null ? 1:0);

        if( this.getImageUrl() != null )
            dest.writeString(this.getImageUrl());
    }


    public void readFromParcel(Parcel in) {

        super.readFromParcel(in);

        if( in.readInt() == 1 )
            setCategoryId(in.readLong());

        if( in.readInt() == 1 )
            setName(in.readString());

        if( in.readInt() == 1 )
            setImageUrl(in.readString());

    }

    public static Creator<RemoteCategory> CREATOR = new Creator<RemoteCategory>() {

        @Override
        public RemoteCategory createFromParcel(Parcel in) {
            return new RemoteCategory(in);
        }

        @Override
        public RemoteCategory[] newArray(int size) {
            return new RemoteCategory[size];
        }
    };


    @Override
    public LinkedHashMap<String, String> getIdentifiers() {

        LinkedHashMap<String, String> identifiers = super.getIdentifiers();
        identifiers.put(IDENTIFIERS[0], getCategoryId()+"");

        return identifiers;
    }

    @Override
    public void populateContentValues(ContentValues values) {
        values.put(CategoryTable.CREATED_AT, getCreatedAt());
        values.put(CategoryTable.UPDATED_AT, getUpdatedAt());
        values.put(CategoryTable.SYNC_STATUS, getSyncStatus() != null ? getSyncStatus().ordinal() : null);
        values.put(CategoryTable.IS_DELETED, getIsDeleted());

        values.put(CategoryTable.CATEGORY_ID, getCategoryId());
        values.put(CategoryTable.NAME, getName());
        values.put(CategoryTable.IMAGE_URL, getImageUrl());
    }

    public Long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(Long categoryId) {
        mCategoryId = categoryId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}