package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class CategoryProductTable {

    public static final String TABLE_NAME = "categoryProduct";

    public static final String ID = "_id";
    public static final String CATEGORY_ID = "categoryId";
    public static final String PRODUCT_ID = "productId";

    // Full names for disambiguation
    public static final String FULL_ID = TABLE_NAME + "." + ID;
    public static final String FULL_CATEGORY_ID = TABLE_NAME + "." + CATEGORY_ID;
    public static final String FULL_PRODUCT_ID = TABLE_NAME + "." + PRODUCT_ID;


    public static String[] ALL_COLUMNS = new String[]{
            ID,
            CATEGORY_ID,
            PRODUCT_ID
    };

    public static String[] FULL_ALL_COLUMNS = new String[]{FULL_ID, FULL_CATEGORY_ID, FULL_PRODUCT_ID};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " integer primary key autoincrement, "
            + CATEGORY_ID + " integer not null,"
            + PRODUCT_ID + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(CategoryProductTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
