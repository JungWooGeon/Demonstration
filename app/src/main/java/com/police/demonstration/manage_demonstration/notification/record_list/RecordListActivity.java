package com.police.demonstration.manage_demonstration.notification.record_list;

import static com.police.demonstration.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.databinding.ActivityRecordListBinding;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.manage_demonstration.notification.adapter.RecordAdapter;
import com.police.demonstration.manage_demonstration.notification.create_notification.CreateNotificationActivity;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 기록 리스트 화면
 * 1. 화면에 측정 기록 리스트 보여주기
 * 2. 측정 기록 리스트 클릭하여 선택
 * 3. 선택한 측정 기록을 토대로 고지 생성 -> CreateNotificationAcvitivy 화면으로 전환
 */
public class RecordListActivity extends AppCompatActivity {

    private ActivityRecordListBinding binding;

    private RecordListViewModel viewModel;

    private DemonstrationInfo demonstrationInfo;
    private RecordAdapter recordAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_list);
        binding.setActivity(this);

        demonstrationInfo = getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);

        viewModel = new ViewModelProvider(this).get(RecordListViewModel.class);
        viewModel.getMeasurementList().observe(this, this::initRecyclerView);
        viewModel.readRecordList(this, demonstrationInfo.getId());

        initButton();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.createButton.setOnClickListener(e -> {
            //@TODO 일단 기록 중 하나만 보내기 (추후 측정 기록 세 개 필요한 것은 세 개 전부 보내기)
            MeasurementInfo measurementInfo = null;
            for (int i = 0; i < recordAdapter.getItemCount(); i++) {
                if (recordAdapter.getCheckList().get(i)) {
                    measurementInfo = Objects.requireNonNull(viewModel.getMeasurementList().getValue()).get(i);
                    break;
                }
            }

            if (measurementInfo != null) {
                // 선택한 기록을 intent 로 전달하고, 고지서 만들기 Activity 로 전환
                Intent intent = new Intent(this, CreateNotificationActivity.class);
                intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
                intent.putExtra(INTENT_NAME_PARCELABLE_MEASUREMENT, measurementInfo);
                startActivity(intent);
            } else {
                // 측정 기록을 선택하지 않았을 경우 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_select_record), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView(ArrayList<MeasurementInfo> measurementInfoList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recordListRecyclerView.setLayoutManager(linearLayoutManager);
        recordAdapter = new RecordAdapter(
                measurementInfoList,
                getIntent().getIntExtra(INTENT_NAME_NOTIFICATION_TYPE, 0)
        );
        binding.recordListRecyclerView.setAdapter(recordAdapter);
    }
}
