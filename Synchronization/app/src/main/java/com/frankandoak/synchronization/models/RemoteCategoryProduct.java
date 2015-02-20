package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class RemoteCategoryProduct extends RemoteObject {

    public static final String[] IDENTIFIERS = new String[] {
        CategoryProductTable.CATEGORY_ID,
        CategoryProductTable.PRODUCT_ID
    };

    private Long mCategoryId;
    private Long mProductId;
    private Integer mPosition;

    public RemoteCategoryProduct(Long id, String createdAt, String updatedAt, SyncStatus syncStatus, Boolean isDeleted, Long categoryId, Long productId, Integer position) {
        super(id, createdAt, updatedAt, syncStatus, isDeleted);
        mCategoryId = categoryId;
        mProductId = productId;
        mPosition = position;
    }

    public RemoteCategoryProduct(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable._ID)));
        setCreatedAt(cursor.getString(cursor.getColumnIndex(CategoryProductTable.CREATED_AT)));
        setUpdatedAt(cursor.getString(cursor.getColumnIndex(CategoryProductTable.UPDATED_AT)));
        setSyncStatus(SyncStatus.getSyncStatusFromCode(cursor.getInt(cursor.getColumnIndex(CategoryProductTable.SYNC_STATUS))));
        setIsDeleted(cursor.getInt(cursor.getColumnIndex(CategoryProductTable.IS_DELETED)) == 1);
        setCategoryId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.CATEGORY_ID)));
        setProductId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.PRODUCT_ID)));
        setPosition(cursor.getInt(cursor.getColumnIndex(CategoryProductTable.POSITION)));

    }

    @Override
    public LinkedHashMap<String, String> getIdentifiers() {

        LinkedHashMap<String, String> identifiers = super.getIdentifiers();
        identifiers.put(IDENTIFIERS[0], getCategoryId() + "");
        identifiers.put(IDENTIFIERS[1], getProductId() + "");

        return identifiers;
    }

    @Override
    public void populateContentValues(ContentValues values) {
        values.put(CategoryProductTable.CREATED_AT, getCreatedAt());
        values.put(CategoryProductTable.UPDATED_AT, getUpdatedAt());
        values.put(CategoryProductTable.SYNC_STATUS, getSyncStatus() != null ? getSyncStatus().ordinal() : null);
        values.put(CategoryProductTable.IS_DELETED, getIsDeleted());

        values.put(CategoryProductTable.CATEGORY_ID, getCategoryId());
        values.put(CategoryProductTable.PRODUCT_ID, getProductId());
        values.put(CategoryProductTable.POSITION, getPosition());

    }

    public Long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(Long categoryId) {
        mCategoryId = categoryId;
    }

    public Long getProductId() {
        return mProductId;
    }

    public void setProductId(Long productId) {
        mProductId = productId;
    }

    public Integer getPosition() {
        return mPosition;
    }

    public void setPosition(Integer position) {
        mPosition = position;
    }
}