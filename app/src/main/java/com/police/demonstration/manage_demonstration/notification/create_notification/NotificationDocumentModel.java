package com.police.demonstration.manage_demonstration.notification.create_notification;

import static com.police.demonstration.Constants.API_NOTIFICATION_BASE_URL;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_HOME;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.TIME_ZONE_DAY;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.manage_demonstration.notification.rest_api.NotificationApi;
import com.police.demonstration.manage_demonstration.notification.rest_api.NotificationRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationDocumentModel {

    private Uri notificationUri;
    private CallbackListener callbackListener;

    public Uri getNotificationUri() {
        return notificationUri;
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public void getNotificationUri(Context context, DemonstrationInfo demonstrationInfo, MeasurementInfo measurementInfo) {
        String timeZone = "";
        switch (demonstrationInfo.getTimeZone()) {
            case TIME_ZONE_DAY:
                timeZone = context.getString(R.string.day);
                break;
            case TIME_ZONE_NIGHT:
                timeZone = context.getString(R.string.night);
                break;
            case TIME_ZONE_LATE_NIGHT:
                timeZone = context.getString(R.string.late_night);
                break;
            default:
                break;
        }

        String placeZone = "";
        switch (demonstrationInfo.getPlaceZone()) {
            case PLACE_ZONE_HOME:
                placeZone = context.getString(R.string.example_place_zone_home);
                break;
            case PLACE_ZONE_PUBLIC:
                placeZone = context.getString(R.string.example_place_zone_public);
                break;
            case PLACE_ZONE_ETC:
                placeZone = context.getString(R.string.example_place_zone_etc);
                break;
            default:
                break;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_NOTIFICATION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);
        NotificationRequest request = new NotificationRequest(
                measurementInfo.getStartTime() + context.getString(R.string.space) + context.getString(R.string.tilde) + context.getString(R.string.space) + measurementInfo.getEndTime(),
                timeZone,
                measurementInfo.getPlace() + context.getString(R.string.space) + measurementInfo.getDetailPlace(),
                measurementInfo.getDistance(),
                placeZone, measurementInfo.getWinterSpeed(),
                demonstrationInfo.getStandardHighest(),
                measurementInfo.getCorrectionHighest(), demonstrationInfo.getOrganizerName()
        );

        // POST 요청 보내고 응답 받기
        Call<ResponseBody> call = notificationApi.uploadImage(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    // 응답으로 받은 PNG 파일 처리
                    if (responseBody == null) {
                        Log.d("이미지 로딩", "이미지 로딩 실패");
                    } else {
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(response, context, measurementInfo);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                        callbackListener.onFinish();
                    }
                } else {
                    // 오류 처리
                    Log.d("Response 오류", "response 오류");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 통신 실패 처리
                Log.d("통신 실패", t.getMessage());
            }
        });
    }

    private Uri convertResponseToUri(Response<ResponseBody> response, Context context, MeasurementInfo measurementInfo) throws IOException {
        if (response.isSuccessful()) {
            // ResponseBody에서 이미지 데이터를 읽어옵니다.
            ResponseBody responseBody = response.body();
            InputStream inputStream = responseBody.byteStream();

            // 이미지를 기기 내부에 저장할 파일을 생성합니다.
            File tempFile = createFile(context, measurementInfo);

            // 파일에 이미지 데이터를 저장합니다.
            writeInputStreamToFile(inputStream, tempFile);

            // 파일의 경로를 Uri로 변환합니다.
            return Uri.fromFile(tempFile);
        } else {
            throw new IOException("Response was not successful: " + response.code());
        }
    }

    private File createFile(Context context, MeasurementInfo measurementInfo) throws IOException {
        // 파일을 저장할 경로 및 파일 이름 설정
        String directoryPath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        String fileName = measurementInfo.getId() + ".png";

        // 파일 생성
        File tempFile = new File(directoryPath, fileName);

        // 파일이 있을 경우 삭제
        if (tempFile.exists()) {
            tempFile.delete();
        }

        tempFile.createNewFile();

        return tempFile;
    }

    private void writeInputStreamToFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
    }

    public interface CallbackListener {
        void onFinish();
    }
}
