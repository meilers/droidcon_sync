package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class ProductTable {

    public static final String TABLE_NAME = "product";

    public static final String ID = "_id";
    public static final String PRODUCT_ID = "productId";
    public static final String SKU = "sku";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "imageUrl";
    public static final String FAVORITE_COUNT = "favoriteCount";
    public static final String PRICE = "price";

    // FULL NAMES FOR DISAMBIGUATION
    public static final String FULL_ID = TABLE_NAME + "." + ID;
    public static final String FULL_PRODUCT_ID = TABLE_NAME + "." + PRODUCT_ID;
    public static final String FULL_SKU = TABLE_NAME + "." + SKU;
    public static final String FULL_NAME = TABLE_NAME + "." + NAME;
    public static final String FULL_IMAGE_URL = TABLE_NAME + "." + IMAGE_URL;
    public static final String FULL_FAVORITE_COUNT = TABLE_NAME + "." + FAVORITE_COUNT;


    public static String[] ALL_COLUMNS = new String[]{ID,
            PRODUCT_ID,
            SKU,
            NAME,
            IMAGE_URL,
            FAVORITE_COUNT,
            PRICE};

    public static String[] FULL_ALL_COLUMNS = new String[]{FULL_PRODUCT_ID, FULL_SKU, FULL_NAME, FULL_IMAGE_URL, FULL_FAVORITE_COUNT};



    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " integer primary key autoincrement, "
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
