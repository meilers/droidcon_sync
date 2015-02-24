package com.frankandoak.synchronization.sync.syncAdapters;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.frankandoak.synchronization.sync.syncAdapters.FavoriteSyncAdapter;


public class FavoriteSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static FavoriteSyncAdapter sFavoriteSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sFavoriteSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sFavoriteSyncAdapterLock) {
            if (sFavoriteSyncAdapter == null) {
                sFavoriteSyncAdapter = new FavoriteSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the FavoriteSyncAdapter
         * constructors call super()
         */
        return sFavoriteSyncAdapter.getSyncAdapterBinder();
    }
}