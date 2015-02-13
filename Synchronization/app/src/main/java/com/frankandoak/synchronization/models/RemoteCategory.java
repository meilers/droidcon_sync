package com.frankandoak.synchronization.models;

import android.database.Cursor;

import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class RemoteCategory extends RemoteObject {

    private Long mId;

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
        setId(cursor.getLong(cursor.getColumnIndex(CategoryTable.ID)));
        setCategoryId(cursor.getLong(cursor.getColumnIndex(CategoryTable.CATEGORY_ID)));
        setName(cursor.getString(cursor.getColumnIndex(CategoryTable.NAME)));
        setImageUrl(cursor.getString(cursor.getColumnIndex(CategoryTable.IMAGE_URL)));
    }

    public static List<String> getIdentifierKeys() {

        List<String> keys = new ArrayList<>();
        keys.add(CategoryTable.CATEGORY_ID);

        return keys;
    }

    @Override
    public List<String> getIdentifierValues() {
        Long categoryId = getCategoryId();

        if( categoryId != null )
        {
            List<String> values = new ArrayList<>();
            values.add(categoryId + "");

            return values;
        }

        return null;
    }


    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
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