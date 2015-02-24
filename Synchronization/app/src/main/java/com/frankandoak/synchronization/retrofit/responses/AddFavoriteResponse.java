package com.frankandoak.synchronization.retrofit.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Michael on 2014-03-11.
 */
public class AddFavoriteResponse extends RemoteResponse {

    @Expose
    @SerializedName("response")
    public AddAddressState mResponse;

    public static class AddAddressState {

        @Expose
        @SerializedName("errorDescription")
        public String mErrorDescription;

        public String getErrorDescription() {
            return mErrorDescription;
        }
    }

    public AddAddressState getResponse() {
        return mResponse;
    }
}
