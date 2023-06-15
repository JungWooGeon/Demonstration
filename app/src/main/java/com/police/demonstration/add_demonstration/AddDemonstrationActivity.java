package com.police.demonstration.add_demonstration;

import static com.police.demonstration.Constants.DATE_DETAIL_END_DATE_IDX;
import static com.police.demonstration.Constants.DATE_DETAIL_START_DATE_IDX;
import static com.police.demonstration.Constants.INTENT_NAME_BACKGROUND_NOISE_LEVEL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_DATE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_PLACE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_END_DATE;
import static com.police.demonstration.Constants.INTENT_NAME_END_YEAR;
import static com.police.demonstration.Constants.INTENT_NAME_GROUP_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_NAME_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_NAME;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_POSITION;
import static com.police.demonstration.Constants.INTENT_NAME_PHONE_NUMBER_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_PLACE_ZONE_IDX;
import static com.police.demonstration.Constants.INTENT_NAME_POSITION_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_START_DATE;
import static com.police.demonstration.Constants.INTENT_NAME_START_YEAR;
import static com.police.demonstration.Constants.INTENT_NAME_TIMEZONE_IDX;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_HOME;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.SIMPLE_DATE_FORMAT;
import static com.police.demonstration.Constants.TIME_ZONE_DAY;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;
import static com.police.demonstration.Constants.YEAR_DATE_FORMAT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityAddDemonstrationBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 시위 추가 화면
 * 1. 추가할 시위 정보 입력
 * 2. 연락망 정보 추가는 '연락망 추가하기' 화면으로 전환하여 진행
 * 3. 정보를 모두 입력한 후 setResult(intent) 사용하여 이전 화면으로 정보 전달
 */
public class AddDemonstrationActivity extends AppCompatActivity {

    final int START_DATE_IDX = 0;
    final int END_DATE_IDX = 1;

    private ActivityAddDemonstrationBinding binding;

    // '주간 / 야간 / 심야' 에서 현재 선택되어 있는 Index (순서대로 0, 1, 2 로 저장)
    private int timeZoneIdx = 0;
    // '주거지역, 학교 / 공공도서관 / 그 밖' 에서 현재 선택되어 있는 Index (순서대로 0, 1, 2 로 저장)
    private int placeZoneIdx = 0;

    private String startYear = "";
    private String endYear = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_demonstration);
        binding.setActivity(this);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(YEAR_DATE_FORMAT);
        String nowYear = formatter.format(new Date(System.currentTimeMillis()));
        startYear = nowYear;
        endYear = nowYear;

        initButton();
        initTextView();
    }

    private void initButton() {
        // '시위 측 연락망' 버튼 클릭 시 '연락망 추가하기' 로 화면 전환 (registerForResultActivity)
        binding.phoneNumberButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            launcher.launch(intent);
        });

        // 뒤로가기 버튼 클릭 시 화면 종료
        binding.backButton.setOnClickListener(e -> finish());

        // '생성' 버튼 클릭 이벤트
        binding.addButton.setOnClickListener(e -> {
            // 시작 날짜, 종료 날짜 구하기
            String[] startEndDate = getStartEndDate(String.valueOf(binding.demonstrationDateDetail.getText()), startYear, endYear);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

            Date startDate;
            Date endDate;
            try {
                startDate = formatter.parse(startEndDate[START_DATE_IDX]);
                endDate = formatter.parse(startEndDate[END_DATE_IDX]);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            if ((startDate != null ? startDate.compareTo(endDate) : 0) < 0) {
                // 종료 날짜가 시작 날짜보다 작을 경우 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_reset_date), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.demonstrateNameEditText.getText()).toString().equals("")) {
                // '시위 명칭' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_demonstration_name), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.groupNameEditText.getText()).toString().equals("")) {
                // '시위 단체명' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_group_name), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.demonstrationPlaceDetail.getText()).toString().equals("")) {
                // '시위 개최 장소' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_place), Toast.LENGTH_SHORT).show();
            } else if (binding.nameDetail.getText().toString().equals(getString(R.string.example_organizer))) {
                // '주최자' 미입력 시 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_organizer), Toast.LENGTH_SHORT).show();
            } else {
                // 입력된 정보들을 모두 intent 에 put 하고, setResult 를 사용하여 이전 화면으로 정보 전달 후 화면 종료
                Intent intent = new Intent();
                intent.putExtra(INTENT_NAME_START_DATE, startEndDate[START_DATE_IDX]);
                intent.putExtra(INTENT_NAME_END_DATE, startEndDate[END_DATE_IDX]);
                intent.putExtra(INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT, String.valueOf(binding.demonstrateNameEditText.getText()));
                intent.putExtra(INTENT_NAME_GROUP_NAME_EDITTEXT, String.valueOf(binding.groupNameEditText.getText()));
                intent.putExtra(INTENT_NAME_TIMEZONE_IDX, String.valueOf(timeZoneIdx));
                intent.putExtra(INTENT_NAME_DEMONSTRATION_PLACE_DETAIL, String.valueOf(binding.demonstrationPlaceDetail.getText()));
                intent.putExtra(INTENT_NAME_PLACE_ZONE_IDX, String.valueOf(placeZoneIdx));
                intent.putExtra(INTENT_NAME_ORGANIZER_NAME, String.valueOf(binding.nameDetail.getText()));
                intent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, String.valueOf(binding.phoneNumberDetail.getText()));
                intent.putExtra(INTENT_NAME_ORGANIZER_POSITION, String.valueOf(binding.positionDetail.getText()));
                intent.putExtra(INTENT_NAME_BACKGROUND_NOISE_LEVEL, String.valueOf(binding.backgroundNoiseLevelDetail.getText()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // registerForActivityResult call back 설정 (연락망 추가하기 화면에서 ok 사인이 나올 경우 화면에 연락망 데이터 반영)
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            Intent intent = data.getData();
            assert intent != null;

            binding.nameDetail.setText(intent.getStringExtra(INTENT_NAME_NAME_DETAIL));
            binding.phoneNumberDetail.setText(intent.getStringExtra(INTENT_NAME_PHONE_NUMBER_DETAIL));
            binding.positionDetail.setText(intent.getStringExtra(INTENT_NAME_POSITION_DETAIL));
        }
    });

    private void initTextView() {
        // '주간, 야간, 심야' textview click 설정 관리 (radio 버튼 효과)
        binding.timeZoneDay.setOnClickListener(e -> setDemonstrationDate(TIME_ZONE_DAY));
        binding.timeZoneNight.setOnClickListener(e -> setDemonstrationDate(TIME_ZONE_NIGHT));
        binding.timeZoneLateNight.setOnClickListener(e -> setDemonstrationDate(TIME_ZONE_LATE_NIGHT));

        // '주거지역, 학교 / 공공도서관 / 그 밖' textview click 설정 관리 (radio 버튼 효과)
        binding.placeZoneHome.setOnClickListener(e -> setDemonstrationOrganizer(PLACE_ZONE_HOME));
        binding.placeZonePublic.setOnClickListener(e -> setDemonstrationOrganizer(PLACE_ZONE_PUBLIC));
        binding.placeZoneEtc.setOnClickListener(e -> setDemonstrationOrganizer(PLACE_ZONE_ETC));

        // 시위 개최 일시 textview click event -> Date Picker 와 Time Picker 를 사용하여 입력
        binding.demonstrationDateDetail.setOnClickListener(e -> {
            Calendar calendar = Calendar.getInstance();

            // 마침 시간 입력 TimePicker
            TimePickerDialog endTimePickerDialog = new TimePickerDialog(AddDemonstrationActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + selectedHour + getString(R.string.hour) + getString(R.string.space) + selectedMinute + getString(R.string.minute);
                binding.demonstrationDateDetail.setText(text);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            endTimePickerDialog.setTitle(getString(R.string.input_end_time));
            endTimePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + getString(R.string.example_time);
                binding.demonstrationDateDetail.setText(text);
            });
            endTimePickerDialog.show();

            // 마침 날짜 입력 DatePicker
            DatePickerDialog endDatePickerDialog = new DatePickerDialog(AddDemonstrationActivity.this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                // 0월부터 시작하여 +1 숫자 조정
                String month = Integer.toString(selectedMonth + 1);
                String text = binding.demonstrationDateDetail.getText().toString() + month + getString(R.string.month) + getString(R.string.space) + selectedDay + getString(R.string.day_month) + getString(R.string.space);
                binding.demonstrationDateDetail.setText(text);

                endYear = String.valueOf(selectedYear);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            endDatePickerDialog.setTitle(getString(R.string.input_end_date));
            endDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + getString(R.string.example_date) + getString(R.string.space);
                binding.demonstrationDateDetail.setText(text);
            });
            endDatePickerDialog.show();

            // 시작 시간 입력 TimePicker
            TimePickerDialog startTimePickerDialog = new TimePickerDialog(AddDemonstrationActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + selectedHour + getString(R.string.hour) + getString(R.string.space) + selectedMinute + getString(R.string.minute) + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space);
                binding.demonstrationDateDetail.setText(text);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            startTimePickerDialog.setTitle(getString(R.string.input_start_time));
            startTimePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + getString(R.string.example_time) + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space);
                binding.demonstrationDateDetail.setText(text);
            });
            startTimePickerDialog.show();

            // 시작 날짜 입력 DatePicker
            DatePickerDialog startDatePickerDialog = new DatePickerDialog(AddDemonstrationActivity.this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                // 0월부터 시작하여 +1 숫자 조정
                String month = Integer.toString(selectedMonth + 1);
                String text = month + getString(R.string.month) + getString(R.string.space) + selectedDay + getString(R.string.day_month) + getString(R.string.space);
                binding.demonstrationDateDetail.setText(text);

                startYear = String.valueOf(selectedYear);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            startDatePickerDialog.setTitle(getString(R.string.input_start_date));
            startDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
                String text = getString(R.string.example_date) + getString(R.string.space);
                binding.demonstrationDateDetail.setText(text);
            });
            startDatePickerDialog.show();

            binding.demonstrationDateDetail.setTextColor(getColor(R.color.contents));
        });
    }

    // "시위 개최 일시" -> '주간' - '야간' - '심야' click event
    private void setDemonstrationDate(int idx) {
        switch (timeZoneIdx) {
            case TIME_ZONE_DAY:
                binding.timeZoneDay.setTextColor(getColor(R.color.contents_light));
                break;
            case TIME_ZONE_NIGHT:
                binding.timeZoneNight.setTextColor(getColor(R.color.contents_light));
                break;
            case TIME_ZONE_LATE_NIGHT:
                binding.timeZoneLateNight.setTextColor(getColor(R.color.contents_light));
                break;
            default:
                break;
        }

        switch (idx) {
            case TIME_ZONE_DAY:
                binding.timeZoneDay.setTextColor(getColor(R.color.contents));
                break;
            case TIME_ZONE_NIGHT:
                binding.timeZoneNight.setTextColor(getColor(R.color.contents));
                break;
            case TIME_ZONE_LATE_NIGHT:
                binding.timeZoneLateNight.setTextColor(getColor(R.color.contents));
                break;
            default:
                break;
        }

        timeZoneIdx = idx;
    }

    // "시위 개최 장소" -> '주거지역, 학교' - '공공도서관' - '그 밖' click event
    private void setDemonstrationOrganizer(int idx) {
        switch (placeZoneIdx) {
            case PLACE_ZONE_HOME:
                binding.placeZoneHome.setTextColor(getColor(R.color.contents_light));
                break;
            case PLACE_ZONE_PUBLIC:
                binding.placeZonePublic.setTextColor(getColor(R.color.contents_light));
                break;
            case TIME_ZONE_LATE_NIGHT:
                binding.placeZoneEtc.setTextColor(getColor(R.color.contents_light));
                break;
            default:
                break;
        }

        switch (idx) {
            case PLACE_ZONE_HOME:
                binding.placeZoneHome.setTextColor(getColor(R.color.contents));
                break;
            case PLACE_ZONE_PUBLIC:
                binding.placeZonePublic.setTextColor(getColor(R.color.contents));
                break;
            case TIME_ZONE_LATE_NIGHT:
                binding.placeZoneEtc.setTextColor(getColor(R.color.contents));
                break;
            default:
                break;
        }

        placeZoneIdx = idx;
    }

    // 시작 날짜와 마침 날짜 정제 ( [시작 날짜, 마침 날짜] )
    private String[] getStartEndDate(String stringDateDetail, String startYear, String endYear) {
        String[] dateDetail = stringDateDetail.split(getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space));

        String[] startDetail = dateDetail[DATE_DETAIL_START_DATE_IDX].split(getString(R.string.split_space));
        String[] endDetail = dateDetail[DATE_DETAIL_END_DATE_IDX].split(getString(R.string.split_space));

        String startMonth = startDetail[0].split(getString(R.string.month))[0];
        String startDay = startDetail[1].split(getString(R.string.day_month))[0];
        String startHour = startDetail[2].split(getString(R.string.hour))[0];
        String startMinute = startDetail[3].split(getString(R.string.minute))[0];

        String endMonth = endDetail[0].split(getString(R.string.month))[0];
        String endDay = endDetail[1].split(getString(R.string.day_month))[0];
        String endHour = endDetail[2].split(getString(R.string.hour))[0];
        String endMinute = endDetail[3].split(getString(R.string.minute))[0];

        String date1 = startYear + getString(R.string.dash) + startMonth + getString(R.string.dash) + startDay + getString(R.string.dash) + startHour + getString(R.string.dash) + startMinute;
        String date2 = endYear + getString(R.string.dash) + endMonth + getString(R.string.dash) + endDay + getString(R.string.dash) + endHour + getString(R.string.dash) + endMinute;

        return new String[]{date1, date2};
    }
}
