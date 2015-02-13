package com.frankandoak.synchronization.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Michael on 2014-03-10.
 */
public class CategoryTable {

    public static final String TABLE_NAME = "category";
    public static final String ID = "_id";
    public static final String CATEGORY_ID = "categoryId";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "imageUrl";

    // Full names for disambiguation
    public static final String FULL_ID = TABLE_NAME + "." + ID;
    public static final String FULL_CATEGORY_ID = TABLE_NAME + "." + CATEGORY_ID;
    public static final String FULL_NAME = TABLE_NAME + "." + NAME;
    public static final String FULL_IMAGE_URL = TABLE_NAME + "." + IMAGE_URL;


    public static String[] ALL_COLUMNS = new String[]{ID,
            CATEGORY_ID,
            NAME,
            IMAGE_URL
    };

    public static String[] FULL_ALL_COLUMNS = new String[]{FULL_ID, FULL_CATEGORY_ID, FULL_NAME, FULL_IMAGE_URL};


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " integer primary key autoincrement, "
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
