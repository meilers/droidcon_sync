package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class FavoriteTable {

    public static final String TABLE_NAME = "favorite";

    public static final String _ID = TABLE_NAME + "_" + "_id";
    public static final String CREATED_AT = TABLE_NAME + "_" + "createdAt";
    public static final String UPDATED_AT = TABLE_NAME + "_" + "updatedAt";
    public static final String SYNC_STATUS = TABLE_NAME + "_" + "syncStatus";
    public static final String IS_DELETED = TABLE_NAME + "_" + "isDeleted";
    public static final String PRODUCT_ID = TABLE_NAME + "_" + "productId";


    public static String[] ALL_COLUMNS = new String[]{
            _ID,
            CREATED_AT,
            UPDATED_AT,
            SYNC_STATUS,
            IS_DELETED,
            PRODUCT_ID
    };


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + _ID + " integer primary key autoincrement not null, "
            + CREATED_AT + " text,"
            + UPDATED_AT + " text,"
            + SYNC_STATUS + " integer not null,"
            + IS_DELETED + " integer not null,"
            + PRODUCT_ID + " integer not null"

            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(FavoriteTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
