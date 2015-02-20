package com.frankandoak.synchronization.adapters.viewHolders;

/**
 * Created by mj_eilers on 15-02-19.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

    private static final int CELL_MARGIN_DP = 2;

    private ProductClickListener mProductClickListener;

    public NetworkImageView mIv;
    public TextView mNameTv;
    public TextView mPriceTv;
    public Button mFavoriteCountTv;
    public View mSelectorView;

    public ProductViewHolder(View v, ProductClickListener productClickListener) {
        super(v);

        mProductClickListener = productClickListener;

        mIv = (NetworkImageView) v.findViewById(R.id.list_item_product_iv);
        mNameTv = (TextView) v.findViewById(R.id.list_item_product_name_tv);
        mPriceTv = (TextView) v.findViewById(R.id.list_item_product_price_tv);
        mSelectorView = v.findViewById(R.id.list_item_product_selector);
        mFavoriteCountTv = (Button)v.findViewById(R.id.list_item_product_favorite_btn);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductClickListener.onClick(getPosition());

            }
        });
    }

    public void bindViewHolder(RemoteProduct product) {

        Context c = SYNApplication.getContext();
        int margin = UiUtil.convertDpToPixels(CELL_MARGIN_DP, c);
        final int fullWidth = UiUtil.getScreenMetrics(c).widthPixels;
        int width = fullWidth/2 - margin;

        mNameTv.setText(product.getName());
        mPriceTv.setText("$" + product.getPrice().intValue());
        mIv.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        mFavoriteCountTv.setText(product.getFavoriteCount()+"");

        if (product.getImageUrl() != null) {
            // Adapter re-use is automatically detected and the previous download canceled.
            mIv.setImageUrl(product.getImageUrl(), SYNApplication.getInstance().getImageLoader());
        }
    }

}
