package com.appreman.app.Api;

import com.appreman.app.Utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAdapter {

    private ApiServices API_SERVICE;

    public ApiServices getApiService(String apiUrl) {

        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            API_SERVICE = retrofit.create(ApiServices.class);
        }

        return API_SERVICE;
    }
}