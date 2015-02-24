package com.frankandoak.synchronization.sync.synchronizers.preprocessors;


import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.models.RemoteFavorite;

import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class FavoritePreProcessor extends BasePreProcessor<RemoteFavorite> {

    @Override
    public void preProcessRemoteRecords(List<RemoteFavorite> records) {

        for( RemoteFavorite favorite : records ) {
            if( favorite != null ) {
                favorite.setIsDeleted(false);
                favorite.setSyncStatus(RemoteObject.SyncStatus.NO_CHANGES);
            }
        }
    }
}
