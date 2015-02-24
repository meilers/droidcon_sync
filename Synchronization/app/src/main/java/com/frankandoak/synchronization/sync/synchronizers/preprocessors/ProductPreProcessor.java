package com.frankandoak.synchronization.sync.synchronizers.preprocessors;


import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.models.RemoteObject;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class ProductPreProcessor extends BasePreProcessor<RemoteProduct> {

    @Override
    public void preProcessRemoteRecords(List<RemoteProduct> records) {

        for( RemoteProduct product : records ) {
            if( product != null ) {
                product.setIsDeleted(false);
                product.setSyncStatus(RemoteObject.SyncStatus.NO_CHANGES);
            }
        }
    }
}
