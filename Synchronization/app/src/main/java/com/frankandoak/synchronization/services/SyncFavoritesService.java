package com.frankandoak.synchronization.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.FavoriteTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.models.RemoteFavorite;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.retrofit.FAOApiClientManager;
import com.frankandoak.synchronization.retrofit.clients.GetFavoritesClient;
import com.frankandoak.synchronization.retrofit.responses.GetFavoritesResponse;
import com.frankandoak.synchronization.sync.synchronizers.FavoriteSynchronizer;
import com.frankandoak.synchronization.sync.synchronizers.ProductSynchronizer;
import com.frankandoak.synchronization.sync.synchronizers.preprocessors.FavoritePreProcessor;
import com.frankandoak.synchronization.sync.synchronizers.preprocessors.ProductPreProcessor;
import com.frankandoak.synchronization.utils.SyncUtil;

import java.util.ArrayList;
import java.util.List;


public class SyncFavoritesService extends IntentService {

	private static final String TAG = SyncFavoritesService.class.getSimpleName();

	public SyncFavoritesService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SYNC))
		{
            Context context = SYNApplication.getContext();

            GetFavoritesClient client = FAOApiClientManager.getInstance().getClient(context, GetFavoritesClient.class);
            GetFavoritesResponse r;

            try {
                r = client.getFavorites();
                List<RemoteProduct> remoteProducts = r.getResponse().getFavorites();

                // Sync products
                Cursor localProducts = context.getContentResolver().query(
                        SYNContentProvider.URIS.PRODUCTS_URI,
                        ProductTable.ALL_COLUMNS,
                        null,
                        null,
                        null
                );
                SyncUtil.synchronize(
                        context,
                        remoteProducts,
                        localProducts,
                        RemoteProduct.IDENTIFIERS,
                        new ProductSynchronizer(context, false),
                        new ProductPreProcessor()
                );
                localProducts.close();

                // Sync favorites
                List<RemoteFavorite> remoteFavorites = new ArrayList<>(remoteProducts.size());
                RemoteProduct product;
                RemoteFavorite favorite;

                for( int i = 0; i < remoteProducts.size(); ++i ) {
                    product = remoteProducts.get(i);
                    favorite = new RemoteFavorite(
                            null,
                            product.getCreatedAt(),
                            product.getUpdatedAt(),
                            RemoteObject.SyncStatus.NO_CHANGES,
                            false,
                            product.getProductId()
                    );
                    remoteFavorites.add(favorite);
                }

                Cursor localFavorites = context.getContentResolver().query(
                        SYNContentProvider.URIS.FAVORITES_URI,
                        FavoriteTable.ALL_COLUMNS,
                        null,
                        null,
                        null
                );
                SyncUtil.synchronize(
                        context,
                        remoteFavorites,
                        localFavorites,
                        RemoteFavorite.IDENTIFIERS,
                        new FavoriteSynchronizer(context),
                        new FavoritePreProcessor()
                );
                localFavorites.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }


        }
	}

}
