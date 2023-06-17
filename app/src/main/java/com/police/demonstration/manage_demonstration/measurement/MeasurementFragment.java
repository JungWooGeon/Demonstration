package com.police.demonstration.manage_demonstration.measurement;

import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_ETC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_HOME;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_DAY_HOME;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_DAY_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_ETC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_NIGHT_HOME;
import static com.police.demonstration.Constants.DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC;
import static com.police.demonstration.Constants.INTENT_NAME_PARCELABLE_DEMONSTRATION;
import static com.police.demonstration.Constants.PLACE_ZONE_ETC;
import static com.police.demonstration.Constants.PLACE_ZONE_HOME;
import static com.police.demonstration.Constants.PLACE_ZONE_PUBLIC;
import static com.police.demonstration.Constants.TIME_ZONE_DAY;
import static com.police.demonstration.Constants.TIME_ZONE_LATE_NIGHT;
import static com.police.demonstration.Constants.TIME_ZONE_NIGHT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.police.demonstration.R;
import com.police.demonstration.database.DemonstrationInfo;
import com.police.demonstration.databinding.FragmentMeasurementBinding;

public class MeasurementFragment extends Fragment {

    private FragmentMeasurementBinding binding;
    private MeasurementViewModel viewModel;

    private DemonstrationInfo demonstrationInfo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);

        binding = FragmentMeasurementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        demonstrationInfo = requireActivity().getIntent().getParcelableExtra(INTENT_NAME_PARCELABLE_DEMONSTRATION);

        initTextView();
        initButton();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initTextView() {
        binding.title.setText(demonstrationInfo.getName());
        binding.groupNameDetail.setText(demonstrationInfo.getGroupName());

        binding.demonstrationDate.setText(getString(R.string.demonstration_time));

        String dateDetail = demonstrationInfo.getStartDate() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + demonstrationInfo.getEndDate();
        binding.demonstrationDateDetail.setText(dateDetail);

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

        binding.demonstrationPlaceDetail.setText(demonstrationInfo.getPlace());

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

        binding.nameDetail.setText(demonstrationInfo.getOrganizerName());
        binding.phoneNumberDetail.setText(demonstrationInfo.getOrganizerPhoneNumber());
        binding.positionDetail.setText(demonstrationInfo.getOrganizerPosition());

        String backgroundNoise = demonstrationInfo.getBackgroundNoiseLevel() + requireActivity().getString(R.string.space) + requireActivity().getString(R.string.decibel);
        binding.backgroundNoiseDetail.setText(backgroundNoise);

        // 등가 소음, 최고 소음 설정
        int timeZone = demonstrationInfo.getTimeZone();
        int placeZone = demonstrationInfo.getPlaceZone();

        String equivalentNoise = getString(R.string.space) + getString(R.string.decibel);
        String highestNoise = getString(R.string.space) + getString(R.string.decibel);

        if (timeZone == TIME_ZONE_DAY) {
            if (placeZone == PLACE_ZONE_HOME) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_DAY_HOME + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_DAY_HOME + highestNoise;
            } else if (placeZone == PLACE_ZONE_PUBLIC) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_DAY_PUBLIC + highestNoise;
            } else if (placeZone == PLACE_ZONE_ETC) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_DAY_ETC + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_ETC + highestNoise;
            }
        } else if (timeZone == TIME_ZONE_NIGHT) {
            if (placeZone == PLACE_ZONE_HOME) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_NIGHT_HOME + highestNoise;
            } else if (placeZone == PLACE_ZONE_PUBLIC) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC + highestNoise;
            } else if (placeZone == PLACE_ZONE_ETC) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_ETC + highestNoise;
            }
        } else if (timeZone == TIME_ZONE_LATE_NIGHT) {
            if (placeZone == PLACE_ZONE_HOME) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME + highestNoise;
            } else if (placeZone == PLACE_ZONE_PUBLIC) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC + highestNoise;
            } else if (placeZone == PLACE_ZONE_ETC) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC + equivalentNoise;
                highestNoise = DEFAULT_HIGHEST_NOISE_ETC + highestNoise;
            }
        }

        binding.equivalentNoiseDetail.setText(equivalentNoise);
        binding.highestNoiseDetail.setText(highestNoise);
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> requireActivity().finish());
        binding.inputRecordMeasurementButton.setOnClickListener(e -> {
            Intent intent = new Intent(requireActivity(), AddMeasurementActivity.class);
            startActivity(intent);
        });
    }
}