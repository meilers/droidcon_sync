package com.frankandoak.synchronization.adapters.viewHolders;

/**
 * Created by mj_eilers on 15-02-19.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.frankandoak.synchronization.R;
import com.frankandoak.synchronization.SYNApplication;
import com.frankandoak.synchronization.adapters.listeners.ProductClickListener;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.utils.UiUtil;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class ProductViewHolder extends RecyclerView.ViewHolder  {

    private ProductClickListener mProductClickListener;

    public NetworkImageView mIv;
    public TextView mNameTv;
    public TextView mPriceTv;
    public TextView mFavoriteCountTv;
    public View mSelectorView;

    public ProductViewHolder(View v, ProductClickListener productClickListener) {
        super(v);

        mProductClickListener = productClickListener;

        mIv = (NetworkImageView) v.findViewById(R.id.list_item_product_iv);
        mNameTv = (TextView) v.findViewById(R.id.list_item_product_name_tv);
        mPriceTv = (TextView) v.findViewById(R.id.list_item_product_price_tv);
        mSelectorView = v.findViewById(R.id.list_item_product_selector);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductClickListener.onClick(getPosition());

            }
        });
    }

    public void bindViewHolder(RemoteProduct product) {

        mNameTv.setText(product.getName());
        mPriceTv.setText("$" + product.getPrice());

        if (product.getImageUrl() != null) {
            // Adapter re-use is automatically detected and the previous download canceled.
            mIv.setImageUrl(product.getImageUrl(), SYNApplication.getInstance().getImageLoader());
        }
    }

}
