package com.police.demonstration.add_demonstration;

import static com.police.demonstration.Constants.INTENT_NAME_BACKGROUND_NOISE_LEVEL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_DATE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_PLACE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_GROUP_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_NAME_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_NAME;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_POSITION;
import static com.police.demonstration.Constants.INTENT_NAME_PHONE_NUMBER_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_PLACE_ZONE_IDX;
import static com.police.demonstration.Constants.INTENT_NAME_POSITION_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_TIMEZONE_IDX;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_HOME;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.TIME_ZONE_DAY;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityAddDemonstrationBinding;

import java.util.Calendar;

public class AddDemonstrationActivity extends AppCompatActivity {

    private ActivityAddDemonstrationBinding binding;

    private int timeZoneIdx = 0;
    private int placeZoneIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_demonstration);
        binding.setActivity(this);

        initButton();
        initTextView();
        initEditText();
    }

    private void initButton() {
        binding.phoneNumberButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            launcher.launch(intent);
        });

        binding.backButton.setOnClickListener(e -> finish());

        binding.addButton.setOnClickListener(e -> {
            Intent intent = new Intent();
            intent.putExtra(INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT, String.valueOf(binding.demonstrateNameEditText.getText()));
            intent.putExtra(INTENT_NAME_GROUP_NAME_EDITTEXT, String.valueOf(binding.groupNameEditText.getText()));
            intent.putExtra(INTENT_NAME_DEMONSTRATION_DATE_DETAIL, String.valueOf(binding.demonstrationDateDetail.getText()));
            intent.putExtra(INTENT_NAME_TIMEZONE_IDX, timeZoneIdx);
            intent.putExtra(INTENT_NAME_DEMONSTRATION_PLACE_DETAIL, String.valueOf(binding.demonstrationPlaceDetail.getText()));
            intent.putExtra(INTENT_NAME_PLACE_ZONE_IDX, placeZoneIdx);
            intent.putExtra(INTENT_NAME_ORGANIZER_NAME, String.valueOf(binding.nameDetail.getText()));
            intent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, String.valueOf(binding.phoneNumberDetail.getText()));
            intent.putExtra(INTENT_NAME_ORGANIZER_POSITION, String.valueOf(binding.positionDetail.getText()));
            intent.putExtra(INTENT_NAME_BACKGROUND_NOISE_LEVEL, String.valueOf(binding.backgroundNoiseLevelDetail.getText()));
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    // registerForActivityResult call back
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
        binding.timeZoneDay.setOnClickListener(e -> setDemonstrationDate(TIME_ZONE_DAY));
        binding.timeZoneNight.setOnClickListener(e -> setDemonstrationDate(TIME_ZONE_NIGHT));
        binding.timeZoneLateNight.setOnClickListener(e -> setDemonstrationDate(TIME_ZONE_LATE_NIGHT));

        binding.placeZoneHome.setOnClickListener(e -> setDemonstrationOrganizer(PLACE_ZONE_HOME));
        binding.placeZonePublic.setOnClickListener(e -> setDemonstrationOrganizer(PLACE_ZONE_PUBLIC));
        binding.placeZoneEtc.setOnClickListener(e -> setDemonstrationOrganizer(PLACE_ZONE_ETC));
    }

    // "시위 개최 일시" -> '주간' - '야간' - '심야' 클릭 설정 이벤트
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

    // "시위 개최 장소" -> '주거지역, 학교' - '공공도서관' - '그 밖' 클릭 설정 이벤트
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

    private void initEditText() {
        binding.demonstrationDateDetail.setOnClickListener(e -> {
            Calendar calendar = Calendar.getInstance();

            TimePickerDialog endTimePickerDialog = new TimePickerDialog(AddDemonstrationActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + selectedHour + "시" + " " + selectedMinute + "분";
                binding.demonstrationDateDetail.setText(text);
            }, calendar.get(Calendar.HOUR_OF_DAY) + 9, calendar.get(Calendar.MINUTE), true);
            endTimePickerDialog.setTitle("마침 시간 입력");
            endTimePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", (dialog, which) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + "0시 0분";
                binding.demonstrationDateDetail.setText(text);
            });
            endTimePickerDialog.show();

            DatePickerDialog endDatePickerDialog = new DatePickerDialog(AddDemonstrationActivity.this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                String month = Integer.toString(selectedMonth + 1);
                String text = binding.demonstrationDateDetail.getText().toString() + month + "월" + " " + selectedDay + "일" + " ";
                binding.demonstrationDateDetail.setText(text);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            endDatePickerDialog.setTitle("마침 날짜 입력");
            endDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "취소", (dialog, which) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + "1월 1일 ";
                binding.demonstrationDateDetail.setText(text);
            });
            endDatePickerDialog.show();

            TimePickerDialog startTimePickerDialog = new TimePickerDialog(AddDemonstrationActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + selectedHour + "시" + " " + selectedMinute + "분" + " " + "~" + " ";
                binding.demonstrationDateDetail.setText(text);
            }, calendar.get(Calendar.HOUR_OF_DAY) + 9, calendar.get(Calendar.MINUTE), true);
            startTimePickerDialog.setTitle("시작 시간 입력");
            startTimePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "취소", (dialog, which) -> {
                String text = binding.demonstrationDateDetail.getText().toString() + "0시 0분 ~ ";
                binding.demonstrationDateDetail.setText(text);
            });
            startTimePickerDialog.show();

            DatePickerDialog startDatePickerDialog = new DatePickerDialog(AddDemonstrationActivity.this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                String month = Integer.toString(selectedMonth + 1);
                String text = month + "월" + " " + selectedDay + "일" + " ";
                binding.demonstrationDateDetail.setText(text);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            startDatePickerDialog.setTitle("시작 날짜 입력");
            startDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "취소", (dialog, which) -> {
                String text = "1월 1일 ";
                binding.demonstrationDateDetail.setText(text);
            });
            startDatePickerDialog.show();

            binding.demonstrationDateDetail.setTextColor(getColor(R.color.contents));
        });
    }
}
