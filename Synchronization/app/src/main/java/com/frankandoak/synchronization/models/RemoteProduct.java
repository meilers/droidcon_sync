package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class RemoteProduct extends RemoteObject {

    private Long mId;

    @Expose
    @SerializedName("id")
    private Long mProductId;

    @Expose
    @SerializedName("product_base_sku")
    private String mSku;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("image_url")
    private String mImageUrl;

    @Expose
    @SerializedName("favorite_count")
    private Integer mFavoriteCount;

    @Expose
    @SerializedName("original_price")
    private float mPrice;


    public RemoteProduct() {}


    public RemoteProduct(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ProductTable.ID)));
        setProductId(cursor.getLong(cursor.getColumnIndex(ProductTable.PRODUCT_ID)));
        setSku(cursor.getString(cursor.getColumnIndex(ProductTable.SKU)));
        setName(cursor.getString(cursor.getColumnIndex(ProductTable.NAME)));
        setImageUrl(cursor.getString(cursor.getColumnIndex(ProductTable.IMAGE_URL)));
        setFavoriteCount(cursor.getInt(cursor.getColumnIndex(ProductTable.FAVORITE_COUNT)));
        setPrice(cursor.getFloat(cursor.getColumnIndex(ProductTable.PRICE)));
    }

    public static List<String> getIdentifierKeys() {

        List<String> keys = new ArrayList<>();
        keys.add(ProductTable.PRODUCT_ID);

        return keys;
    }

    @Override
    public List<String> getIdentifierValues() {

        Long productId = getProductId();

        if( productId != null )
        {
            List<String> values = new ArrayList<>();
            values.add(productId + "");

            return values;
        }

        return null;
    }

    public void populateContentValues(ContentValues values) {
        values.put(ProductTable.PRODUCT_ID, getProductId());
        values.put(ProductTable.SKU, getSku());
        values.put(ProductTable.NAME, getName());
        values.put(ProductTable.IMAGE_URL, getImageUrl());
        values.put(ProductTable.FAVORITE_COUNT, getFavoriteCount());
    }


    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getProductId() {
        return mProductId;
    }

    public void setProductId(Long productId) {
        mProductId = productId;
    }

    public String getSku() {
        return mSku;
    }

    public void setSku(String sku) {
        mSku = sku;
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

    public Integer getFavoriteCount() {
        return mFavoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        mFavoriteCount = favoriteCount;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        mPrice = price;
    }
}