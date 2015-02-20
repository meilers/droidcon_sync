package com.frankandoak.synchronization.events;

import com.frankandoak.synchronization.models.RemoteProduct;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class ProductClickedEvent {

    private RemoteProduct mProduct;


    public ProductClickedEvent(RemoteProduct product) {
        this.mProduct = product;
    }

    public RemoteProduct getProduct() {
        return mProduct;
    }
}
