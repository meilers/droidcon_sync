package com.frankandoak.synchronization.retrofit;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Michael on 2014-05-09.
 */
public interface DownloadCategoryProductsClient {
    @GET("/products")
    DownloadCategoryProductsResponse downloadCategoryProducts(@Query("store_id") Long storeId, @Query("compact") Integer compact);
}
