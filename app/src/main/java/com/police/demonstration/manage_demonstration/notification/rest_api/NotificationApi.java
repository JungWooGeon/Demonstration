package com.police.demonstration.manage_demonstration.notification.rest_api;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationApi {
    @POST("/one")
    Observable<ResponseBody> getNotificationImageOne(@Body NotificationRequest request);

    @POST("/two")
    Observable<ResponseBody> getNotificationImageTwo(@Body NotificationRequest request);

    @POST("/three")
    Observable<ResponseBody> getNotificationImageThree(@Body NotificationRequest request);
}