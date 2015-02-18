package com.frankandoak.synchronization.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michael on 2014-03-10.
 */
public class SYNDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "synchronization.db";
    private static final int DATABASE_VERSION = 1;

    private static SYNDatabaseHelper mInstance = null;

    public static SYNDatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.install
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new SYNDatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private SYNDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        CategoryTable.onCreate(database);
        ProductTable.onCreate(database);
        CategoryProductTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {

        CategoryTable.onUpgrade(database, oldVersion, newVersion);
        ProductTable.onUpgrade(database, oldVersion, newVersion);
        CategoryProductTable.onUpgrade(database, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}

