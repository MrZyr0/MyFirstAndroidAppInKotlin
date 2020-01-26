package com.jseixas.randomUser.network;

/**
 * Callback for asynchronous call for ApiRequest.
 * methods can be overrides if needed.
 *
 * Created by Nicolas Churlet on 13/04/2018.
 */

public class ApiRequestCallback<T> {
    public void onSuccess(T result) {}
    public void onError(ApiError error) {}
}
