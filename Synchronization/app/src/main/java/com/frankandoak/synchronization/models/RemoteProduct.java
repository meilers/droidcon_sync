package com.frankandoak.synchronization.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.frankandoak.synchronization.database.CategoryProductTable;
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
public class RemoteProduct extends RemoteObject {

    public static final String[] IDENTIFIERS = new String[] {
            ProductTable.PRODUCT_ID
    };


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


    public RemoteProduct(final Cursor cursor) {

        super(cursor);

        setProductId(cursor.getLong(cursor.getColumnIndex(ProductTable.PRODUCT_ID)));
        setSku(cursor.getString(cursor.getColumnIndex(ProductTable.SKU)));
        setName(cursor.getString(cursor.getColumnIndex(ProductTable.NAME)));
        setImageUrl(cursor.getString(cursor.getColumnIndex(ProductTable.IMAGE_URL)));
        setFavoriteCount(cursor.getInt(cursor.getColumnIndex(ProductTable.FAVORITE_COUNT)));
        setPrice(cursor.getFloat(cursor.getColumnIndex(ProductTable.PRICE)));
    }

    @Override
    public LinkedHashMap<String, String> getIdentifiers() {

        LinkedHashMap<String, String> identifiers = super.getIdentifiers();
        identifiers.put(IDENTIFIERS[0], getProductId() + "");

        return identifiers;
    }

    @Override
    public void populateContentValues(ContentValues values) {
        super.populateContentValues(values);

        values.put(ProductTable.PRODUCT_ID, getProductId());
        values.put(ProductTable.SKU, getSku());
        values.put(ProductTable.NAME, getName());
        values.put(ProductTable.IMAGE_URL, getImageUrl());
        values.put(ProductTable.FAVORITE_COUNT, getFavoriteCount());
        values.put(ProductTable.PRICE, getPrice());
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