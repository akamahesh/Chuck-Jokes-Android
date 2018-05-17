package com.akamahesh.chuckjokes.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    public static final String kAPIBaseURL = "https://api.chucknorris.io/";

    private static APIManager _APIManager;
    private Retrofit _Retrofit;
    private APIRequestHelper _APIHelper;

    private APIManager(){}

    public static synchronized APIManager APIManager(){
        if(_APIManager == null){
            _APIManager = new APIManager();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .build();
            _APIManager._Retrofit = new Retrofit.Builder()
                    .baseUrl(kAPIBaseURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            _APIManager._APIHelper = _APIManager._Retrofit.create(APIRequestHelper.class);
        }
        return _APIManager;
    }

    public APIRequestHelper getRequestHelper(){
        return _APIHelper;
    }

}
