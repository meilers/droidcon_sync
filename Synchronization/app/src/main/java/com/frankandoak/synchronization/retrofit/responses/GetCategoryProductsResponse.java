package com.frankandoak.synchronization.retrofit.responses;

import com.frankandoak.synchronization.models.RemoteProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class GetCategoryProductsResponse extends RemoteResponse {

    @Expose
    @SerializedName("response")
    private List<RemoteProduct> mResponse;

    public List<RemoteProduct> getResponse() {
        return mResponse;
    }
}
