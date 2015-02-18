package com.frankandoak.synchronization.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.models.RemoteCategoryProduct;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.retrofit.GetCategoryProductsClient;
import com.frankandoak.synchronization.retrofit.GetCategoryProductsResponse;
import com.frankandoak.synchronization.retrofit.FAOApiClientManager;
import com.frankandoak.synchronization.synchronizers.CategoryProductSynchronizer;
import com.frankandoak.synchronization.synchronizers.ProductSynchronizer;
import com.frankandoak.synchronization.synchronizers.preprocessors.CategoryProductPreProcessor;
import com.frankandoak.synchronization.utilities.SyncUtil;

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
            GetCategoryProductsResponse r = null;

            try {
                r = client.downloadCategoryProducts(categoryId,1);
                List<RemoteProduct> remoteProducts = r.response;

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
                        null
                );
                localProducts.close();

                // Sync category products
                List<RemoteCategoryProduct> remoteCategoryProducts = new ArrayList<>(remoteProducts.size());
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
                        new CategoryProductPreProcessor(categoryId, remoteProducts)
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
