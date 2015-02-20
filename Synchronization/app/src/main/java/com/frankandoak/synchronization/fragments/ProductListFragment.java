package com.frankandoak.synchronization.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frankandoak.synchronization.R;
import com.frankandoak.synchronization.SYNConstants;
import com.frankandoak.synchronization.activities.ProductListActivity;
import com.frankandoak.synchronization.adapters.ProductAdapter;
import com.frankandoak.synchronization.database.CategoryProductTable;
import com.frankandoak.synchronization.database.CategoryTable;
import com.frankandoak.synchronization.database.ProductTable;
import com.frankandoak.synchronization.events.ProductClickedEvent;
import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.providers.SYNContentProvider;
import com.frankandoak.synchronization.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class ProductListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final class ARGUMENTS {
        public static final String IN_CATEGORY = "inCategory";
    }

    private RemoteCategory mCategory;

    private RecyclerView mRv;
    private GridLayoutManager mLayoutManager;
    private ProductAdapter mCategoryAdapter;
    private List<RemoteProduct> mProducts;

    public static final ProductListFragment newInstance(RemoteCategory cat) {
        ProductListFragment f = new ProductListFragment();

        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
            args.putParcelable(ARGUMENTS.IN_CATEGORY, cat);
        }

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProducts = new ArrayList<>();
        mCategory = getArguments().getParcelable(ARGUMENTS.IN_CATEGORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, null);

        mRv = (RecyclerView) view.findViewById(R.id.fragment_product_list_rv);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mCategoryAdapter = new ProductAdapter(mProducts);
        mRv.setAdapter(mCategoryAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRv.setHasFixedSize(true);
        
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getLoaderManager().initLoader(SYNConstants.CATEGORY_PRODUCTS_LOADER_ID, null, this);
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
            case SYNConstants.CATEGORY_PRODUCTS_LOADER_ID:

                return new CursorLoader(
                        getActivity(),
                        SYNContentProvider.URIS.CATEGORY_PRODUCTS_URI,
                        ProductTable.ALL_COLUMNS,
                        CategoryProductTable.CATEGORY_ID + "=?",
                        new String[] {mCategory.getCategoryId()+""},
                        CategoryProductTable.POSITION
                );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId())
        {
            case SYNConstants.CATEGORY_PRODUCTS_LOADER_ID:

                if( data != null ) {
                    mProducts.clear();

                    for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                        mProducts.add(new RemoteProduct(data));
                    }

                    mCategoryAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    public void onEventMainThread(ProductClickedEvent event) {



    }
}
