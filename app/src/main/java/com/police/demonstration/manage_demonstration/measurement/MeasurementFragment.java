package com.police.demonstration.manage_demonstration.measurement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.police.demonstration.databinding.FragmentMeasurementBinding;

public class MeasurementFragment extends Fragment {

    private FragmentMeasurementBinding binding;
    private MeasurementViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);

        binding = FragmentMeasurementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}