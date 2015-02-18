package com.frankandoak.synchronization.synchronizers.preprocessors;


import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.models.RemoteCategoryProduct;
import com.frankandoak.synchronization.models.RemoteProduct;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class CategoryProductPreProcessor extends BasePreProcessor<RemoteCategoryProduct> {

    private Long mCategoryId;
    private List<RemoteProduct> mProducts;


    public CategoryProductPreProcessor(Long categoryId, List<RemoteProduct> products)
    {
        this.mCategoryId = categoryId;
        this.mProducts = products;
    }

    @Override
    public void preProcessRemoteRecords(List<RemoteCategoryProduct> records) {

        for( int i = 0; i < records.size(); ++i ) {

            RemoteCategoryProduct categoryProduct = records.get(i);
            RemoteProduct product = mProducts.get(i);

            if( categoryProduct != null ) {
                categoryProduct.setCategoryId(mCategoryId);
                categoryProduct.setProductId(product.getProductId());
            }
        }
    }
}
