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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NotificationDocumentViewModel Model
 * Rxjava - Retrofit 사용하여 고지서 URI 관리
 */
public class NotificationDocumentModel {

    // 고지서 URI
    private Uri notificationUri;
    private CallbackListener callbackListener;

    public Uri getNotificationUri() {
        return notificationUri;
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    // 안내문 발송 : rxjava3 - retrofit2 -> POST 요청 보내고 응답 받기
    public void getNotificationUriOne(Context context, DemonstrationInfo demonstrationInfo) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_NOTIFICATION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // API 파라미터에 필요한 인자들 사용하여 생성
        NotificationRequest request = new NotificationRequest(demonstrationInfo.getOrganizerName());

        // RxJava
        notificationApi.getNotificationImageOne(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context, -1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        callbackListener.onFinish();
                    }
                });
    }

    // 최고 소음 초과 (유지) : rxjava3 - retrofit2 -> POST 요청 보내고 응답 받기
    public void getNotificationUriTwo(Context context, DemonstrationInfo demonstrationInfo, MeasurementInfo measurementInfo) {
        // 시간대, 대상 지역 추출
        String timeZone = getTimeZone(context, demonstrationInfo.getTimeZone());
        String placeZone = getPlaceZone(context, demonstrationInfo.getPlaceZone());

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_NOTIFICATION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // API 파라미터에 필요한 인자들 사용하여 생성
        NotificationRequest request = new NotificationRequest(
                measurementInfo.getStartTime() + context.getString(R.string.space) + context.getString(R.string.tilde) + context.getString(R.string.space) + measurementInfo.getEndTime(),
                timeZone,
                measurementInfo.getPlace() + context.getString(R.string.space) + measurementInfo.getDetailPlace(),
                measurementInfo.getDistance(),
                placeZone, measurementInfo.getWinterSpeed(),
                demonstrationInfo.getStandardHighest(),
                measurementInfo.getCorrectionHighest(), demonstrationInfo.getOrganizerName()
        );

        // Rxjava
        notificationApi.getNotificationImageTwo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context, measurementInfo.getId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        callbackListener.onFinish();
                    }
                });
    }

    // 등가 소음 초과 (유지) : rxjava3 - retrofit2 -> POST 요청 보내고 응답 받기
    public void getNotificationThree(Context context, DemonstrationInfo demonstrationInfo, MeasurementInfo measurementInfo) {
        // 시간대, 대상 지역 추출
        String timeZone = getTimeZone(context, demonstrationInfo.getTimeZone());
        String placeZone = getPlaceZone(context, demonstrationInfo.getPlaceZone());

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_NOTIFICATION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // API 파라미터에 필요한 인자들 사용하여 생성
        NotificationRequest request = new NotificationRequest(
                measurementInfo.getStartTime() + context.getString(R.string.space) + context.getString(R.string.tilde) + context.getString(R.string.space) + measurementInfo.getEndTime(),
                timeZone,
                measurementInfo.getPlace() + context.getString(R.string.space) + measurementInfo.getDetailPlace(),
                measurementInfo.getDistance(),
                placeZone, measurementInfo.getWinterSpeed(),
                demonstrationInfo.getStandardHighest(),
                measurementInfo.getCorrectionHighest(), demonstrationInfo.getOrganizerName()
        );

        // Rxjava
        notificationApi.getNotificationImageThree(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context, measurementInfo.getId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        callbackListener.onFinish();
                    }
                });
    }

    // ResponseBody Type 에서 Uri 로 변환 후 return
    private Uri convertResponseToUri(ResponseBody responseBody, Context context, int measurementInfoId) throws IOException {
        // ResponseBody에서 이미지 데이터를 읽기
        InputStream inputStream = responseBody.byteStream();

        // 이미지를 기기 내부에 저장할 파일을 생성
        File tempFile = createFile(context, measurementInfoId);

        // 파일에 이미지 데이터를 저장
        writeInputStreamToFile(inputStream, tempFile);

        // 파일의 경로를 Uri로 변환
        return Uri.fromFile(tempFile);
    }

    private File createFile(Context context, int measurementInfoId) throws IOException {
        // 파일을 저장할 경로 및 파일 이름 설정 (file 이름은 측정 정보 ID 로 설정)
        String directoryPath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        String fileName = measurementInfoId + ".png";

        // 파일 생성
        File tempFile = new File(directoryPath, fileName);

        // 기존에 파일이 있을 경우 삭제
        if (tempFile.exists()) {
            tempFile.delete();
        }

        tempFile.createNewFile();

        return tempFile;
    }

    // 파일 쓰기 - 이미지 데이터 저장 (png)
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

    // 시간대 추출하여 return
    private String getTimeZone(Context context, int timeZoneId) {
        String timeZone = "";
        switch (timeZoneId) {
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

        return timeZone;
    }

    // 대상 지역 추출하여 return
    private String getPlaceZone(Context context, int placeZoneId) {
        String placeZone = "";
        switch (placeZoneId) {
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

        return placeZone;
    }

    // 비동기 작업 완료를 알리는 listener 역할
    public interface CallbackListener {
        void onFinish();
    }
}
