package com.jk.apps.foundandlost;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.libraries.places.api.Places;
import com.jk.apps.foundandlost.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Places.initialize(this,getString(R.string.api_key));
        onClick();
    }

    public void onClick() {
        binding.btnNewAdvert.setOnClickListener(v -> {
            startActivity(new Intent(this, NewPostActivity.class));
        });
        binding.btnShowAll.setOnClickListener(v -> {
            startActivity(new Intent(this, AllPostActivity.class));
        });
        binding.btnMap.setOnClickListener(v -> {
            startActivity(new Intent(this, MapActivity.class));
        });
    }
}