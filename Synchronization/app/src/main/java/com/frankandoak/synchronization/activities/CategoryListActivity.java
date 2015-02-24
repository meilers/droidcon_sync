package com.frankandoak.synchronization.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.frankandoak.synchronization.R;
import com.frankandoak.synchronization.services.SyncCategoriesService;
import com.frankandoak.synchronization.services.SyncCategoryProductsService;
import com.frankandoak.synchronization.services.SyncFavoritesService;


public class CategoryListActivity extends ActionBarActivity {

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.frankandoak.synchronization.providers.SYNContentProvider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.frankandoak.synchronization";
    // The account name
    public static final String ACCOUNT = "synchronization";
    // Instance fields
    Account mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        setTitle(getString(R.string.categories));

        mAccount = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                10L);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, SyncCategoriesService.class);
        intent.setAction(Intent.ACTION_SYNC);
        startService(intent);

        intent = new Intent(this, SyncFavoritesService.class);
        intent.setAction(Intent.ACTION_SYNC);
        startService(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
    }
}
