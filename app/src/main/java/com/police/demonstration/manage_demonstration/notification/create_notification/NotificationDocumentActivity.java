package com.police.demonstration.manage_demonstration.notification.create_notification;

import static com.police.demonstration.Constants.INTENT_NAME_ADD_TEXT_MESSAGE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.PACKAGE_NAME;
import static com.police.demonstration.Constants.SIMPLE_DATE_FORMAT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.database.notification.NotificationInfo;
import com.police.demonstration.databinding.ActivityNotificationDocumentBinding;
import com.police.demonstration.manage_demonstration.ManageDemonstrationActivity;
import com.police.demonstration.manage_demonstration.notification.record_list.RecordListActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 고지서 문서 확인 화면
 * 1. 생성된 고지서를 확인 (클릭 시 ImageViewActivity 로 전환하여 이미지 확대/축소 가능)
 * 2. '고지' 버튼 클릭하여 안드로이드 기본 메시지 앱으로 전환
 */
public class NotificationDocumentActivity extends AppCompatActivity {

    private ActivityNotificationDocumentBinding binding;
    private NotificationDocumentViewModel viewModel;

    private DemonstrationInfo demonstrationInfo;
    private MeasurementInfo measurementInfo;

    private String textMessage;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_document);
        binding.setActivity(this);

        // 시위 정보, 측정 정보, 추가 텍스트 메시지 저장
        demonstrationInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        measurementInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_MEASUREMENT);
        textMessage = getIntent().getStringExtra(INTENT_NAME_ADD_TEXT_MESSAGE);

        viewModel = new ViewModelProvider(this).get(NotificationDocumentViewModel.class);

        // 이미지 내용 변경(업로드 완료) 시 화면에 대응
        viewModel.getMeasurementList().observe(this, uri -> {
            imageUri = uri;
            loadImage(uri);
        });

        // 고지 기록 저장 완료 시 화면 종료 후 시위 관리 화면으로 이동
        viewModel.getIsFinish().observe(this, isFinish -> {
            if (isFinish) {
                // 고지 기록 저장 완료 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.complete_add_notification), Toast.LENGTH_SHORT).show();

                // 현재 Activity 종료
                finish();

                // 시위 관리 화면으로 이동 -> Activity Task 정리
                Intent intent = new Intent(this, ManageDemonstrationActivity.class);
                intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        if (measurementInfo == null || textMessage == null) {
            // 안내문 발송
            initNotificationTypeNotTextView();
            viewModel.getNotificationUriOne(this, demonstrationInfo);
        } else {
            // 고지 타입 반영
            String titleText = "";
            switch (measurementInfo.getNotificationType()) {
                case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE:
                    // 최고 소음 초과(유지) 일 때, 제목 반영, 최고소음초과(유지) 고지서 받아오기
                    titleText = getString(R.string.exceed_highest_noise) + getString(R.string.space) + getString(R.string.open_bracket) + getString(R.string.space) + getString(R.string.maintenance) + getString(R.string.space) + getString(R.string.close_bracket);
                    viewModel.getNotificationUriTwo(this, demonstrationInfo, measurementInfo);
                    break;
                case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE:
                    // 등가 소음 초과(유지) 일 때, 제목 반영, 등가소음초과(유지) 고지서 받아오기
                    titleText = getString(R.string.exceed_equivalent_noise) + getString(R.string.space) + getString(R.string.open_bracket) + getString(R.string.space) + getString(R.string.maintenance) + getString(R.string.space) + getString(R.string.close_bracket);
                    viewModel.getNotificationThree(this, demonstrationInfo, measurementInfo);
                    break;
                default:
                    break;
            }
            binding.measurementType.setText(titleText);

            // 고지 시간 반영
            binding.currentTime.setText(getCurrentTime());
        }

        initButton();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void initButton() {
        // 뒤로 가기 버튼 클릭 이벤트 -> 화면 종료
        binding.backButton.setOnClickListener(e -> finish());

        // '고지' 버튼 클릭 이벤트 -> 기본 메시지 앱으로 화면 전환
        binding.notificationButton.setOnClickListener(e -> {
            // MMS를 보내기 위한 Intent 생성
            // FileUriExposedException (Android 7.0 이상에서 발생하는 예외) 를 피하기 위하여
            // FileProvider 사용
            File file = new File(imageUri.getPath());
            Uri imageFileUri = FileProvider.getUriForFile(this, PACKAGE_NAME, file);

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

            // '고지 저장' 버튼 활성화
            binding.notificationFinishButton.setEnabled(true);
        });

        // '고지 저장' 버튼 이벤트 -> 고지 기록 추가, 고지 관리 화면으로 이동 (액티비티 종료)
        binding.notificationFinishButton.setOnClickListener(e -> {
            // 고지 기록 추가
            NotificationInfo notificationInfo = new NotificationInfo(
                    measurementInfo.getId(),
                    binding.currentTime.getText().toString(),
                    binding.measurementType.getText().toString(),
                    imageUri
            );
            viewModel.addNotification(this, notificationInfo);
        });
    }

    // 현재 시간 계산
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

    // 이미지를 ImageView 에 보여주고, 로드 성공과 실패 시 이벤트 등록
    private void loadImage(Uri uri) {
        // 이미지 로딩이 시작되기 전에 로딩 화면을 표시
        binding.progressBar.setVisibility(View.VISIBLE);

        // Glide를 사용하여 이미지를 로딩
        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // 이미지 로딩 실패 시 경고 토스트 메시지 출력 후 화면 종료
                        binding.progressBar.setVisibility(View.GONE); // 로딩 화면 숨김
                        Toast.makeText(binding.getActivity(), getString(R.string.plz_retry_connect_network), Toast.LENGTH_SHORT).show();
                        finish();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 이미지 로딩 성공 시 프로그래스바 숨김, 화면 버튼 클릭 활성화
                        binding.progressBar.setVisibility(View.GONE); // 로딩 화면 숨김
                        binding.notificationButton.setEnabled(true); // '고지' 버튼 활성화

                        // 이미지 뷰 클릭 이벤트
                        binding.notificationImage.setOnClickListener(e -> {
                            // ImageViewActivity 로 전환
                            Intent intent = new Intent(binding.getActivity(), ImageViewActivity.class);
                            intent.setData(imageUri);
                            startActivity(intent);
                        });
                        return false;
                    }
                })
                .into(binding.notificationImage);
    }
}
