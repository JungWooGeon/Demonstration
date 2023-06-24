package com.police.demonstration.manage_demonstration.notification.create_notification;

import static com.police.demonstration.Constants.INTENT_NAME_ADD_TEXT_MESSAGE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_HOME;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.TIME_ZONE_DAY;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.databinding.ActivityCreateNotificationBinding;

public class CreateNotificationActivity extends AppCompatActivity {

    ActivityCreateNotificationBinding binding;

    private DemonstrationInfo demonstrationInfo;
    private MeasurementInfo measurementInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_notification);
        binding.setActivity(this);

        demonstrationInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        measurementInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_MEASUREMENT);

        initTextView();
        initButton();
    }

    private void initTextView() {
        // 측정 시간, 측정 위치, 상세 주소 반영
        binding.measurementStartTime.setText(measurementInfo.getStartTime());
        binding.measurementEndTime.setText(measurementInfo.getEndTime());
        binding.measurementPlaceDetail.setText(measurementInfo.getPlace());
        binding.detailAddressDetail.setText(measurementInfo.getDetailPlace());

        // 대상지역 반영
        String placeZoneText = "";
        switch (demonstrationInfo.getPlaceZone()) {
            case PLACE_ZONE_HOME:
                placeZoneText = getString(R.string.example_place_zone_home);
                break;
            case PLACE_ZONE_PUBLIC:
                placeZoneText = getString(R.string.example_place_zone_public);
                break;
            case PLACE_ZONE_ETC:
                placeZoneText = getString(R.string.example_place_zone_etc);
                break;
            default:
                break;
        }
        binding.placeZoneDetail.setText(placeZoneText);

        // 시간대 반영
        String timeZoneText = "";
        switch (demonstrationInfo.getTimeZone()) {
            case TIME_ZONE_DAY:
                timeZoneText = getString(R.string.day);
                break;
            case TIME_ZONE_NIGHT:
                timeZoneText = getString(R.string.night);
                break;
            case TIME_ZONE_LATE_NIGHT:
                timeZoneText = getString(R.string.late_night);
                break;
            default:
                break;
        }
        binding.timeZoneDetail.setText(timeZoneText);

        // 측정거리 반영
        String distanceText = measurementInfo.getDistance() + getString(R.string.space) + getString(R.string.meter);
        binding.distanceDetail.setText(distanceText);

        // 풍속 반영
        String windSpeedText = measurementInfo.getWinterSpeed() + getString(R.string.space) + getString(R.string.meter_per_second);
        binding.windSpeedDetail.setText(windSpeedText);

        // 최고 소음, 등가 소음에 따라 제목, 측정 소음, 기준 소음 반영
        String titleText = "";
        String measurementNoiseText = "";
        String measurementNoise = "";
        String standardNoiseText = "";
        String standardNoise = "";
        switch (measurementInfo.getNotificationType()) {
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE:
                titleText = getString(R.string.exceed_highest_noise);
                measurementNoiseText = getString(R.string.measurement_highest_noise);
                measurementNoise = measurementInfo.getCorrectionHighest();
                standardNoiseText = getString(R.string.standard_highest_noise);
                standardNoise = demonstrationInfo.getStandardHighest();
                break;
            case NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE:
                titleText = getString(R.string.exceed_equivalent_noise);
                measurementNoiseText = getString(R.string.measurement_equivalent_noise);
                measurementNoise = measurementInfo.getCorrectionEquivalent();
                standardNoiseText = getString(R.string.standard_equivalent_noise);
                standardNoise = demonstrationInfo.getStandardEquivalent();
                break;
            default:
                break;
        }
        measurementNoise += getString(R.string.space) + getString(R.string.decibel);
        standardNoise += getString(R.string.space) + getString(R.string.decibel);

        binding.title.setText(titleText);
        binding.measurementNoise.setText(measurementNoiseText);
        binding.measurementNoiseDetail.setText(measurementNoise);
        binding.standardNoise.setText(standardNoiseText);
        binding.standardNoiseDetail.setText(standardNoise);

        // 텍스트 추가 기능 -> 텍스트 저장 화면으로 전환
        binding.addTextTextView.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddTextMessageActivity.class);
            addTextMessageLauncher.launch(intent);
        });
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());

        binding.createNotificationButton.setOnClickListener(e -> {

        });
    }

    // registerForActivityResult call back 설정 (배경 소음도 추가 화면에서 ok 사인이 나올 경우 배경 소음도 데이터 추가)
    ActivityResultLauncher<Intent> addTextMessageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            // RESULT_OK
            Intent intent = data.getData();
            assert intent != null;

            binding.addTextTextView.setText(intent.getStringExtra(INTENT_NAME_ADD_TEXT_MESSAGE));
        }
    });
}
