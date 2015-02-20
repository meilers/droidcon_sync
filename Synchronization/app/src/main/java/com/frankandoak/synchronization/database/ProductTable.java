package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class ProductTable {

    public static final String TABLE_NAME = "product";

    public static final String _ID = TABLE_NAME + "_" + "_id";
    public static final String CREATED_AT = TABLE_NAME + "_" + "createdAt";
    public static final String UPDATED_AT = TABLE_NAME + "_" + "updatedAt";
    public static final String SYNC_STATUS = TABLE_NAME + "_" + "syncStatus";
    public static final String IS_DELETED = TABLE_NAME + "_" + "isDeleted";
    public static final String PRODUCT_ID = TABLE_NAME + "_" + "productId";
    public static final String SKU = TABLE_NAME + "_" + "sku";
    public static final String NAME = TABLE_NAME + "_" + "name";
    public static final String IMAGE_URL = TABLE_NAME + "_" + "imageUrl";
    public static final String FAVORITE_COUNT = TABLE_NAME + "_" + "favoriteCount";
    public static final String PRICE = TABLE_NAME + "_" + "price";


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
