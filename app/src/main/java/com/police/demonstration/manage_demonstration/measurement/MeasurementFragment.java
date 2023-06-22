package com.police.demonstration.manage_demonstration.measurement;

import static com.police.demonstration.Constants.INTENT_NAME_EQUIVALENT_NOISE;
import static com.police.demonstration.Constants.INTENT_NAME_HIGHEST_NOISE;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_MEASUREMENT;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.police.demonstration.R;
import com.police.demonstration.database.demonstration.DemonstrationInfo;
import com.police.demonstration.databinding.FragmentMeasurementBinding;
import com.police.demonstration.manage_demonstration.measurement.add_measurement.AddMeasurementActivity;

/**
 * 시위 정보 화면 측정 부분 Fragment
 * 1. 시위 기본 정보를 보여줌 ( 등가 소음과 최고 소음 계산 하여 같이 보여줌)
 * 2. '측정 기록 입력' 버튼 클릭 -> 측정 기록 입력 화면 으로 전환
 * 3. 측정 기록 입력 화면에서 입력 완료 시 이 화면으로 돌아와 측정 기록 데이터 추가
 */
public class MeasurementFragment extends Fragment {

    private FragmentMeasurementBinding binding;
    private MeasurementViewModel viewModel;

    private DemonstrationInfo demonstrationInfo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);

        binding = FragmentMeasurementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Intent 로 전달 받은 시위 정보를 저장
        demonstrationInfo = requireActivity().getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);
        Log.d("테스트", demonstrationInfo.getId() + " " + demonstrationInfo.getPlace());

        initTextView();
        initButton();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 시위 정보로 화면 정보 변경 (제목, 단체명, 시간, 장소, 주최자 정보, 소음도)
    private void initTextView() {
        // 시위 제목 반영
        binding.title.setText(demonstrationInfo.getName());

        // 시위 그룹명 반영
        binding.groupNameDetail.setText(demonstrationInfo.getGroupName());

        // 시위 시간 반영
        String dateDetail = demonstrationInfo.getStartDate() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + demonstrationInfo.getEndDate();
        binding.demonstrationDateDetail.setText(dateDetail);

        // 시위 시간대 반영 (주간 | 야간 | 심야)
        switch (demonstrationInfo.getTimeZone()) {
            case TIME_ZONE_NIGHT:
                binding.timeZoneDay.setTextColor(requireActivity().getColor(R.color.contents_light));
                binding.timeZoneNight.setTextColor(requireActivity().getColor(R.color.content_purple));
                break;
            case TIME_ZONE_LATE_NIGHT:
                binding.timeZoneDay.setTextColor(requireActivity().getColor(R.color.contents_light));
                binding.timeZoneLateNight.setTextColor(requireActivity().getColor(R.color.content_purple));
                break;
            default:
                break;
        }

        // 시위 개최 장소 반영
        binding.demonstrationPlaceDetail.setText(demonstrationInfo.getPlace());

        // 시위 개최 장소대 반영 (주거 지역, 학교 | 공공 도서관 | 그 밖)
        switch (demonstrationInfo.getPlaceZone()) {
            case PLACE_ZONE_PUBLIC:
                binding.placeZoneHome.setTextColor(requireActivity().getColor(R.color.contents_light));
                binding.placeZonePublic.setTextColor(requireActivity().getColor(R.color.content_purple));
                break;
            case PLACE_ZONE_ETC:
                binding.placeZoneHome.setTextColor(requireActivity().getColor(R.color.contents_light));
                binding.placeZoneEtc.setTextColor(requireActivity().getColor(R.color.content_purple));
                break;
            default:
                break;
        }

        // 주최자 성명, 연락처, 직책 반영
        binding.nameDetail.setText(demonstrationInfo.getOrganizerName());
        binding.phoneNumberDetail.setText(demonstrationInfo.getOrganizerPhoneNumber());
        binding.positionDetail.setText(demonstrationInfo.getOrganizerPosition());

        // 배경 소음도 반영
        String backgroundNoise = demonstrationInfo.getBackgroundNoiseLevel() + requireActivity().getString(R.string.space) + requireActivity().getString(R.string.decibel);
        binding.backgroundNoiseDetail.setText(backgroundNoise);

        // 기준 등가 소음, 기준 최고 소음 반영
        String equivalentNoise = demonstrationInfo.getStandardEquivalent() + getString(R.string.space) + getString(R.string.decibel);
        String highestNoise = demonstrationInfo.getStandardHighest() + getString(R.string.space) + getString(R.string.decibel);

        // 등가 소음, 최고 소음 반영
        binding.equivalentNoiseDetail.setText(equivalentNoise);
        binding.highestNoiseDetail.setText(highestNoise);
    }

    private void initButton() {
        // '뒤로 가기' 버튼 클릭 시 종료
        binding.backButton.setOnClickListener(e -> requireActivity().finish());

        // '측정 기록 입력' 버튼 클릭 이벤트 -> '측정 입력' 화면으로 전환
        binding.inputRecordMeasurementButton.setOnClickListener(e -> {
            // Intent 에 시위 정보 객체, 등가 소음, 최고 소음 데이터를 담아 전달
            Intent intent = new Intent(requireActivity(), AddMeasurementActivity.class);
            intent.putExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION, demonstrationInfo);
            intent.putExtra(INTENT_NAME_EQUIVALENT_NOISE, binding.equivalentNoiseDetail.getText());
            intent.putExtra(INTENT_NAME_HIGHEST_NOISE, binding.highestNoiseDetail.getText());

            addLauncher.launch(intent);
        });
    }

    // registerForActivityResult call back 설정 (측정 입력 화면에서 ok 사인이 나올 경우 데이터 저장)
    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            // RESULT_OK 를 전달 받은 경우 토스트 메시지를 띄우고, viewModel 에 측정 기록 추가 함수를 실행
            Toast.makeText(requireActivity(), getString(R.string.complete_add_measurement), Toast.LENGTH_SHORT).show();

            Intent intent = data.getData();
            assert intent != null;

            viewModel.addMeasurement(requireActivity(), intent.getParcelableExtra(INTENT_NAME_PARCELABLE_MEASUREMENT));
        }
    });
}