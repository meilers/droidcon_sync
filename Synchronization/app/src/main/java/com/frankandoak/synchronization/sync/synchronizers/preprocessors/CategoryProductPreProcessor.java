package com.frankandoak.synchronization.sync.synchronizers.preprocessors;


import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.models.RemoteCategoryProduct;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class CategoryProductPreProcessor extends BasePreProcessor<RemoteCategoryProduct> {

    @Override
    public void preProcessRemoteRecords(List<RemoteCategoryProduct> records) {

        for( RemoteCategoryProduct product : records ) {
            if( product != null ) {
                product.setIsDeleted(false);
                product.setSyncStatus(RemoteObject.SyncStatus.NO_CHANGES);
            }
        }
    }
}
