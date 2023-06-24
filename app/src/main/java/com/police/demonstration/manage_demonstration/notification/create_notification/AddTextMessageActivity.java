package com.police.demonstration.manage_demonstration.notification.create_notification;

import static com.police.demonstration.Constants.INTENT_NAME_ADD_TEXT_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityAddTextMessageBinding;

import java.util.Objects;

/**
 * 추가 텍스트 메시지 입력 화면
 * 1. 텍스트 메시지 입력
 * 2. 저장 버튼 클릭 시 setResult() 를 통해 메시지 정보를 이전 화면(CreateNotificationActivity)으로 전달
 */
public class AddTextMessageActivity extends AppCompatActivity {

    ActivityAddTextMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_text_message);
        binding.setActivity(this);

        // 뒤로가기 버튼 클릭 이벤트 -> 종료
        binding.backButton.setOnClickListener(e -> finish());

        // 저장 버튼 클릭 이벤트
        binding.saveButton.setOnClickListener(e -> {
            if (Objects.requireNonNull(binding.contentEditText.getText()).toString().equals("")) {
                // 입력 값이 없을 경우 토스트 메시지 출력
                Toast.makeText(this, getString(R.string.plz_input_text_message), Toast.LENGTH_SHORT).show();
            } else {
                // 입력 값이 있을 경우 setResult 로 입력 값 전달 후 종료
                Intent intent = new Intent();
                intent.putExtra(INTENT_NAME_ADD_TEXT_MESSAGE, Objects.requireNonNull(binding.contentEditText.getText()).toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
