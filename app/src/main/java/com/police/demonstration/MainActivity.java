package com.police.demonstration;

import static com.police.demonstration.Constants.DATE_DETAIL_END_DATE_IDX;
import static com.police.demonstration.Constants.DATE_DETAIL_START_DATE_IDX;
import static com.police.demonstration.Constants.INTENT_NAME_BACKGROUND_NOISE_LEVEL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_DATE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_PLACE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_END_YEAR;
import static com.police.demonstration.Constants.INTENT_NAME_GROUP_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_NAME;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_POSITION;
import static com.police.demonstration.Constants.INTENT_NAME_PLACE_ZONE_IDX;
import static com.police.demonstration.Constants.INTENT_NAME_START_YEAR;
import static com.police.demonstration.Constants.INTENT_NAME_TIMEZONE_IDX;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.police.demonstration.adapter.DemonstrationAdapter;
import com.police.demonstration.add_demonstration.AddDemonstrationActivity;
import com.police.demonstration.database.DemonstrationInfo;
import com.police.demonstration.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * 메인화면
 * 1. 시위 리스트 화면에 시위 정보 출력 -> room db, recyclerview
 * 2. '시위 추가' 버튼 클릭 -> '시위 추가' 화면으로 이동
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // MainActivity ViewModel
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        // init ViewModel
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        // viewModel 에서 시위 데이터 변경 시 recyclerview update
        viewModel.getDemonstrationList().observe(this, this::initRecyclerView);
        // onCreate 에서 시위 데이터 읽기
        viewModel.readDemonstration(this);

        initButton();
    }

    private void initButton() {
        // 시위 추가 버튼 클릭 시 시위 추가 화면으로 전환
        binding.addDemonstrateButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddDemonstrationActivity.class);
            launcher.launch(intent);
        });
    }

    // registerForActivityResult call back 설정 (시위 추가 화면에서 ok 사인이 나올 경우 데이터 저장)
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            Intent intent = data.getData();
            assert intent != null;

            final int START_DATE_IDX = 0;
            final int END_DATE_IDX = 1;

            String[] startEndDate = getStartEndDate(
                    intent.getStringExtra(INTENT_NAME_DEMONSTRATION_DATE_DETAIL),
                    intent.getStringExtra(INTENT_NAME_START_YEAR),
                    intent.getStringExtra(INTENT_NAME_END_YEAR)
            );

            DemonstrationInfo demonstrationInfo = new DemonstrationInfo(
                intent.getStringExtra(INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT),
                intent.getStringExtra(INTENT_NAME_GROUP_NAME_EDITTEXT),
                startEndDate[START_DATE_IDX], startEndDate[END_DATE_IDX],
                intent.getStringExtra(INTENT_NAME_TIMEZONE_IDX),
                intent.getStringExtra(INTENT_NAME_DEMONSTRATION_PLACE_DETAIL),
                intent.getStringExtra(INTENT_NAME_PLACE_ZONE_IDX),
                intent.getStringExtra(INTENT_NAME_ORGANIZER_NAME),
                intent.getStringExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER),
                intent.getStringExtra(INTENT_NAME_ORGANIZER_POSITION),
                intent.getStringExtra(INTENT_NAME_BACKGROUND_NOISE_LEVEL)
            );

            viewModel.addDemonstration(this, demonstrationInfo);
        }
    });

    // recyclerview 설정 (demonstrationList 데이터 연결)
    private void initRecyclerView(ArrayList<DemonstrationInfo> demonstrationList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.demonstrationRecyclerView.setLayoutManager(linearLayoutManager);
        DemonstrationAdapter demonstrationAdapter = new DemonstrationAdapter(demonstrationList);
        binding.demonstrationRecyclerView.setAdapter(demonstrationAdapter);
    }

    private String[] getStartEndDate(String stringDateDetail, String startYear, String endYear) {
        String[] dateDetail = stringDateDetail.split(getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space));

        String[] startDetail = dateDetail[DATE_DETAIL_START_DATE_IDX].split("\\s");
        String[] endDetail = dateDetail[DATE_DETAIL_END_DATE_IDX].split("\\s");

        String startMonth = startDetail[0].split(getString(R.string.month))[0];
        String startDay = startDetail[1].split(getString(R.string.day))[0];
        String startHour = startDetail[2].split("시")[0];
        String startMinute = startDetail[3].split("분")[0];

        String endMonth = endDetail[0].split("월")[0];
        String endDay = endDetail[1].split("일")[0];
        String endHour = endDetail[2].split("시")[0];
        String endMinute = endDetail[3].split("분")[0];

        String date1 = startYear + "-" + startMonth + "-" + startDay + "-" + startHour + "-" + startMinute;
        String date2 = endYear + "-" + endMonth + "-" + endDay + "-" + endHour + "-" + endMinute;

        return new String[]{date1, date2};
    }
}