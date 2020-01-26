package com.jseixas.randomUser.network;

import android.content.Context;
import android.content.SharedPreferences;
import com.jseixas.randomUser.model.RandomUsersResults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nicolas Churlet on 21/03/2018.
 */

public class ApiHelpers {

    public static final String ENDPOINT = "https://randomuser.me/api/"; // API URL
    public static final String API_KEY = "";

    private ErpInterventionApiService apiservice;

    final private Context _context;

    SharedPreferences preferences;

    public ApiHelpers(Context context) {

        this._context = context;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        // region OkHttpClient
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request request = chain.request();

                        preferences = _context.getSharedPreferences("login", MODE_PRIVATE);
                        String token = preferences.getString("token", "");

                        final Request newRequest;
                        // cas de l'auhtnetification
                        if (request.url().encodedPathSegments().get(0).equals("api") &&
                                request.url().encodedPathSegments().get(1).equals("Auth") &&
                                request.url().encodedPathSegments().get(2).equals("Login")) {
                            newRequest = request.newBuilder()
                                    .build();
                        } else if (token == null) {
                            return null;
                        } else {
                            newRequest = request.newBuilder()
                                    //ajoute "baerer: token" en header de chaque requête
                                    .addHeader("Authorization", "Bearer " + token)
                                    .build();
                        }
                        System.out.println("request = " + request);

                        return chain.proceed(newRequest);
                    }
                })
                .build();
        // endregion OkHttpClient

        // region API service
        apiservice = new Retrofit
                .Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(ErpInterventionApiService.class);
        // endregion API service
    }

    // Custom API request
    public void apiRequestUsersByCount(
            int nbOfUsers,  // API parameter
            ApiRequestCallback<RandomUsersResults> callback
    ) {
        (new ApiRequest<RandomUsersResults>()).
                requestAsync(apiservice.getUsersByCount(nbOfUsers), callback);  // Do the request
    }
}