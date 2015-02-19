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
import com.frankandoak.synchronization.adapters.listeners.CategoryClickListener;
import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.utils.UiUtil;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class CategoryViewHolder extends RecyclerView.ViewHolder  {

    final static int WIDTH = UiUtil.getScreenMetrics(SYNApplication.getContext()).widthPixels;;
    final static int HEIGHT = (int) (WIDTH * 0.494);


    private CategoryClickListener mCategoryClickListener;

    public NetworkImageView mIv;
    public TextView mNameTv;
    public View mSelectorView;

    public CategoryViewHolder(View v, CategoryClickListener categoryClickListener) {
        super(v);

        mCategoryClickListener = categoryClickListener;

        mIv = (NetworkImageView) v.findViewById(R.id.list_item_category_iv);
        mNameTv = (TextView) v.findViewById(R.id.list_item_category_name);
        mSelectorView = v.findViewById(R.id.list_item_category_selector);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryClickListener.onClick(getPosition());

            }
        });
    }

    public void bindViewHolder(RemoteCategory category) {
        mIv.setLayoutParams(new FrameLayout.LayoutParams(WIDTH, HEIGHT));
        mSelectorView.setLayoutParams(mIv.getLayoutParams());

        if (category.getImageUrl() != null && WIDTH > 0 && HEIGHT > 0) {
            // Adapter re-use is automatically detected and the previous download canceled.
            mIv.setImageUrl(category.getImageUrl(), SYNApplication.getInstance().getImageLoader());
        }

        if (category.getName() != null)
            mNameTv.setText(category.getName());
    }

}
