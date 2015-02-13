package com.frankandoak.synchronization.synchronizers.preprocessors;


import com.frankandoak.synchronization.models.RemoteProduct;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class CategoryProductPreProcessor extends BasePreProcessor<RemoteProduct> {

    @Override
    public void preProcessRemoteRecords(List<RemoteProduct> records) {

        int i = 0;
        for( RemoteProduct product : records ) {
            if( product != null ) {
                ++i;
            }
        }
    }
}
