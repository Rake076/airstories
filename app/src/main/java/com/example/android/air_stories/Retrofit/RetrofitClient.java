package com.example.android.air_stories.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private INodeJS myApi;
    private static Retrofit retrofit = null;
    final static String BASE_URL = "http://192.168.18.201:5000/";




    public static Retrofit getInstance(){
        if(instance == null)
            instance = new Retrofit.Builder()
                   // .baseUrl("http://10.0.2.2:5000/") // In Emulator, 127.0.0.1 will change to 10.0.2.2
//                    .baseUrl("http://192.168.18.67:5000/")
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        return instance;
    }



    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
