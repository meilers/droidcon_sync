package com.frankandoak.synchronization.events;

import com.frankandoak.synchronization.models.RemoteFavorite;
import com.frankandoak.synchronization.models.RemoteProduct;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class ProductClickedEvent {

    private RemoteProduct mProduct;
    private RemoteFavorite mFavorite;

    public ProductClickedEvent(RemoteProduct product, RemoteFavorite favorite) {
        mProduct = product;
        mFavorite = favorite;
    }

    public RemoteProduct getProduct() {
        return mProduct;
    }

    public RemoteFavorite getFavorite() {
        return mFavorite;
    }
}
