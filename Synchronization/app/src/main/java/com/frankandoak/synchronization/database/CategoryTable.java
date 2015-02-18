package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class CategoryTable extends BaseTable {

    public static final String TABLE_NAME = "category";

    public static final String CATEGORY_ID = "categoryId";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "imageUrl";

    // Full names for disambiguation
    public static final String FULL_ID = TABLE_NAME + "." + _ID;
    public static final String FULL_CREATED_AT = TABLE_NAME + "." + CREATED_AT;
    public static final String FULL_UPDATED_AT = TABLE_NAME + "." + UPDATED_AT;
    public static final String FULL_SYNC_STATUS = TABLE_NAME + "." + SYNC_STATUS;
    public static final String FULL_IS_DELETED = TABLE_NAME + "." + IS_DELETED;
    public static final String FULL_CATEGORY_ID = TABLE_NAME + "." + CATEGORY_ID;
    public static final String FULL_NAME = TABLE_NAME + "." + NAME;
    public static final String FULL_IMAGE_URL = TABLE_NAME + "." + IMAGE_URL;


    public static String[] ALL_COLUMNS = new String[]{
            _ID,
            CREATED_AT,
            UPDATED_AT,
            SYNC_STATUS,
            IS_DELETED,
            CATEGORY_ID,
            NAME,
            IMAGE_URL
    };

    public static String[] FULL_ALL_COLUMNS = new String[]{
            FULL_ID,
            FULL_CREATED_AT,
            FULL_UPDATED_AT,
            FULL_SYNC_STATUS,
            FULL_IS_DELETED,
            FULL_CATEGORY_ID,
            FULL_NAME,
            FULL_IMAGE_URL
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
            + CATEGORY_ID + " integer not null, "
            + NAME + " text not null,"
            + IMAGE_URL + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(CategoryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

}
