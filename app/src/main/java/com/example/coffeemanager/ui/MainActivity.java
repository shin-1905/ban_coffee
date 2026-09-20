package com.example.coffeemanager.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String userRole;
    private String startDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRole = getIntent().getStringExtra("role");
        if (userRole == null || userRole.trim().isEmpty()) {
            userRole = "manager";
        }
        startDestination = getIntent().getStringExtra("start_destination");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_list_container,
                            "history".equals(startDestination) ? new HistoryFragment() : new DrinkListFragment())
                    .commit();
        }
    }

    public String getUserRole() {
        return userRole;
    }
}
