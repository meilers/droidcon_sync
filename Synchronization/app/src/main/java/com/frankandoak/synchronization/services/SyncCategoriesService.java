package com.frankandoak.synchronization.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.retrofit.FAOApiClientManager;
import com.frankandoak.synchronization.retrofit.clients.GetCategoriesClient;
import com.frankandoak.synchronization.retrofit.responses.GetCategoriesResponse;
import com.frankandoak.synchronization.sync.synchronizers.CategorySynchronizer;
import com.frankandoak.synchronization.sync.synchronizers.preprocessors.CategoryPreProcessor;
import com.frankandoak.synchronization.utils.SyncUtil;

import java.util.List;


public class SyncCategoriesService extends IntentService {

	private static final String TAG = SyncCategoriesService.class.getSimpleName();

	public SyncCategoriesService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SYNC))
		{
            Context context = SYNApplication.getContext();

            GetCategoriesClient client = FAOApiClientManager.getInstance().getClient(context, GetCategoriesClient.class);
            GetCategoriesResponse r;

            try {
                r = client.getCategories();
                List<RemoteCategory> remoteCategories = r.getResponse().getStores();

                // Sync products
                Cursor localCategories = context.getContentResolver().query(
                        SYNContentProvider.URIS.CATEGORIES_URI,
                        CategoryTable.ALL_COLUMNS,
                        null,
                        null,
                        null
                );
                SyncUtil.synchronize(
                        context,
                        remoteCategories,
                        localCategories,
                        RemoteCategory.IDENTIFIERS,
                        new CategorySynchronizer(context),
                        new CategoryPreProcessor()
                );
                localCategories.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }


        }
	}

}
