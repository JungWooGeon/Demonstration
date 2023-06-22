package com.police.demonstration.manage_demonstration.measurement.add_measurement;

import static com.police.demonstration.Constants.INTENT_NAME_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.INTENT_NAME_HIGHEST_NOISE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_NOT;
import static com.police.demonstration.Constants.STANDARD_CORRECTION_NOISE;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.databinding.ActivityAddMeasurementBinding;

import java.util.Calendar;
import java.util.Objects;

/**
 * 측정 입력 화면
 * 1. 기준 소음도를 보여줌
 * 2. 측정값 입력에 따라 보정치를 적용하여 보여줌
 * 3. 상세 측정 정보들 입력 (Timepicker, EditText)
 * 4. 입력된 정보들을 setResult() 를 통해 전달
 */
public class AddMeasurementActivity extends AppCompatActivity {

    private ActivityAddMeasurementBinding binding;

    private DemonstrationInfo demonstrationInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_measurement);
        binding.setActivity(this);

        // Intent 로 전달 받은 시위 정보를 저장
        demonstrationInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);

        initTextView();
        initEditText();
        initButton();
    }

    private void initTextView() {
        // TimePicker 에서 사용할 Calendar
        Calendar calendar = Calendar.getInstance();

        binding.measurementStartTime.setOnClickListener(e -> {
            // 시작 시간 입력 TimePicker
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeasurementActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                // 시작 시간 입력 시 시작 시간 textView 에 반영
                binding.measurementStartTime.setText(getTimeText(selectedHour, selectedMinute));

                // 시작 시간 입력 시 마침 시간 textView 에 10분 추가 하여 반영
                binding.measurementEndTime.setText(getTimeText(selectedHour, selectedMinute + 10));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(getString(R.string.input_start_time));
            timePickerDialog.show();
        });

        binding.measurementEndTime.setOnClickListener(e -> {
            // 마침 시간 입력 TimePicker
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeasurementActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                // 마침 시간 입력 시 마침 시간 textView 에 반영
                binding.measurementEndTime.setText(getTimeText(selectedHour, selectedMinute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(getString(R.string.input_end_time));
            timePickerDialog.show();
        });
    }

    private void initEditText() {
        // 기준 소음도 등가 소음 표시 (Intent 로 받아온 기준 등가 소음 표시)
        String standardNoiseEquivalent = getIntent().getStringExtra(INTENT_NAME_EQUIVALENT_NOISE);
        binding.standardNoiseEquivalentDetail.setText(standardNoiseEquivalent);

        // 기준 소음도 최고 소음 표시 (Intent 로 받아온 기준 최고 소음 표시)
        String standardNoiseHighest = getIntent().getStringExtra(INTENT_NAME_HIGHEST_NOISE);
        binding.standardNoiseHighestDetail.setText(standardNoiseHighest);

        // 측정값 입력 등가 소음 EditText 입력 이벤트 (보정치 적용값에 소음도 계산 후 표시 하기)
        binding.inputMeasurementEquivalentDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Objects.requireNonNull(binding.inputMeasurementEquivalentDetail.getText()).toString().equals("")) {
                    // EditText 에 입력한 값이 있을 경우 보정치 적용
                    String text = getTargetNoise(Double.parseDouble(Objects.requireNonNull(binding.inputMeasurementEquivalentDetail.getText()).toString()))
                            + getString(R.string.space) + getString(R.string.decibel);
                    binding.correctionValueEquivalentDetail.setText(text);
                }
            }
        });

        // 측정값 입력 최고 소음 EditText 입력 이벤트 (보정치 적용값에 소음도 계산 후 표시 하기)
        binding.inputMeasurementHighestDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Objects.requireNonNull(binding.inputMeasurementHighestDetail.getText()).toString().equals("")) {
                    // EditText 에 입력한 값이 있을 경우 보정치 적용
                    String text = getTargetNoise(Double.parseDouble(Objects.requireNonNull(binding.inputMeasurementHighestDetail.getText()).toString()))
                            + getString(R.string.space) + getString(R.string.decibel);
                    binding.correctionValueHighestDetail.setText(text);
                }
            }
        });
    }

    private void initButton() {
        // 뒤로 가기 버튼 클릭 이벤트 (종료)
        binding.backButton.setOnClickListener(e -> finish());

        // 초기화 버튼 클릭 이벤트
        binding.resetButton.setOnClickListener(e -> {
            binding.measurementStartTime.setText(getString(R.string.measurement_time_example));
            binding.measurementEndTime.setText(getString(R.string.measurement_time_example));
            binding.measurementPlaceDetail.setText("");
            binding.detailAddressDetail.setText("");
            binding.measurementDistanceDetail.setText("");
            binding.windSpeedDetail.setText("");
            binding.inputMeasurementEquivalentDetail.setText("");
            binding.inputMeasurementHighestDetail.setText("");
            binding.correctionValueEquivalentDetail.setText(getString(R.string.example_noise));
            binding.correctionValueHighestDetail.setText(getString(R.string.example_noise));
        });

        // 기록 버튼 클릭 이벤트
        binding.recordButton.setOnClickListener(e -> {
            if (binding.measurementStartTime.getText().toString().equals(getString(R.string.measurement_time_example))) {
                // '시작 시간' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_start_time), Toast.LENGTH_SHORT).show();
            } else if (binding.measurementEndTime.getText().toString().equals(getString(R.string.measurement_time_example))) {
                // '마침 시간' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_end_time), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.measurementPlaceDetail.getText()).toString().equals("")) {
                // '측정 위치' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_measurement_place), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.detailAddressDetail.getText()).toString().equals("")) {
                // '상세 주소' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_measurement_place_detail), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.measurementDistanceDetail.getText()).toString().equals("")) {
                // '측정 거리' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_measurement_distance), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.windSpeedDetail.getText()).toString().equals("")) {
                // '풍속' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_measurement_wind_speed), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.inputMeasurementEquivalentDetail.getText()).toString().equals("") ||
                    Objects.requireNonNull(binding.inputMeasurementHighestDetail.getText()).toString().equals("")) {
                // '측정값' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_measurement), Toast.LENGTH_SHORT).show();
            } else {
                // 입력된 정보들 (측정 정보) 을 setResult 를 사용하여 이전 화면으로 정보 전달 후 화면 종료
                String correctionEquivalent = binding.correctionValueEquivalentDetail.getText().toString().split(getString(R.string.split_space))[0];
                String correctionHighest = binding.correctionValueHighestDetail.getText().toString().split(getString(R.string.split_space))[0];
                // 위반 사항 없음
                int notificationType = NOTIFICATION_TYPE_NOT;

                if (Integer.parseInt(correctionEquivalent) > Integer.parseInt(demonstrationInfo.getStandardEquivalent())) {
                    // 등가 소음 초과
                    notificationType = NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
                } else if (Integer.parseInt(correctionHighest) > Integer.parseInt(demonstrationInfo.getStandardHighest())) {
                    // 최고 소음 초과
                    notificationType = NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
                }

                Intent intent = new Intent();
                MeasurementInfo measurementInfo = new MeasurementInfo(
                        demonstrationInfo.getId(),
                        binding.measurementStartTime.getText().toString(),
                        binding.measurementEndTime.getText().toString(),
                        Objects.requireNonNull(binding.measurementPlaceDetail.getText()).toString(),
                        Objects.requireNonNull(binding.detailAddressDetail.getText()).toString(),
                        Objects.requireNonNull(binding.measurementDistanceDetail.getText()).toString(),
                        Objects.requireNonNull(binding.windSpeedDetail.getText()).toString(),
                        Objects.requireNonNull(binding.inputMeasurementEquivalentDetail.getText()).toString(),
                        Objects.requireNonNull(binding.inputMeasurementHighestDetail.getText()).toString(),
                        correctionEquivalent, correctionHighest, notificationType
                );
                intent.putExtra(INTENT_NAME_PARCELABLE_MEASUREMENT, measurementInfo);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // 배경 소음도와 입력한 소음도를 기준으로 계산한 보정치 값 return
    private String getTargetNoise(double noise) {
        double backgroundNoise = Double.parseDouble(demonstrationInfo.getBackgroundNoiseLevel());

        // 소음도의 차이는 소수 첫째 자리 까지 표시
        double differenceNoise = Math.round((noise - backgroundNoise) * 10) / 10.0;

        if (differenceNoise < 3.0) {
            // 배경 소음도와 입력한 소음도의 차이가 3.0 미만일 경우 대상 소음도 0
            noise = 0;
        } else if (differenceNoise <= 9.9) {
            // 배경 소음도와 입력한 소음도의 차이가 3 dB 이상 10 dB 미만일 경우 보정
            Double correction = STANDARD_CORRECTION_NOISE.get(differenceNoise);
            assert correction != null;

            double correctionNoise = correction;
            noise += correctionNoise;
        }

        return String.valueOf(Math.round(noise));
    }

    private String getTimeText(int selectedHour, int selectedMinute) {
        // 1. 분이 60 이상일 경우에 시간 계산
        // 2. 시 혹은 분이 10 미만일 경우에 앞에 "0" 을 붙여 "01" 과 같은 String 으로 변환
        // 3. '[시간] + " : " + [분]' 의 형태로 String 값을 반환

        int minute = selectedMinute % 60;
        int hour = (selectedHour + (int) (selectedMinute / 60)) % 24;

        String h = String.valueOf(hour);
        String m = String.valueOf(minute);

        if (minute < 10) {
            m = getString(R.string.number_zero) + minute;
        }

        if (hour < 10) {
            h = getString(R.string.number_zero) + hour;
        }

        return h + getString(R.string.space) + getString(R.string.colon) + getString(R.string.space) + m;
    }
}
