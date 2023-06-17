package com.police.demonstration.manage_demonstration.measurement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityAddMeasurementBinding;

public class AddMeasurementActivity extends AppCompatActivity {

    private ActivityAddMeasurementBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_measurement);
        binding.setActivity(this);
    }
}
