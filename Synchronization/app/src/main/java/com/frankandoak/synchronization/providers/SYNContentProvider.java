package com.frankandoak.synchronization.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.FavoriteTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.database.SYNDatabaseHelper;

import java.util.HashMap;

/**
 * Created by Michael on 2014-03-10.
 */
public class SYNContentProvider extends ContentProvider {

    private static final String TAG = SYNContentProvider.class.getSimpleName();

    public static final String SCHEME = "content";
    public static final String AUTHORITY = "com.frankandoak.synchronization.providers.SYNContentProvider";

    public static final class URIS {

        public static final Uri CATEGORIES_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + Paths.CATEGORIES);
        public static final Uri PRODUCTS_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + Paths.PRODUCTS);
        public static final Uri FAVORITES_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + Paths.FAVORITE_PRODUCTS);
        public static final Uri CATEGORY_PRODUCTS_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + Paths.CATEGORY_PRODUCTS);
        public static final Uri FAVORITE_PRODUCTS_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + Paths.FAVORITE_PRODUCTS);

    }

    public static final class Paths {
        public static final String CATEGORIES = "categories";
        public static final String PRODUCTS = "products";
        public static final String FAVORITES = "favorites";
        public static final String CATEGORY_PRODUCTS = "categoryProducts";
        public static final String FAVORITE_PRODUCTS = "favoriteProducts";
    }

    private static final int CATEGORIES_DIR = 0;
    private static final int CATEGORY_ID = 1;
    private static final int PRODUCTS_DIR = 2;
    private static final int PRODUCT_ID = 3;
    private static final int FAVORITES_DIR = 4;
    private static final int FAVORITE_ID = 5;
    private static final int CATEGORY_PRODUCTS_DIR = 6;
    private static final int CATEGORY_PRODUCT_ID = 7;
    private static final int FAVORITE_PRODUCTS_DIR = 8;
    private static final int FAVORITE_PRODUCT_ID = 9;


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, Paths.CATEGORIES, CATEGORIES_DIR);
        sURIMatcher.addURI(AUTHORITY, Paths.CATEGORIES + "/#", CATEGORY_ID);
        sURIMatcher.addURI(AUTHORITY, Paths.PRODUCTS, PRODUCTS_DIR);
        sURIMatcher.addURI(AUTHORITY, Paths.PRODUCTS + "/#", PRODUCT_ID);
        sURIMatcher.addURI(AUTHORITY, Paths.FAVORITES, FAVORITES_DIR);
        sURIMatcher.addURI(AUTHORITY, Paths.FAVORITES + "/#", FAVORITE_ID);
        sURIMatcher.addURI(AUTHORITY, Paths.CATEGORY_PRODUCTS, CATEGORY_PRODUCTS_DIR);
        sURIMatcher.addURI(AUTHORITY, Paths.CATEGORY_PRODUCTS + "/#", CATEGORY_PRODUCT_ID);
        sURIMatcher.addURI(AUTHORITY, Paths.FAVORITE_PRODUCTS, FAVORITE_PRODUCTS_DIR);
        sURIMatcher.addURI(AUTHORITY, Paths.FAVORITE_PRODUCTS + "/#", FAVORITE_PRODUCT_ID);
    }

    // database
    private SYNDatabaseHelper database;



    @Override
    public boolean onCreate() {
        database = SYNDatabaseHelper.getInstance(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (sURIMatcher.match(uri)) {
            case CATEGORY_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Paths.CATEGORIES;
            case CATEGORIES_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Paths.CATEGORIES;

            case PRODUCT_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Paths.PRODUCTS;
            case PRODUCTS_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Paths.PRODUCTS;

            case FAVORITE_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Paths.FAVORITES;
            case FAVORITES_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Paths.FAVORITES;


            case CATEGORY_PRODUCT_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Paths.CATEGORY_PRODUCTS;
            case CATEGORY_PRODUCTS_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Paths.CATEGORY_PRODUCTS;

            case FAVORITE_PRODUCT_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Paths.FAVORITE_PRODUCTS;
            case FAVORITE_PRODUCTS_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Paths.FAVORITE_PRODUCTS;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String groupBy = null;
        String having = null;

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case CATEGORY_ID:
                queryBuilder.appendWhere(CategoryTable._ID + "="
                        + uri.getLastPathSegment());
            case CATEGORIES_DIR:
                queryBuilder.setTables(CategoryTable.TABLE_NAME);
                break;


            case PRODUCT_ID:
                queryBuilder.appendWhere(ProductTable._ID + "="
                        + uri.getLastPathSegment());
            case PRODUCTS_DIR:
                queryBuilder.setTables(ProductTable.TABLE_NAME);
                break;

            case FAVORITE_ID:
                queryBuilder.appendWhere(FavoriteTable._ID + "="
                        + uri.getLastPathSegment());
            case FAVORITES_DIR:
                queryBuilder.setTables(FavoriteTable.TABLE_NAME);
                break;


            case CATEGORY_PRODUCT_ID:

                queryBuilder.setTables(CategoryProductTable.TABLE_NAME +
                        " INNER JOIN " + CategoryTable.TABLE_NAME + " ON " +
                        CategoryProductTable.CATEGORY_ID + " = " + CategoryTable.CATEGORY_ID +
                        " INNER JOIN " + ProductTable.TABLE_NAME + " ON " +
                        CategoryProductTable.PRODUCT_ID + " = " + ProductTable.PRODUCT_ID
                );

                queryBuilder.appendWhere(CategoryProductTable.CATEGORY_ID + "=" + uri.getLastPathSegment());
                break;

            case CATEGORY_PRODUCTS_DIR:
                queryBuilder.setTables(CategoryProductTable.TABLE_NAME +
                                " INNER JOIN " + CategoryTable.TABLE_NAME + " ON " +
                                CategoryProductTable.CATEGORY_ID + " = " + CategoryTable.CATEGORY_ID +
                                " INNER JOIN " + ProductTable.TABLE_NAME + " ON " +
                                CategoryProductTable.PRODUCT_ID + " = " + ProductTable.PRODUCT_ID +
                                " LEFT JOIN " + FavoriteTable.TABLE_NAME + " ON " +
                                CategoryProductTable.PRODUCT_ID + " = " + FavoriteTable.PRODUCT_ID

                );
                break;

            case FAVORITE_PRODUCT_ID:

                queryBuilder.setTables(FavoriteTable.TABLE_NAME +
                                " INNER JOIN " + ProductTable.TABLE_NAME + " ON " +
                                FavoriteTable.PRODUCT_ID + " = " + ProductTable.PRODUCT_ID
                );

                queryBuilder.appendWhere(FavoriteTable.PRODUCT_ID + "=" + uri.getLastPathSegment());
                break;

            case FAVORITE_PRODUCTS_DIR:
                queryBuilder.setTables(FavoriteTable.TABLE_NAME +
                                " INNER JOIN " + ProductTable.TABLE_NAME + " ON " +
                                FavoriteTable.PRODUCT_ID + " = " + ProductTable.PRODUCT_ID
                );
                break;



            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        final SQLiteDatabase dbConnection = database.getWritableDatabase();
        int deleteCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (uriType) {
                case CATEGORIES_DIR :
                    deleteCount = dbConnection.delete(CategoryTable.TABLE_NAME,
                            selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_ID :
                    deleteCount = dbConnection.delete(CategoryTable.TABLE_NAME,
                            CategoryTable._ID + "=?", new String[]{uri
                                    .getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;

                case PRODUCTS_DIR :
                    deleteCount = dbConnection.delete(ProductTable.TABLE_NAME,
                            selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case PRODUCT_ID :
                    deleteCount = dbConnection.delete(ProductTable.TABLE_NAME,
                            ProductTable._ID + "=?", new String[]{uri
                                    .getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;

                case FAVORITES_DIR :
                    deleteCount = dbConnection.delete(FavoriteTable.TABLE_NAME,
                            selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case FAVORITE_ID :
                    deleteCount = dbConnection.delete(FavoriteTable.TABLE_NAME,
                            FavoriteTable._ID + "=?", new String[]{uri
                                    .getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;


                case CATEGORY_PRODUCTS_DIR :
                    deleteCount = dbConnection.delete(CategoryProductTable.TABLE_NAME,
                            selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_PRODUCT_ID :
                    deleteCount = dbConnection.delete(CategoryProductTable.TABLE_NAME,
                            CategoryProductTable._ID + "=?", new String[]{uri
                                    .getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;

                case FAVORITE_PRODUCTS_DIR :
                    deleteCount = dbConnection.delete(FavoriteTable.TABLE_NAME,
                            selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case FAVORITE_PRODUCT_ID :
                    deleteCount = dbConnection.delete(FavoriteTable.TABLE_NAME,
                            FavoriteTable._ID + "=?", new String[]{uri
                                    .getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;


                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }


        // This is bad if you're batching deletions
//        if (deleteCount > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        final SQLiteDatabase dbConnection = database.getWritableDatabase();
        int updateCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (uriType) {

                case CATEGORIES_DIR :
                    updateCount = dbConnection.update(CategoryTable.TABLE_NAME,
                            values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_ID :
                    final Long categoryId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(
                            CategoryTable.TABLE_NAME,
                            values,
                            CategoryTable._ID
                                    + "="
                                    + categoryId
                                    + (TextUtils.isEmpty(selection)
                                    ? ""
                                    : " AND (" + selection + ")"),
                            selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case PRODUCTS_DIR :
                    updateCount = dbConnection.update(ProductTable.TABLE_NAME,
                            values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case PRODUCT_ID :
                    final Long productId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(
                            ProductTable.TABLE_NAME,
                            values,
                            ProductTable._ID
                                    + "="
                                    + productId
                                    + (TextUtils.isEmpty(selection)
                                    ? ""
                                    : " AND (" + selection + ")"),
                            selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case FAVORITES_DIR :
                    updateCount = dbConnection.update(FavoriteTable.TABLE_NAME,
                            values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case FAVORITE_ID :
                    final Long favoriteId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(
                            FavoriteTable.TABLE_NAME,
                            values,
                            FavoriteTable._ID
                                    + "="
                                    + favoriteId
                                    + (TextUtils.isEmpty(selection)
                                    ? ""
                                    : " AND (" + selection + ")"),
                            selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;


                case CATEGORY_PRODUCTS_DIR :
                    updateCount = dbConnection.update(CategoryProductTable.TABLE_NAME,
                            values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_PRODUCT_ID :
                    final Long categoryProductId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(
                            CategoryProductTable.TABLE_NAME,
                            values,
                            CategoryProductTable._ID
                                    + "="
                                    + categoryProductId
                                    + (TextUtils.isEmpty(selection)
                                    ? ""
                                    : " AND (" + selection + ")"),
                            selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;


                case FAVORITE_PRODUCTS_DIR :
                    updateCount = dbConnection.update(FavoriteTable.TABLE_NAME,
                            values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case FAVORITE_PRODUCT_ID :
                    final Long favoriteProductId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(
                            FavoriteTable.TABLE_NAME,
                            values,
                            FavoriteTable._ID
                                    + "="
                                    + favoriteProductId
                                    + (TextUtils.isEmpty(selection)
                                    ? ""
                                    : " AND (" + selection + ")"),
                            selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;



                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        // This is bad if you're batching updates
//        if (updateCount > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }

        return updateCount;
    }


}