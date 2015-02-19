package com.frankandoak.synchronization.retrofit.responses;

import com.frankandoak.synchronization.models.RemoteCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class GetCategoriesResponse extends RemoteResponse {

    public static class Stores
    {
        @Expose
        @SerializedName("stores")
        public List<RemoteCategory> mStores;

        public List<RemoteCategory> getStores() {
            return mStores;
        }
    }


    @Expose
    @SerializedName("response")
    private Stores mResponse;

    public Stores getResponse() {
        return mResponse;
    }
}
