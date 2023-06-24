package com.police.demonstration.manage_demonstration.notification;

import static com.police.demonstration.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE;
import static com.police.demonstration.Constants.NOTIFICATION_TYPE_MAINTENANCE_VIOLATION_HIGHEST_NOISE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.police.demonstration.databinding.FragmentNotificationBinding;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.manage_demonstration.notification.create_notification.NotificationActivity;
import com.police.demonstration.manage_demonstration.notification.record_list.RecordListActivity;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;

    private DemonstrationInfo demonstrationInfo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        demonstrationInfo = requireActivity().getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        initButton();
        initTextView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initTextView() {
        binding.title.setText(demonstrationInfo.getName());
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> requireActivity().finish());

        // 안내문 발송 버튼 클릭 이벤트 - > 고지 화면으로 전환
        binding.sendNoticeButton.setOnClickListener(e -> {
            Intent intent = new Intent(requireActivity(), NotificationActivity.class);
            intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
            startActivity(intent);
        });

        // 유지 명령 - 최고 소음 초과 클릭 이벤트
        binding.maintenanceOrder1.setOnClickListener(e-> startRecordListActivity(NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE));
        binding.maintenanceOrder1Detail.setOnClickListener(e -> startRecordListActivity(NOTIFICATION_TYPE_MAINTENANCE_EXCEED_HIGHEST_NOISE));

        // 유지 명령 - 등가 소음 초과 클릭 이벤트
        binding.maintenanceOrder2.setOnClickListener(e-> startRecordListActivity(NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE));
        binding.maintenanceOrder2Detail.setOnClickListener(e -> startRecordListActivity(NOTIFICATION_TYPE_MAINTENANCE_EXCEED_EQUIVALENT_NOISE));

        // 유지 명령 - 최고 소음 위반 클릭 이벤트
        binding.maintenanceOrder3.setOnClickListener(e-> startRecordListActivity(NOTIFICATION_TYPE_MAINTENANCE_VIOLATION_HIGHEST_NOISE));
        binding.maintenanceOrder3Detail.setOnClickListener(e -> startRecordListActivity(NOTIFICATION_TYPE_MAINTENANCE_VIOLATION_HIGHEST_NOISE));
    }

    private void startRecordListActivity(int notificationType) {
        Intent intent = new Intent(requireActivity(), RecordListActivity.class);
        // Intent 로 전달 받은 시위 정보를 activity 에 전달
        intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
        intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
        startActivity(intent);
    }
}

