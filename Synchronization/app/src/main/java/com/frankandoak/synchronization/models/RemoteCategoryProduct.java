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
public class RemoteCategoryProduct extends RemoteObject {

    private Long mId;
    private Long mCategoryId;
    private Long mProductId;

    private RemoteCategory mCategory;
    private RemoteProduct mProduct;

    public RemoteCategoryProduct(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.ID)));
        setCategoryId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.CATEGORY_ID)));
        setProductId(cursor.getLong(cursor.getColumnIndex(CategoryProductTable.PRODUCT_ID)));
    }

    public static List<String> getIdentifierKeys() {

        List<String> keys = new ArrayList<>();
        keys.add(CategoryProductTable.CATEGORY_ID);
        keys.add(CategoryProductTable.PRODUCT_ID);
        return keys;
    }



    @Override
    public List<String> getIdentifierValues() {
        Long categoryId = getCategoryId();
        Long productId = getProductId();

        if( categoryId != null && productId != null )
        {
            List<String> values = new ArrayList<>();
            values.add(categoryId + "");
            values.add(productId + "");

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