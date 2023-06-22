package com.police.demonstration.manage_demonstration.notification;

import static com.police.demonstration.Constants.INTENT_NAME_DEMONSTRATION_ID;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.police.demonstration.databinding.FragmentNotificationBinding;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
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

        binding.maintenanceOrderButton.setOnClickListener(e -> {
            Intent intent = new Intent(requireActivity(), RecordListActivity.class);
            // Intent 로 전달 받은 시위 정보의 ID 를 activity 에 전달
            intent.putExtra(INTENT_NAME_DEMONSTRATION_ID, demonstrationInfo.getId());
            startActivity(intent);
        });
    }
}

