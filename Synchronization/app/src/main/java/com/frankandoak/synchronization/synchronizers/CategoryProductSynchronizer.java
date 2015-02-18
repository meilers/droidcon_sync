package com.frankandoak.synchronization.synchronizers;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.BaseTable;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.database.SYNDatabaseHelper;
import com.frankandoak.synchronization.models.RemoteCategoryProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.utilities.DateUtil;
import com.frankandoak.synchronization.utilities.SyncUtil;
import com.frankandoak.synchronization.utilities.SyncUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Michael on 2014-03-17.
 */
public class CategoryProductSynchronizer extends BaseSynchronizer<RemoteCategoryProduct>{

    private static final String TAG = CategoryProductSynchronizer.class.getSimpleName();

    public CategoryProductSynchronizer(Context context) {

        super(context);
    }

    @Override
    protected void performSynchronizationOperations(Context context, List<RemoteCategoryProduct> inserts, List<RemoteCategoryProduct> updates, List<Long> deletions) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        if( inserts.size() > 0 )
        {
            doBulkInsertOptimised(inserts);
        }

        for (RemoteCategoryProduct w : updates) {
            SyncUtil.Selection selection = SyncUtil.buildSelection(w.getIdentifiers());
            ContentProviderOperation op = ContentProviderOperation
                    .newUpdate(SYNContentProvider.URIS.CATEGORY_PRODUCTS_URI)
                    .withSelection(selection.getQuery(), selection.getValues())
                    .withValues(getContentValuesForRemoteEntity(w)).build();

            operations.add(op);
        }

        for (Long id : deletions) {
            ContentProviderOperation op = ContentProviderOperation
                    .newDelete(SYNContentProvider.URIS.CATEGORY_PRODUCTS_URI)
                    .withSelection(CategoryTable._ID + " = ?", new String[]{String.valueOf(id)})
                    .build();

            operations.add(op);
        }

        try {
            if( inserts.size() > 0 || operations.size() > 0 )
            {
                context.getContentResolver().applyBatch(SYNContentProvider.AUTHORITY, operations);
                context.getContentResolver().notifyChange(SYNContentProvider.URIS.CATEGORY_PRODUCTS_URI, null);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private int doBulkInsertOptimised(List<RemoteCategoryProduct> inserts) {

        Context context = SYNApplication.getContext();
        SYNDatabaseHelper helper = SYNDatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, ProductTable.TABLE_NAME);
        RemoteCategoryProduct categoryProduct;

        db.beginTransaction();
        int numInserted = 0;
        try {
            int len = inserts.size();
            for (int i = 0; i < len; i++) {

                categoryProduct = inserts.get(i);

                inserter.prepareForInsert();
                inserter.bind(inserter.getColumnIndex(CategoryProductTable.CREATED_AT), categoryProduct.getCreatedAt());
                inserter.bind(inserter.getColumnIndex(CategoryProductTable.UPDATED_AT), categoryProduct.getUpdatedAt());
                inserter.bind(inserter.getColumnIndex(CategoryProductTable.SYNC_STATUS), categoryProduct.getSyncStatus() != null ? categoryProduct.getSyncStatus().ordinal() : null);
                inserter.bind(inserter.getColumnIndex(CategoryProductTable.IS_DELETED), categoryProduct.getIsDeleted());
                inserter.bind(inserter.getColumnIndex(CategoryProductTable.CATEGORY_ID), categoryProduct.getCategoryId());
                inserter.bind(inserter.getColumnIndex(CategoryProductTable.PRODUCT_ID), categoryProduct.getProductId());
                inserter.execute();
            }
            numInserted = len;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            inserter.close();
        }
        return numInserted;
    }
}