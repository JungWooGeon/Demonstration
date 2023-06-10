package com.police.demonstration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.databinding.ActivityAddDemonstrationBinding;


public class AddDemonstrationActivity extends AppCompatActivity {

    private ActivityAddDemonstrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_demonstration);
        binding.setActivity(this);

        initButton();
    }

    private void initButton() {
        binding.phoneNumberButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            launcher.launch(intent);
        });

        binding.backButton.setOnClickListener(e -> finish());
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            Intent intent = data.getData();
            assert intent != null;

            binding.nameDetail.setText(intent.getStringExtra("name"));
            binding.phoneNumberDetail.setText(intent.getStringExtra("number"));
            binding.positionDetail.setText(intent.getStringExtra("position"));
        }
    });
}
