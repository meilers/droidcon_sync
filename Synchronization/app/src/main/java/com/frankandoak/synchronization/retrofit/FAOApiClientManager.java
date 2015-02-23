package com.frankandoak.synchronization.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Michael on 2014-03-11.
 */
public class FAOApiClientManager {

    private static FAOApiClientManager mInstance;
    private String mBaseUrl = "https://api.frankandoak.com/v2/BackendConnector";

    private RestAdapter mRestAdapter;
    private Map<String, Object> mClients = new HashMap<String, Object>();


    private FAOApiClientManager() {
    }

    public static FAOApiClientManager getInstance() {
        if (null == mInstance) {
            mInstance = new FAOApiClientManager();
        }

        return mInstance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getClient(Context context, Class<T> clazz) {
        if (mRestAdapter == null) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {

                request.addHeader("X-Session-Token", "1qf3h_v6hitkbehmse4beqcc1au561c3");
                }
            };


            mRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(getBaseUrl())
                    .setConverter(new GsonConverter(gson))
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        }
        T client = null;
        if ((client = (T) mClients.get(clazz.getCanonicalName())) != null) {
            return client;
        }
        client = mRestAdapter.create(clazz);
        mClients.put(clazz.getCanonicalName(), client);
        return client;
    }

    public void setRestAdapter(RestAdapter restAdapter) {
        mRestAdapter = restAdapter;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }


}
