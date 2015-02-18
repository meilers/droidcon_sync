package com.frankandoak.synchronization.retrofit;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Michael on 2014-05-09.
 */
public interface GetCategoryProductsClient {
    @GET("/products")
    GetCategoryProductsResponse downloadCategoryProducts(@Query("store_id") Long storeId, @Query("compact") Integer compact);
}
