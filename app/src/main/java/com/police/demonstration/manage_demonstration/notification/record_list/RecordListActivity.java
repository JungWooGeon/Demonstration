package com.police.demonstration.manage_demonstration.notification.record_list;

import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_ID;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityRecordListBinding;
import com.police.demonstration.database.measurement.MeasurementInfo;
import com.police.demonstration.manage_demonstration.notification.adapter.RecordAdapter;

import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {

    private ActivityRecordListBinding binding;

    private RecordListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_list);
        binding.setActivity(this);

        int demonstrationId = getIntent().getIntExtra(INTENT_NAME_DEMONSTRATION_ID, -1);

        viewModel = new ViewModelProvider(this).get(RecordListViewModel.class);
        viewModel.getMeasurementList().observe(this, this::initRecyclerView);
        viewModel.readRecordList(this, demonstrationId);

        initButton();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
    }

    private void initRecyclerView(ArrayList<MeasurementInfo> measurementInfoList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recordListRecyclerView.setLayoutManager(linearLayoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(measurementInfoList);
        binding.recordListRecyclerView.setAdapter(recordAdapter);
    }
}
