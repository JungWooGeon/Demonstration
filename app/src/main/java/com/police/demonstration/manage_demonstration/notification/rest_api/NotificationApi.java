package com.police.demonstration.manage_demonstration.notification.rest_api;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationApi {
    @POST("/two")
    Observable<ResponseBody> getNotificationImage(@Body NotificationRequest request);
}