package com.frankandoak.synchronization.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.models.RemoteCategoryProduct;
import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.retrofit.clients.GetCategoryProductsClient;
import com.frankandoak.synchronization.retrofit.responses.GetCategoryProductsResponse;
import com.frankandoak.synchronization.retrofit.FAOApiClientManager;
import com.frankandoak.synchronization.sync.synchronizers.CategoryProductSynchronizer;
import com.frankandoak.synchronization.sync.synchronizers.ProductSynchronizer;
import com.frankandoak.synchronization.sync.synchronizers.preprocessors.CategoryProductPreProcessor;
import com.frankandoak.synchronization.sync.synchronizers.preprocessors.ProductPreProcessor;
import com.frankandoak.synchronization.utils.SyncUtil;

import java.util.ArrayList;
import java.util.List;


public class SyncCategoryProductsService extends IntentService {

	private static final String TAG = SyncCategoryProductsService.class.getSimpleName();

    public static final class EXTRAS {
        public static final String IN_CATEGORY_ID = "inCategoryId";
    }

	public SyncCategoryProductsService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SYNC))
		{
            Long categoryId = intent.getLongExtra(EXTRAS.IN_CATEGORY_ID, -1L);
            Context context = SYNApplication.getContext();

            GetCategoryProductsClient client = FAOApiClientManager.getInstance().getClient(context, GetCategoryProductsClient.class);
            GetCategoryProductsResponse r;

            try {
                r = client.getCategoryProducts(categoryId, 1);
                List<RemoteProduct> remoteProducts = r.getResponse();

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

                // Sync category products
                List<RemoteCategoryProduct> remoteCategoryProducts = new ArrayList<>(remoteProducts.size());
                RemoteCategoryProduct categoryProduct;
                RemoteProduct product;

                for( int i = 0; i < remoteProducts.size(); ++i ) {
                    product = remoteProducts.get(i);
                    categoryProduct = new RemoteCategoryProduct(
                            null,
                            product.getCreatedAt(),
                            product.getUpdatedAt(),
                            RemoteObject.SyncStatus.NO_CHANGES,
                            false,
                            categoryId,
                            product.getProductId(),
                            i
                    );
                    remoteCategoryProducts.add(categoryProduct);
                }

                Cursor localCategoryProducts = context.getContentResolver().query(
                        SYNContentProvider.URIS.CATEGORY_PRODUCTS_URI,
                        CategoryProductTable.ALL_COLUMNS,
                        CategoryProductTable.CATEGORY_ID + "=?",
                        new String[]{categoryId + ""},
                        null
                );
                SyncUtil.synchronize(
                        context,
                        remoteCategoryProducts,
                        localCategoryProducts,
                        RemoteCategoryProduct.IDENTIFIERS,
                        new CategoryProductSynchronizer(context),
                        new CategoryProductPreProcessor()
                );
                localCategoryProducts.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }


        }
	}

}
