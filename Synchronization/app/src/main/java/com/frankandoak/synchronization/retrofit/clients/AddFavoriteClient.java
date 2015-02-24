package com.frankandoak.synchronization.retrofit.clients;

import com.frankandoak.synchronization.retrofit.responses.AddFavoriteResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Michael on 2014-05-09.
 */
public interface AddFavoriteClient {
    @FormUrlEncoded
    @POST("/add_favorite")
    AddFavoriteResponse addFavorite(@Field("sku") String productSku);
}

