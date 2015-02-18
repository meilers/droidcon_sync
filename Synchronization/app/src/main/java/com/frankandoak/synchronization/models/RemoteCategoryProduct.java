package com.frankandoak.synchronization.models;

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

    private Long mId;
    private Long mCategoryId;
    private Long mProductId;

    private RemoteCategory mCategory;
    private RemoteProduct mProduct;

    public RemoteCategoryProduct(final Cursor cursor) {
        super(cursor);

        setCategoryId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.CATEGORY_ID)));
        setProductId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.PRODUCT_ID)));
    }

    @Override
    public LinkedHashMap<String, String> getIdentifiers() {

        LinkedHashMap<String, String> identifiers = super.getIdentifiers();
        identifiers.put(IDENTIFIERS[0], getCategoryId() + "");
        identifiers.put(IDENTIFIERS[1], getProductId() + "");

        return identifiers;
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

    public Long getProductId() {
        return mProductId;
    }

    public void setProductId(Long productId) {
        mProductId = productId;
    }

    public RemoteCategory getCategory() {
        return mCategory;
    }

    public void setCategory(RemoteCategory category) {
        mCategory = category;
    }

    public RemoteProduct getProduct() {
        return mProduct;
    }

    public void setProduct(RemoteProduct product) {
        mProduct = product;
    }

}