package com.frankandoak.synchronization.retrofit.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Michael on 2014-03-11.
 */
public class DeleteFavoriteResponse extends RemoteResponse {

    @Expose
    @SerializedName("response")
    public DeleteAddressState mResponse;

    public static class DeleteAddressState {

        @Expose
        @SerializedName("removed")
        public Integer mRemoved;
    }

    public DeleteAddressState getResponse() {
        return mResponse;
    }
}
