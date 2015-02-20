package com.frankandoak.synchronization.synchronizers;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database. CategoryTable;
import com.frankandoak.synchronization.database.SYNDatabaseHelper;
import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.utils.DateUtil;
import com.frankandoak.synchronization.utils.SyncUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Michael on 2014-03-17.
 */
public class CategorySynchronizer extends BaseSynchronizer<RemoteCategory>{

    private static final String TAG = CategorySynchronizer.class.getSimpleName();

    public CategorySynchronizer(Context context) {

        super(context);
    }

    @Override
    protected void performSynchronizationOperations(Context context, List<RemoteCategory> inserts, List<RemoteCategory> updates, List<Long> deletions) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        if( inserts.size() > 0 ) {
            doBulkInsertOptimised(inserts);
        }

        for (RemoteCategory w : updates) {
            SyncUtil.Selection selection = SyncUtil.buildSelection(w.getIdentifiers());
            ContentProviderOperation op = ContentProviderOperation
                    .newUpdate(SYNContentProvider.URIS.CATEGORIES_URI)
                    .withSelection(selection.getQuery(), selection.getValues())
                    .withValues(getContentValuesForRemoteEntity(w)).build();

            operations.add(op);
        }

        for (Long id : deletions) {
            ContentProviderOperation op = ContentProviderOperation
                    .newDelete(SYNContentProvider.URIS.CATEGORIES_URI)
                    .withSelection(CategoryTable._ID + " = ?", new String[]{String.valueOf(id)})
                    .build();

            operations.add(op);
        }

        try {
            if( inserts.size() > 0 || operations.size() > 0 )
            {
                context.getContentResolver().applyBatch(SYNContentProvider.AUTHORITY, operations);
                context.getContentResolver().notifyChange(SYNContentProvider.URIS.CATEGORIES_URI, null);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    protected boolean isRemoteEntityNewerThanLocal(RemoteCategory remote, Cursor c) {
        try {
            Calendar remoteUpdatedTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            Calendar localUpdatedTime = DateUtil.convertToDate(c.getString(c.getColumnIndex(CategoryTable.UPDATED_AT)));

            if( remoteUpdatedTime == null || localUpdatedTime == null )
                return true;

            return remoteUpdatedTime.getTimeInMillis() > localUpdatedTime.getTimeInMillis();

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static List<Long> doBulkInsertOptimised(List<RemoteCategory> inserts) {

        Context context = SYNApplication.getContext();
        SYNDatabaseHelper helper = SYNDatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, CategoryTable.TABLE_NAME);
        List<Long> insertedIds = new ArrayList<>();
        RemoteCategory category;

        db.beginTransaction();

        try {
            int len = inserts.size();
            for (int i = 0; i < len; i++) {

                category = inserts.get(i);

                inserter.prepareForInsert();

                if( category.getCreatedAt() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.CREATED_AT), category.getCreatedAt());

                if( category.getCreatedAt() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.UPDATED_AT), category.getUpdatedAt());

                if( category.getSyncStatus() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.SYNC_STATUS), category.getSyncStatus().ordinal());

                if( category.getIsDeleted() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.IS_DELETED), category.getIsDeleted());

                if( category.getCategoryId() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.CATEGORY_ID), category.getCategoryId());

                if( category.getName() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.NAME), category.getName());

                if( category.getImageUrl() != null )
                    inserter.bind(inserter.getColumnIndex( CategoryTable.IMAGE_URL), category.getImageUrl());

                insertedIds.add(inserter.execute());
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            inserter.close();
        }
        return insertedIds;
    }
}