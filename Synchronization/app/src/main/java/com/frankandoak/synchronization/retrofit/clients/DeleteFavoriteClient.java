package com.frankandoak.synchronization.retrofit.clients;

import com.frankandoak.synchronization.retrofit.responses.DeleteFavoriteResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Michael on 2014-05-09.
 */
public interface DeleteFavoriteClient {
    @FormUrlEncoded
    @POST("/delete_favorite")
    DeleteFavoriteResponse deleteFavorite(@Field("sku") String productSku);
}

