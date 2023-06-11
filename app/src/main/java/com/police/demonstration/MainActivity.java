package com.police.demonstration;

import static com.police.demonstration.Constants.INTENT_NAME_BACKGROUND_NOISE_LEVEL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_DATE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_PLACE_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_GROUP_NAME_EDITTEXT;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_NAME;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstration.Constants.INTENT_NAME_ORGANIZER_POSITION;
import static com.police.demonstration.Constants.INTENT_NAME_PLACE_ZONE_IDX;
import static com.police.demonstration.Constants.INTENT_NAME_TIMEZONE_IDX;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.police.demonstration.adapter.DemonstrationAdapter;
import com.police.demonstration.add_demonstration.AddDemonstrationActivity;
import com.police.demonstration.database.DemonstrationInfo;
import com.police.demonstration.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getDemonstrationList().observe(this, this::initRecyclerView);

        viewModel.readDemonstration(this);

        initButton();
    }

    private void initButton() {
        binding.addDemonstrateButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddDemonstrationActivity.class);
            launcher.launch(intent);
        });
    }

    // registerForActivityResult call back
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            Intent intent = data.getData();
            assert intent != null;

            String[] dateDetail = intent.getStringExtra(INTENT_NAME_DEMONSTRATION_DATE_DETAIL).split(" ~ ");

            DemonstrationInfo demonstrationInfo = new DemonstrationInfo(
                intent.getStringExtra(INTENT_NAME_DEMONSTRATE_NAME_EDITTEXT),
                intent.getStringExtra(INTENT_NAME_GROUP_NAME_EDITTEXT),
                dateDetail[0], dateDetail[1],
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

    private void initRecyclerView(ArrayList<DemonstrationInfo> demonstrationList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.demonstrationRecyclerView.setLayoutManager(linearLayoutManager);
        DemonstrationAdapter demonstrationAdapter = new DemonstrationAdapter(demonstrationList);
        binding.demonstrationRecyclerView.setAdapter(demonstrationAdapter);
    }
}