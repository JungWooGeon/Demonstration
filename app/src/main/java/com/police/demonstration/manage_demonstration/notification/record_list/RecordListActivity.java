package com.police.demonstration.manage_demonstration.notification.record_list;

import static com.police.demonstration.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.databinding.ActivityRecordListBinding;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.manage_demonstration.notification.adapter.RecordAdapter;

import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {

    private ActivityRecordListBinding binding;

    private RecordListViewModel viewModel;

    private DemonstrationInfo demonstrationInfo;

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
    }

    private void initRecyclerView(ArrayList<MeasurementInfo> measurementInfoList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recordListRecyclerView.setLayoutManager(linearLayoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(
                measurementInfoList,
                getIntent().getIntExtra(INTENT_NAME_NOTIFICATION_TYPE, 0)
        );
        binding.recordListRecyclerView.setAdapter(recordAdapter);
    }
}
