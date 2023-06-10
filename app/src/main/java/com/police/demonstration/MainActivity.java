package com.police.demonstration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.police.demonstration.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        initButton();
    }

    private void initButton() {
        binding.addDemonstrateButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddDemonstrationActivity.class);
            startActivity(intent);
        });
    }
}