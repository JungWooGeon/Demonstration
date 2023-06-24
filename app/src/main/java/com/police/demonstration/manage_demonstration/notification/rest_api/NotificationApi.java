package com.police.demonstration.manage_demonstration.notification.rest_api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationApi {
    @POST("/two")
    Call<ResponseBody> uploadImage(@Body NotificationRequest request);
}