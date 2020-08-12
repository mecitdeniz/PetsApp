package com.mecitdeniz.petto;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetsApiClient {

    private static final String BASE_URL ="";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.5:3030/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
