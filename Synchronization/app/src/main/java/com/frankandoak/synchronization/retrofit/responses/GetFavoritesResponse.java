package com.frankandoak.synchronization.retrofit.responses;

import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mj_eilers on 15-02-20.
 */
public class GetFavoritesResponse extends RemoteResponse {

    @Expose
    @SerializedName("response")
    public Favorites mResponse;


    public static class Favorites
    {
        @Expose
        @SerializedName("products_summary")
        public List<RemoteProduct> mFavorites;

        public List<RemoteProduct> getFavorites() {
            return mFavorites;
        }
    }

    public Favorites getResponse() {
        return mResponse;
    }
}
