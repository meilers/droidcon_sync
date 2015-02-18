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
public class RemoteCategory extends RemoteObject {

    public static final String[] IDENTIFIERS = new String[] {
            CategoryTable.CATEGORY_ID
    };


    @Expose
    @SerializedName("id")
    private Long mCategoryId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("image_url")
    private String mImageUrl;

    public RemoteCategory(final Cursor cursor) {
        super(cursor);

        setCategoryId(cursor.getLong(cursor.getColumnIndex(CategoryTable.CATEGORY_ID)));
        setName(cursor.getString(cursor.getColumnIndex(CategoryTable.NAME)));
        setImageUrl(cursor.getString(cursor.getColumnIndex(CategoryTable.IMAGE_URL)));
    }

    @Override
    public LinkedHashMap<String, String> getIdentifiers() {

        LinkedHashMap<String, String> identifiers = super.getIdentifiers();
        identifiers.put(IDENTIFIERS[0], getCategoryId()+"");

        return identifiers;
    }

    @Override
    public void populateContentValues(ContentValues values) {
        super.populateContentValues(values);

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