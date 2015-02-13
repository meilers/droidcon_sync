package com.frankandoak.synchronization.utilities;

import android.content.Context;
import android.database.Cursor;

import com.frankandoak.synchronization.models.RemoteCategory;
import com.frankandoak.synchronization.models.RemoteCategoryProduct;
import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.models.RemoteProduct;
import com.frankandoak.synchronization.synchronizers.BaseSynchronizer;
import com.frankandoak.synchronization.synchronizers.preprocessors.BasePreProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mj_eilers on 15-02-09.
 */
public class SyncUtil {

    public static String buildIdentifierTag(List<String> identifiers)
    {
        if( identifiers == null || identifiers.isEmpty() )
            return null;

        String tag = "";
        int i = 0;

        for( ; i < identifiers.size()-1; ++i )
        {
            tag += identifiers.get(i) + "_";
        }

        tag += identifiers.get(identifiers.size()-1);

        return tag;
    }

    public static String buildIdentifierTag(Cursor item, List<Integer> columnIndices)
    {
        if( item == null || columnIndices == null || columnIndices.isEmpty() )
            return null;

        String tag = "";

        for( int i=0; i < columnIndices.size()-1; ++i )
        {
            tag += item.getString(columnIndices.get(i)) + "_";
        }

        tag += item.getString(columnIndices.get(columnIndices.size()-1));

        return tag;
    }

    private static List<Integer> getColumnIndices(Cursor cursor, List<String> identifierKeys)
    {
        List<Integer> columnIndices = new ArrayList<>();

        for( String id : identifierKeys )
        {
            columnIndices.add(cursor.getColumnIndex(id));
        }

        return columnIndices;
    }



    public static void synchronizeProducts( Context c,
                                                    List<RemoteProduct> remoteItems,
                                                    Cursor localItems,
                                                    List<Integer> identifierColumnIndices,
                                                    BaseSynchronizer<RemoteProduct> synchronizer,
                                                    BasePreProcessor<RemoteProduct> preProcessor) {

        if( preProcessor != null )
            preProcessor.preProcessRemoteRecords(remoteItems);

        synchronizer.synchronize(c, remoteItems, localItems, identifierColumnIndices);
    }

    public static void synchronizeCategoryProducts( Context c,
                                                    List<RemoteCategoryProduct> remoteItems,
                                                    Cursor localItems,
                                                    List<Integer> identifierColumnIndices,
                                                    BaseSynchronizer<RemoteCategoryProduct> synchronizer,
                                                    BasePreProcessor<RemoteCategoryProduct> preProcessor) {

        if( preProcessor != null )
            preProcessor.preProcessRemoteRecords(remoteItems);

        synchronizer.synchronize(c, remoteItems, localItems, identifierColumnIndices);
    }

}
