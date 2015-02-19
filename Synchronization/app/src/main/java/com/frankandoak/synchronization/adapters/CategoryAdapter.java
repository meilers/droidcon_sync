package com.frankandoak.synchronization.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frankandoak.synchronization.R;
import com.frankandoak.synchronization.adapters.listeners.CategoryClickListener;
import com.frankandoak.synchronization.adapters.viewHolders.CategoryViewHolder;
import com.frankandoak.synchronization.events.CategoryClickedEvent;
import com.frankandoak.synchronization.models.RemoteCategory;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RemoteCategory> mCategories;
    private CategoryClickListener mCategoryClickListener = new CategoryClickListener() {
        @Override
        public void onClick(int position) {
            EventBus.getDefault().post(new CategoryClickedEvent(mCategories.get(position)));
        }
    };


    public CategoryAdapter(List<RemoteCategory> items) {
        mCategories = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);

        return new CategoryViewHolder(v, mCategoryClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        RemoteCategory item = mCategories.get(position);

        ((CategoryViewHolder) holder).bindViewHolder(item);

    }


    @Override
    public int getItemCount() {
        return mCategories.size();
    }


}
