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
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.database.SYNDatabaseHelper;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class ProductSynchronizer extends BaseSynchronizer<RemoteProduct>{

    private static final String TAG = ProductSynchronizer.class.getSimpleName();

    public ProductSynchronizer(Context context) {

        super(context);
    }

    @Override
    protected void performSynchronizationOperations(Context context, List<RemoteProduct> inserts, List<RemoteProduct> updates, List<Long> deletions) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        if( inserts.size() > 0 )
        {
            ContentValues[] val = new ContentValues[inserts.size()];
            int i = 0;
            for (RemoteProduct w : inserts) {

                val[i] = this.getContentValuesForRemoteEntity(w);
                ++i;
            }

            doBulkInsertOptimised(val);
        }

        for (RemoteProduct w : updates) {
            ContentValues values = this.getContentValuesForRemoteEntity(w);

            List<String> identifierKeys = w.getIdentifierKeys();
            List<String> identifierValues = w.getIdentifierValues();
            String selection = "";
            String[] selectionValues = new String[identifierKeys.size()];

            for( int i=0; i < identifierKeys.size()-1; ++i )
            {
                selection += identifierKeys.get(i) + "=?,";
                selectionValues[i] = identifierValues.get(i);
            }

            selection += identifierKeys.get(identifierKeys.size()-1) + "=?";
            selectionValues[identifierKeys.size()-1] = identifierValues.get(identifierKeys.size()-1);

            ContentProviderOperation op = ContentProviderOperation.newUpdate(SYNContentProvider.Uris.CATEGORIES_URI).withSelection(selection, selectionValues)
                    .withValues(values).build();
            operations.add(op);
        }

        for (Long id : deletions) {
            ContentProviderOperation op = ContentProviderOperation.newDelete(SYNContentProvider.Uris.CATEGORIES_URI).withSelection(CategoryTable.ID + " = ?", new String[] { String.valueOf(id) }).build();
            operations.add(op);
        }

        try {
            if( inserts.size() > 0 || operations.size() > 0 )
            {
                context.getContentResolver().applyBatch(SYNContentProvider.AUTHORITY, operations);
                context.getContentResolver().notifyChange(SYNContentProvider.Uris.CATEGORIES_URI, null);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private int doBulkInsertOptimised(ContentValues values[]) {

        Context context = SYNApplication.getContext();
        SYNDatabaseHelper helper = SYNDatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, ProductTable.TABLE_NAME);


        db.beginTransaction();
        int numInserted = 0;
        try {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                inserter.prepareForInsert();

                long productId = ((Number) values[i].get(ProductTable.PRODUCT_ID)).longValue();
                inserter.bind(inserter.getColumnIndex(ProductTable.PRODUCT_ID), productId);

                String sku = (String)(values[i].get(ProductTable.SKU));
                inserter.bind(inserter.getColumnIndex(ProductTable.SKU), sku);

                String name = (String)(values[i].get(ProductTable.NAME));
                inserter.bind(inserter.getColumnIndex(ProductTable.NAME), name);

                String imageUrl = (String)(values[i].get(ProductTable.IMAGE_URL));
                inserter.bind(inserter.getColumnIndex(ProductTable.IMAGE_URL), imageUrl);

                int favoriteCount = ((Number) values[i].get(ProductTable.FAVORITE_COUNT)).intValue();
                inserter.bind(inserter.getColumnIndex(ProductTable.FAVORITE_COUNT), favoriteCount);


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

    @Override
    protected boolean isRemoteEntityNewerThanLocal(RemoteProduct remote, Cursor c) {

        try {
            boolean isSameId = remote.getProductId() == c.getInt(c.getColumnIndex(ProductTable.PRODUCT_ID));
            boolean isSameSku = remote.getSku().equals(c.getString(c.getColumnIndex(ProductTable.SKU)));
            boolean isSameName = remote.getName().equals(c.getString(c.getColumnIndex(ProductTable.NAME)));
            boolean isSameImageUrl = remote.getImageUrl().equals(c.getString(c.getColumnIndex(ProductTable.IMAGE_URL)));
            boolean isSameFavoriteCount = remote.getFavoriteCount() == c.getInt(c.getColumnIndex(ProductTable.FAVORITE_COUNT));

            return !(isSameId && isSameSku && isSameName && isSameImageUrl && isSameFavoriteCount);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    protected ContentValues getContentValuesForRemoteEntity(RemoteProduct remoteProduct) {

        ContentValues values = new ContentValues();
        remoteProduct.populateContentValues(values);

        return values;
    }
}