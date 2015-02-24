package com.frankandoak.synchronization.sync.syncAdapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.frankandoak.synchronization.database.FavoriteTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.models.RemoteFavorite;
import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.retrofit.FAOApiClientManager;
import com.frankandoak.synchronization.retrofit.clients.AddFavoriteClient;
import com.frankandoak.synchronization.retrofit.clients.DeleteFavoriteClient;
import com.frankandoak.synchronization.utils.ArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mj_eilers on 15-02-23.
 */
public class FavoriteSyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;


    public FavoriteSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    public FavoriteSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        try {

            HashMap<String,RemoteFavorite> productSkusToAdd = new HashMap<>();
            HashMap<String,RemoteFavorite> productSkusToDelete = new HashMap<>();
            RemoteProduct product;
            RemoteFavorite favorite;

            String[] columns = ArrayUtil.concatenate(FavoriteTable.ALL_COLUMNS, ProductTable.ALL_COLUMNS);

            final Cursor c = mContentResolver.query(
                    SYNContentProvider.URIS.FAVORITE_PRODUCTS_URI,
                    columns,
                    FavoriteTable.SYNC_STATUS + "=?",
                    new String[] {RemoteObject.SyncStatus.QUEUED_TO_SYNC.ordinal()+""},
                    null
            );

            for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext() ) {

                product = new RemoteProduct(c);
                favorite = new RemoteFavorite(c);

                if( favorite.getIsDeleted() ) {
                    productSkusToDelete.put(product.getSku(), favorite);
                }
                else if( favorite.getSyncStatus().equals(RemoteObject.SyncStatus.QUEUED_TO_SYNC) ) {
                    productSkusToAdd.put(product.getSku(), favorite);
                }
            }

            c.close();

            if( !productSkusToAdd.isEmpty() ) {
                ContentValues values = new ContentValues();
                AddFavoriteClient addFavoriteClient = FAOApiClientManager.getInstance().getClient(getContext(), AddFavoriteClient.class);
                Iterator it = productSkusToAdd.entrySet().iterator();
                String sku;

                while (it.hasNext()) {
                    Map.Entry<String,RemoteFavorite> pair = (Map.Entry)it.next();

                    sku = pair.getKey();
                    favorite = pair.getValue();

                    Log.d("Adding Favorite SKU to remote:", sku);
                    addFavoriteClient.addFavorite(sku);

                    favorite.setSyncStatus(RemoteObject.SyncStatus.NO_CHANGES);
                    favorite.setIsDeleted(false);
                    favorite.populateContentValues(values);
                    mContentResolver.update(SYNContentProvider.URIS.FAVORITES_URI, values, FavoriteTable.PRODUCT_ID + "=?", new String[]{favorite.getProductId() + ""});
                }

            }

            if( !productSkusToDelete.isEmpty() ) {
                DeleteFavoriteClient deleteFavoriteClient = FAOApiClientManager.getInstance().getClient(getContext(), DeleteFavoriteClient.class);
                Iterator it = productSkusToDelete.entrySet().iterator();
                String sku;

                while (it.hasNext()) {
                    Map.Entry<String,RemoteFavorite> pair = (Map.Entry)it.next();

                    sku = pair.getKey();
                    favorite = pair.getValue();

                    Log.d("Deleting Favorite SKU from remote:", sku);
                    deleteFavoriteClient.deleteFavorite(sku);

                    mContentResolver.delete(SYNContentProvider.URIS.FAVORITES_URI, FavoriteTable._ID + "=?", new String[]{favorite.getId() + ""});
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
