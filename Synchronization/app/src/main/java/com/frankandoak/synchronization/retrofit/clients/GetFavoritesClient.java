package com.frankandoak.synchronization.retrofit.clients;

import com.frankandoak.synchronization.retrofit.responses.GetCategoryProductsResponse;
import com.frankandoak.synchronization.retrofit.responses.GetFavoritesResponse;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Michael on 2014-05-09.
 */
public interface GetFavoritesClient {
    @GET("/favourites")
    GetFavoritesResponse getFavorites();
}
