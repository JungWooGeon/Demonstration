package com.police.demonstration.manage_demonstration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.police.demonstration.R;
import com.police.demonstration.databinding.ActivityManageDemonstrationBinding;

/**
 * 시위 관리 화면
 * Bottom Navigation Bar 를 통해 3개의 Fragment 를 보여줌 (측정, 기록, 고지)
 */
public class ManageDemonstrationActivity extends AppCompatActivity {
    private ActivityManageDemonstrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_demonstration);
        binding.setActivity(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
