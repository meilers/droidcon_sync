package com.frankandoak.synchronization.sync.synchronizers.preprocessors;


import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.models.RemoteObject;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class CategoryPreProcessor extends BasePreProcessor<RemoteCategory> {

    @Override
    public void preProcessRemoteRecords(List<RemoteCategory> records) {

        int i = 0;
        for( RemoteCategory catProduct : records ) {
            if( catProduct != null ) {
                catProduct.setIsDeleted(false);
                catProduct.setSyncStatus(RemoteObject.SyncStatus.NO_CHANGES);

                catProduct.setPosition(i);
                ++i;
            }
        }
    }
}
