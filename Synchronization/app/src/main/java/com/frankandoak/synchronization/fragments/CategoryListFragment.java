package com.frankandoak.synchronization.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frankandoak.synchronization.R;
import com.frankandoak.synchronization.SYNConstants;
import com.frankandoak.synchronization.activities.CategoryListActivity;
import com.frankandoak.synchronization.activities.ProductListActivity;
import com.frankandoak.synchronization.adapters.CategoryAdapter;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.events.CategoryClickedEvent;
import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class CategoryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRv;
    private LinearLayoutManager mLayoutManager;
    private CategoryAdapter mCstegoryAdapter;
    private List<RemoteCategory> mCategories;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, null);

        mRv = (RecyclerView) view.findViewById(R.id.fragment_category_list_rv);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mCstegoryAdapter = new CategoryAdapter(mCategories);
        mRv.setAdapter(mCstegoryAdapter);

        // Decorations
        mRv.addItemDecoration(new DividerItemDecoration(getActivity()));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRv.setHasFixedSize(true);
        
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getLoaderManager().initLoader(SYNConstants.CATEGORIES_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id)
        {
            case SYNConstants.CATEGORIES_LOADER_ID:
                return new CursorLoader(getActivity(), SYNContentProvider.URIS.CATEGORIES_URI, CategoryTable.ALL_COLUMNS, null, null, CategoryTable.POSITION);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId())
        {
            case SYNConstants.CATEGORIES_LOADER_ID:

                if( data != null ) {
                    mCategories.clear();

                    for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                        mCategories.add(new RemoteCategory(data));
                    }

                    mCstegoryAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    public void onEventMainThread(CategoryClickedEvent event) {

        RemoteCategory category = event.getCategory();
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(ProductListActivity.EXTRAS.IN_CATEGORY_ID, category);
        startActivity(intent);

    }
}
