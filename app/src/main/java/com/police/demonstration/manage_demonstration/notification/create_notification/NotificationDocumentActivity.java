package com.police.demonstration.manage_demonstration.notification.create_notification;

import static com.police.demonstration.Constants.INTENT_NAME_ADD_TEXT_MESSAGE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.SIMPLE_DATE_FORMAT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.databinding.ActivityNotificationBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationDocumentActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private NotificationDocumentViewModel viewModel;

    private DemonstrationInfo demonstrationInfo;
    private MeasurementInfo measurementInfo;

    private String textMessage;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        binding.setActivity(this);

        viewModel = new ViewModelProvider(this).get(NotificationDocumentViewModel.class);
        // 이미지 내용 변경 시 화면에 대응
        viewModel.getMeasurementList().observe(this, uri -> {
            Glide.with(getApplicationContext()).load(uri).into(binding.notificationImage);
            imageUri = uri;
        });

        demonstrationInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        measurementInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_MEASUREMENT);
        textMessage = getIntent().getStringExtra(INTENT_NAME_ADD_TEXT_MESSAGE);

        if (measurementInfo == null || textMessage == null) {
            // 안내문 발송
            initNotificationTypeNotTextView();
        } else {
            initTextView();

            // 이미지 가져오기
            viewModel.getNotificationUri(this, demonstrationInfo, measurementInfo);
        }

        initButton();
    }

    private void initTextView() {
        // 고지 타입 반영
        String titleText = "";
        switch (measurementInfo.getNotificationType()) {
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE:
                titleText = getString(R.string.exceed_highest_noise) + getString(R.string.space) + getString(R.string.open_bracket) + getString(R.string.space) + getString(R.string.maintenance) + getString(R.string.space) + getString(R.string.close_bracket);
                break;
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE:
                titleText = getString(R.string.exceed_equivalent_noise) + getString(R.string.space) + getString(R.string.open_bracket) + getString(R.string.space) + getString(R.string.maintenance) + getString(R.string.space) + getString(R.string.close_bracket);
                break;
            default:
                break;
        }
        binding.measurementType.setText(titleText);

        // 고지 시간 반영
        binding.currentTime.setText(getCurrentTime());
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.notificationButton.setOnClickListener(e -> {
            //@TODO 고지 버튼 클릭 이벤트 - MMS 전송
            // MMS를 보내기 위한 Intent 생성
            // FileUriExposedException (Android 7.0 이상에서 발생하는 예외) 를 피하기 위하여
            // FileProvider 사용
            File file = new File(imageUri.getPath());
            Uri imageFileUri = FileProvider.getUriForFile(this, "com.police.demonstration.fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, textMessage); // 텍스트 메시지 내용
            intent.putExtra("address", demonstrationInfo.getOrganizerPhoneNumber()); // 수신자 전화번호
            intent.setType("image/*"); // 이미지 MIME 타입
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, imageFileUri); // 이미지 파일의 경로

            // MMS 앱 실행
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

//            // 메시지 앱 실행을 위한 Intent 생성
//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("smsto:" + Uri.encode(demonstrationInfo.getOrganizerPhoneNumber())));
//
//            if (textMessage != null) {
//                // 메시지 내용 추가
//                intent.putExtra("sms_body", textMessage);
//            }
//
//            startActivity(intent);
        });
    }

    private String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Date currentDate = new Date(System.currentTimeMillis());
        String[] current = formatter.format(currentDate).split(getString(R.string.dash));

        if (current.length == 5) {
            return current[0] + getString(R.string.year) + getString(R.string.space)
                    + current[1] + getString(R.string.month) + getString(R.string.space)
                    + current[2] + getString(R.string.day_month) + getString(R.string.space)
                    + current[3] + getString(R.string.hour) + getString(R.string.space)
                    + current[4] + getString(R.string.minute);
        } else {
            return "";
        }
    }

    // 안내문 발송 TextView 초기화
    private void initNotificationTypeNotTextView() {
        // 고지 타입 변경
        binding.measurementType.setText(getString(R.string.send_notice));

        // 고지 시간 반영
        binding.currentTime.setText(getCurrentTime());
    }
}
