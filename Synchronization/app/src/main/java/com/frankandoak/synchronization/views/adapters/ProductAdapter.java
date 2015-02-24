package com.frankandoak.synchronization.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frankandoak.synchronization.R;
import com.frankandoak.synchronization.views.adapters.listeners.ProductClickListener;
import com.frankandoak.synchronization.views.adapters.viewHolders.ProductViewHolder;
import com.frankandoak.synchronization.events.ProductClickedEvent;
import com.frankandoak.synchronization.models.RemoteFavorite;
import com.frankandoak.synchronization.models.RemoteProduct;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private List<RemoteProduct> mProducts;
    private HashMap<Long,RemoteFavorite> mFavorites;

    private ProductClickListener mProductClickListener = new ProductClickListener() {
        @Override
        public void onClick(int position) {

            RemoteProduct product = mProducts.get(position);
            EventBus.getDefault().post(new ProductClickedEvent(product, mFavorites.get(product.getProductId())));
        }
    };


    public ProductAdapter(List<RemoteProduct> items, HashMap<Long,RemoteFavorite> favorites) {
        mProducts = items;
        mFavorites = favorites;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);

        return new ProductViewHolder(v, mProductClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        RemoteProduct item = mProducts.get(position);
        RemoteFavorite favorite = mFavorites.get(item.getProductId());

        holder.bindViewHolder(item, favorite);

    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }


}
