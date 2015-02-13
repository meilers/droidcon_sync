package com.frankandoak.synchronization.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.retrofit.DownloadCategoryProductsClient;
import com.frankandoak.synchronization.retrofit.DownloadCategoryProductsResponse;
import com.frankandoak.synchronization.retrofit.FAOApiClientManager;
import com.frankandoak.synchronization.synchronizers.ProductSynchronizer;
import com.frankandoak.synchronization.utilities.SyncUtil;

import java.util.ArrayList;
import java.util.List;


public class SyncCategoryProductsService extends IntentService {

	private static final String TAG = SyncCategoryProductsService.class.getSimpleName();

    public static final class EXTRAS {
        public static final String IN_CATEGORY_ID = "inCategoryId";
    }

	public SyncCategoryProductsService() {
		super("SyncCategoryProductsService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SYNC))
		{
            Long categoryId = intent.getLongExtra(EXTRAS.IN_CATEGORY_ID, -1L);
            Context context = SYNApplication.getContext();

            DownloadCategoryProductsClient client = FAOApiClientManager.getInstance().getClient(context, DownloadCategoryProductsClient.class);
            DownloadCategoryProductsResponse r = null;

            try {
                r = client.downloadCategoryProducts(categoryId,1);
                List<RemoteProduct> categoryProducts = r.response;

                // Sync products
                Cursor localProductCursor = context.getContentResolver().query(SYNContentProvider.Uris.PRODUCTS_URI, ProductTable.ALL_COLUMNS, null, null, null);
                localProductCursor.moveToFirst();

                List<Integer> productIdentifierColumnIndices = new ArrayList<>();
                List<String> productIdentifierKeys = RemoteProduct.getIdentifierKeys();

                for( String key : productIdentifierKeys )
                {
                    productIdentifierColumnIndices.add(localProductCursor.getColumnIndex(key));
                }


                SyncUtil.synchronizeProducts(context, categoryProducts, localProductCursor, productIdentifierColumnIndices, new ProductSynchronizer(context), null);
                localProductCursor.close();


                // Sync category products
//                Cursor localCategoryProductsCursor = context.getContentResolver().query(Uri.withAppendedPath(SYNContentProvider.Uris.CATEGORY_PRODUCTS_URI, categoryId + ""), CategoryProductTable.ALL_COLUMNS, CategoryProductTable.CATEGORY_ID + "=?", new String[]{categoryId + ""}, null);
//                localCategoryProductsCursor.moveToFirst();
//                SyncUtil.synchronizeCategoryProducts(categoryProducts, localCategoryProductsCursor, localCategoryProductsCursor.getColumnIndex(CategoryProductTable.PRODUCT_ID), localCategoryProductsCursor.getColumnIndex(CategoryProductTable.CATEGORY_ID), new CategoryProductSynchronizer(context, mCategoryId), new CategoryProductPreProcessor(mCategoryId));
//                localCategoryProductsCursor.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }


        }
	}

}
