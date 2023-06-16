package com.police.demonstration.main;

import static com.police.demonstration.Constants.INTENT_NAME_IS_ADD_BACKGROUND_NOISE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.police.demonstration.R;
import com.police.demonstration.adapter.DemonstrationAdapter;
import com.police.demonstration.add_demonstration.AddDemonstrationActivity;
import com.police.demonstration.database.DemonstrationInfo;
import com.police.demonstration.databinding.ActivityMainBinding;
import com.police.demonstration.manage_demonstration.ManageDemonstrationActivity;

import java.util.ArrayList;

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
            intent.putExtra(INTENT_NAME_IS_ADD_BACKGROUND_NOISE, false);
            addLauncher.launch(intent);
        });
    }

    // recyclerview 설정 (demonstrationList 데이터 연결)
    private void initRecyclerView(ArrayList<DemonstrationInfo> demonstrationList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.demonstrationRecyclerView.setLayoutManager(linearLayoutManager);
        DemonstrationAdapter demonstrationAdapter = new DemonstrationAdapter(demonstrationList);
        demonstrationAdapter.setListener(new DemonstrationAdapter.AdapterListener() {
            @Override
            public void onDetailButtonClick(View view, DemonstrationInfo demonstrationInfo) {
                // '>' 버튼 클릭 이벤트
                if (demonstrationInfo.getBackgroundNoiseLevel().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.plz_input_background_noise), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), ManageDemonstrationActivity.class);
                    intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
                    startActivity(intent);
                }
            }

            @Override
            public void inputBackNoiseButtonClick(View view, DemonstrationInfo demonstrationInfo) {
                // 배경 소음도 추가 버튼 클릭 이벤트 - 배경 소음도 수정 화면으로 전환
                Intent intent = new Intent(view.getContext(), AddDemonstrationActivity.class);
                intent.putExtra(INTENT_NAME_IS_ADD_BACKGROUND_NOISE, true);
                intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);

                addBackgroundNoiseLauncher.launch(intent);
            }
        });
        binding.demonstrationRecyclerView.setAdapter(demonstrationAdapter);
    }

    // registerForActivityResult call back 설정 (시위 추가 화면에서 ok 사인이 나올 경우 데이터 저장)
    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            Toast.makeText(this, getString(R.string.complete_add_demonstration), Toast.LENGTH_SHORT).show();

            Intent intent = data.getData();
            assert intent != null;

            viewModel.addDemonstration(this, intent.getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION));
        }
    });

    // registerForActivityResult call back 설정 (배경 소음도 추가 화면에서 ok 사인이 나올 경우 배경 소음도 데이터 추가)
    ActivityResultLauncher<Intent> addBackgroundNoiseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            Toast.makeText(this, getString(R.string.complete_update_background_noise), Toast.LENGTH_SHORT).show();

            Intent intent = data.getData();
            assert intent != null;

            viewModel.updateBackgroundNoise(this, intent.getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION));
        }
    });
}