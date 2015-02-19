package com.frankandoak.synchronization.retrofit.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Michael on 2014-04-23.
 */
public class RemoteResponse {

    @Expose
    @SerializedName("status")
    public String mStatus;

    @Expose
    @SerializedName("errorDescription")
    public String mErrorDescription;

    public String getStatus() {
        return mStatus;
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }
}
