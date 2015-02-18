package com.frankandoak.synchronization.retrofit;

import com.google.gson.annotations.Expose;

/**
 * Created by Michael on 2014-04-23.
 */
public class RemoteResponse {

    @Expose
    public String status;

    @Expose
    public String errorDescription;

    public String getStatus() {
        return status;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
