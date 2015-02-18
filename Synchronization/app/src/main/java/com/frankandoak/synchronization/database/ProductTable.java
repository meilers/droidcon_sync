package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class ProductTable extends BaseTable {

    public static final String TABLE_NAME = "product";

    public static final String PRODUCT_ID = "productId";
    public static final String SKU = "sku";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "imageUrl";
    public static final String FAVORITE_COUNT = "favoriteCount";
    public static final String PRICE = "price";

    // FULL NAMES FOR DISAMBIGUATION
    public static final String FULL_ID = TABLE_NAME + "." + _ID;
    public static final String FULL_CREATED_AT = TABLE_NAME + "." + CREATED_AT;
    public static final String FULL_UPDATED_AT = TABLE_NAME + "." + UPDATED_AT;
    public static final String FULL_SYNC_STATUS = TABLE_NAME + "." + SYNC_STATUS;
    public static final String FULL_IS_DELETED = TABLE_NAME + "." + IS_DELETED;
    public static final String FULL_PRODUCT_ID = TABLE_NAME + "." + PRODUCT_ID;
    public static final String FULL_SKU = TABLE_NAME + "." + SKU;
    public static final String FULL_NAME = TABLE_NAME + "." + NAME;
    public static final String FULL_IMAGE_URL = TABLE_NAME + "." + IMAGE_URL;
    public static final String FULL_FAVORITE_COUNT = TABLE_NAME + "." + FAVORITE_COUNT;
    public static final String FULL_PRICE = TABLE_NAME + "." + PRICE;


    public static String[] ALL_COLUMNS = new String[]{
            _ID,
            CREATED_AT,
            UPDATED_AT,
            SYNC_STATUS,
            IS_DELETED,
            PRODUCT_ID,
            SKU,
            NAME,
            IMAGE_URL,
            FAVORITE_COUNT,
            PRICE
    };

    public static String[] FULL_ALL_COLUMNS = new String[]{
            FULL_ID,
            FULL_CREATED_AT,
            FULL_UPDATED_AT,
            FULL_SYNC_STATUS,
            FULL_IS_DELETED,
            FULL_PRODUCT_ID,
            FULL_SKU,
            FULL_NAME,
            FULL_IMAGE_URL,
            FULL_FAVORITE_COUNT,
            FULL_PRICE
    };



    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + _ID + " integer primary key autoincrement, "
            + CREATED_AT + " text,"
            + UPDATED_AT + " text,"
            + SYNC_STATUS + " integer,"
            + IS_DELETED + " integer,"
            + PRODUCT_ID + " integer not null, "
            + SKU + " text not null,"
            + NAME + " text not null,"
            + IMAGE_URL + " text not null,"
            + FAVORITE_COUNT + " integer not null,"
            + PRICE + " real not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ProductTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
