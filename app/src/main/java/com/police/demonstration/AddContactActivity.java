package com.police.demonstration;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
            String name = String.valueOf(binding.nameEditText.getText());
            String number = String.valueOf(binding.numberEditText.getText());
            String position = String.valueOf(binding.positionEditText.getText());

            Intent intent = new Intent();
            intent.putExtra ("name", name);
            intent.putExtra ("number", number);
            intent.putExtra ("position", position);
            setResult (RESULT_OK, intent);
            finish ();
        });
    }
}
