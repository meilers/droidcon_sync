package com.frankandoak.synchronization.utils;

import android.content.Context;
import android.database.Cursor;

import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.synchronizers.BaseSynchronizer;
import com.frankandoak.synchronization.synchronizers.preprocessors.BasePreProcessor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mj_eilers on 15-02-09.
 */
public class SyncUtil {

    public static class Selection {
        private String mQuery;
        private String[] mValues;

        public Selection(String query, String[] values) {
            mQuery = query;
            mValues = values;
        }

        public String getQuery() {
            return mQuery;
        }

        public String[] getValues() {
            return mValues;
        }
    }


    public static String getIdentifierTag(Collection<String> idValues)
    {
        if( idValues == null || idValues.isEmpty() )
            return null;

        String tag = "";
        Iterator<String> iterator = idValues.iterator();
        String idValue;

        while(iterator.hasNext()) {
            idValue = iterator.next();

            if(iterator.hasNext()) {
                tag += idValue + "_";
            }
            else {
                tag += idValue;
            }
        }

        return tag;
    }

    public static String getIdentifierTag(Cursor cursor, String[] idKeys)
    {
        if( cursor == null || idKeys == null || idKeys.length == 0 )
            return null;

        String tag = "";
        String idKey;
        String idValue;

        for( int i = 0; i < idKeys.length-1; ++i ) {
            idKey = idKeys[i];
            idValue = cursor.getString(cursor.getColumnIndex(idKey));
            tag += idValue + "_";
        }

        idKey = idKeys[ idKeys.length-1];
        idValue = cursor.getString(cursor.getColumnIndex(idKey));
        tag += idValue;

        return tag;
    }




    public static <T extends RemoteObject> void synchronize(
            Context c,
            List<T> remoteItems,
            Cursor localItems,
            String[] idKeys,
            BaseSynchronizer<T> synchronizer,
            BasePreProcessor<T> preProcessor) {

        if( preProcessor != null )
            preProcessor.preProcessRemoteRecords(remoteItems);

        localItems.moveToFirst();
        synchronizer.synchronize(c, remoteItems, localItems, idKeys);
    }


    public static Selection buildSelection(LinkedHashMap<String,String> identifiers) {

        if( identifiers == null )
            return null;

        String selectionQuery = "";
        String[] selectionValues = new String[identifiers.size()];

        Set<Map.Entry<String, String>> entries = identifiers.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        Map.Entry<String, String> entry;
        int i = 0;

        while(iterator.hasNext()) {
            entry = iterator.next();

            if(iterator.hasNext()) {
                selectionQuery += entry.getKey() + "=?,";
            }
            else {
                selectionQuery += entry.getKey() + "=?";
            }

            selectionValues[i] = entry.getValue();
            ++i;
        }

        Selection selection = new Selection(selectionQuery, selectionValues);

        return selection;
    }
}
