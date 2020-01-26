package com.jseixas.randomUser.network;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Nicolas Churlet on 11/04/2018.
 */

public class AppInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = "";
        Request originalRequest = chain.request();
        Request jsonHeaderRequest = originalRequest.newBuilder().addHeader("Authorization", "Bearer " + token).build();
        Response response = chain.proceed(jsonHeaderRequest);
        // Avant le return tu peux catch le code pour un retour générique dans le cas ou ton server traite les tokens expirés etc...
        return response;
    }
}
