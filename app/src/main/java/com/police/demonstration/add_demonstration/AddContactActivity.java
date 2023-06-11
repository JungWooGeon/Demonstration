package com.police.demonstration.add_demonstration;

import static com.police.demonstration.Constants.INTENT_NAME_NAME_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_PHONE_NUMBER_DETAIL;
import static com.police.demonstration.Constants.INTENT_NAME_POSITION_DETAIL;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityAddContactBinding;

public class AddContactActivity extends AppCompatActivity {
    private ActivityAddContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact);
        binding.setActivity(this);

        initButton();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.addButton.setOnClickListener(e -> {
            Intent intent = new Intent();
            intent.putExtra (INTENT_NAME_NAME_DETAIL, String.valueOf(binding.nameEditText.getText()));
            intent.putExtra (INTENT_NAME_PHONE_NUMBER_DETAIL, String.valueOf(binding.numberEditText.getText()));
            intent.putExtra (INTENT_NAME_POSITION_DETAIL, String.valueOf(binding.positionEditText.getText()));
            setResult (RESULT_OK, intent);
            finish ();
        });
    }
}
