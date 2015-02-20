package com.frankandoak.synchronization.synchronizers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.frankandoak.synchronization.models.RemoteObject;
import com.frankandoak.synchronization.utils.SyncUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Michael on 2014-03-11.
 */
public abstract class BaseSynchronizer<T extends RemoteObject> {

    protected Context mContext;

    public BaseSynchronizer(Context context) {
        mContext = context;
    }

    /*
     * The ID column for the localItems Cursor must be at index 0!
     */
    public void synchronize(Context context, List<T> items, Cursor localItems, String[] identifierKeys) {

        Set<String> idKeys;
        Collection<String> idValues;
        String tag;

        Map<String, T> remoteEntities = new HashMap<String, T>();
        for (T entity : items) {

            idValues = entity.getIdentifiers().values();                // Returned in order by LinkedHashMap
            tag = SyncUtil.getIdentifierTag(idValues);

            if( tag != null )
                remoteEntities.put(tag, entity);
        }

        List<T> updates = new ArrayList<T>();
        List<Long> deletions = new ArrayList<Long>();
        for (boolean hasItem = localItems.moveToFirst(); hasItem; hasItem = localItems.moveToNext()) {

            tag = SyncUtil.getIdentifierTag(localItems, identifierKeys);

            T matchingEntity = remoteEntities.get(tag);
            if (matchingEntity == null) {
                // there was no match so this entity should be removed from the
                // local storage
                deletions.add(localItems.getLong(0));
                continue;
            }
            if (this.isRemoteEntityNewerThanLocal(matchingEntity, localItems)) {
                // the remote entity is newer than the local counterpart, mark
                // this one for update

                updates.add(matchingEntity);
            }
            remoteEntities.remove(tag);
            continue;
        }
        // anything left over in the remoteEntities Map is a new entity that we
        // don't have yet, mark them for insertion
        List<T> inserts = new ArrayList<T>(remoteEntities.values());

        this.performSynchronizationOperations(context, inserts, updates, deletions);
    }


    protected abstract void performSynchronizationOperations(Context context, List<T> inserts, List<T> updates,
                                                             List<Long> deletions);

    protected abstract boolean isRemoteEntityNewerThanLocal(T remote, Cursor c);

    protected ContentValues getContentValuesForRemoteEntity(T t) {
        ContentValues values = new ContentValues();
        t.populateContentValues(values);

        return values;
    }
}