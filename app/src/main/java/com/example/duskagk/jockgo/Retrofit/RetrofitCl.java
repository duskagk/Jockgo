package com.example.duskagk.jockgo.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitCl {
    private static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance==null)
            instance=new Retrofit.Builder()
                    .baseUrl("https://che5uuetmi.execute-api.ap-northeast-2.amazonaws.com/test/login")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
