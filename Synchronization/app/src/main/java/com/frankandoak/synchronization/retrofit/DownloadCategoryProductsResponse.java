package com.frankandoak.synchronization.retrofit;

import com.frankandoak.synchronization.models.RemoteProduct;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class DownloadCategoryProductsResponse {

    @Expose
    public List<RemoteProduct> response;

    public DownloadCategoryProductsResponse() {}

}
