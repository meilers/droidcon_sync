package com.frankandoak.synchronization.sync.synchronizers;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.FavoriteTable;
import com.frankandoak.synchronization.database.SYNDatabaseHelper;
import com.frankandoak.synchronization.models.RemoteFavorite;
import com.frankandoak.synchronization.models.RemoteObject;
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
public class FavoriteSynchronizer extends BaseSynchronizer<RemoteFavorite> {

    private static final String TAG = FavoriteSynchronizer.class.getSimpleName();

    public FavoriteSynchronizer(Context context) {

        super(context);
    }

    @Override
    protected void performSynchronizationOperations(Context context, List<RemoteFavorite> inserts, List<RemoteFavorite> updates, List<Long> deletions) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        if( inserts.size() > 0 ) {
            doBulkInsertOptimised(inserts);
        }

        for (RemoteFavorite w : updates) {
            SyncUtil.Selection selection = SyncUtil.buildSelection(w.getIdentifiers());
            ContentProviderOperation op = ContentProviderOperation
                    .newUpdate(SYNContentProvider.URIS.FAVORITES_URI)
                    .withSelection(selection.getQuery(), selection.getValues())
                    .withValues(getContentValuesForRemoteEntity(w)).build();

            operations.add(op);
        }

        for (Long id : deletions) {
            ContentProviderOperation op = ContentProviderOperation
                    .newDelete(SYNContentProvider.URIS.FAVORITES_URI)
                    .withSelection(FavoriteTable._ID + " = ? AND (" + FavoriteTable.SYNC_STATUS + "=? OR " + FavoriteTable.IS_DELETED + "=?)",
                            new String[]{String.valueOf(id), RemoteObject.SyncStatus.NO_CHANGES.ordinal() + "", "1"})
                    .build();

            operations.add(op);
        }

        try {
            if( inserts.size() > 0 || operations.size() > 0 ) {
                context.getContentResolver().applyBatch(SYNContentProvider.AUTHORITY, operations);
                context.getContentResolver().notifyChange(SYNContentProvider.URIS.FAVORITES_URI, null);
                context.getContentResolver().notifyChange(SYNContentProvider.URIS.FAVORITE_PRODUCTS_URI, null);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    protected boolean isRemoteEntityNewerThanLocal(RemoteFavorite remote, Cursor c) {
        try {
            Calendar remoteUpdatedTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            Calendar localUpdatedTime = DateUtil.convertToDate(c.getString(c.getColumnIndex(FavoriteTable.UPDATED_AT)));

            if( remoteUpdatedTime == null || localUpdatedTime == null )
                return true;

            RemoteObject.SyncStatus localSyncStatus = RemoteObject.SyncStatus.getSyncStatusFromCode(c.getInt(c.getColumnIndex(FavoriteTable.SYNC_STATUS)));

            return (remoteUpdatedTime.getTimeInMillis() > localUpdatedTime.getTimeInMillis()) && !localSyncStatus.equals(RemoteObject.SyncStatus.QUEUED_TO_SYNC);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    public static List<Long> doBulkInsertOptimised(List<RemoteFavorite> inserts) {

        Context context = SYNApplication.getContext();
        SYNDatabaseHelper helper = SYNDatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, FavoriteTable.TABLE_NAME);
        List<Long> insertedIds = new ArrayList<>();
        RemoteFavorite favorite;

        db.beginTransaction();

        try {
            int len = inserts.size();
            for (int i = 0; i < len; i++) {

                favorite = inserts.get(i);

                inserter.prepareForInsert();

                if( favorite.getCreatedAt() != null )
                    inserter.bind(inserter.getColumnIndex(FavoriteTable.CREATED_AT), favorite.getCreatedAt());

                if( favorite.getCreatedAt() != null )
                    inserter.bind(inserter.getColumnIndex(FavoriteTable.UPDATED_AT), favorite.getUpdatedAt());

                if( favorite.getSyncStatus() != null )
                    inserter.bind(inserter.getColumnIndex(FavoriteTable.SYNC_STATUS), favorite.getSyncStatus().ordinal());

                if( favorite.getIsDeleted() != null )
                    inserter.bind(inserter.getColumnIndex(FavoriteTable.IS_DELETED), favorite.getIsDeleted());

                if( favorite.getProductId() != null )
                    inserter.bind(inserter.getColumnIndex(FavoriteTable.PRODUCT_ID), favorite.getProductId());

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