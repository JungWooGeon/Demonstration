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

/**
 * 연락망 추가하기 화면
 * 1. 연락망 추가 정보 입력
 * 2. 정보를 모두 입력한 후 setResult(intent) 사용하여 이전 화면으로 정보 전달
 */
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
        // 뒤로가기 버튼 클릭 시 화면 종료
        binding.backButton.setOnClickListener(e -> finish());

        // '추가하기' 버튼 클릭 시 입력된 정보들을 모두 intent 에 put 하고, setResult 를 사용하여 이전 화면으로 정보 전달 후 화면 종료
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
