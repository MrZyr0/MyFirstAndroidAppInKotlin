package com.jseixas.randomUser.network;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nicolas Churlet on 12/04/2018.
 */

public class ApiRequest<T>
{
    public void requestAsync(Call<T> c, ApiRequestCallback<T> callback) {
        final ApiRequestCallback<T> fcallback = callback;
        final Throwable throwable;
        c.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    fcallback.onSuccess(response.body());
                    Log.d("requestAsync ", " onSuccess: " + response.message());
                } else {
                    fcallback.onError(new ApiError(response));
                    Log.d("APIrequestAsync ",
                            "onError: Code = [" + response.code() + "], message = ["
                                    + response.message() + "], body = [" + response.errorBody()
                                    + "]");
                    Log.d("APIrequestAsync ", "onError: Call was = [" + call.request()
                            + "] or [" + call.toString());
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                fcallback.onError(new ApiError(t));
                Log.d("requestAsync ",  "onFailure : " + t.getMessage() + ", "
                        + t.getLocalizedMessage() + ", " + t.getCause());
            }
        });
    }
}