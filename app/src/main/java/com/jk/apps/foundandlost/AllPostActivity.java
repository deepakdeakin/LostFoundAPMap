package com.jk.apps.foundandlost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jk.apps.foundandlost.adapter.PostAdapter;
import com.jk.apps.foundandlost.databinding.ActivityAllPostBinding;
import com.jk.apps.foundandlost.model.PostModel;
import com.jk.apps.foundandlost.utils.DbHelper;

import java.util.ArrayList;

public class AllPostActivity extends AppCompatActivity {

    ActivityAllPostBinding binding;
    PostAdapter adapter;
    DbHelper helper;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityAllPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        helper = new DbHelper(this);
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        refreshData();
                    }
                });
        setData();
    }

    public void setData() {
        adapter = new PostAdapter(this, new ArrayList<>(), (pos, model) -> {
            Intent intent = new Intent(this, ViewPostActivity.class);
            intent.putExtra("postId", model.postId);
            resultLauncher.launch(intent);
        });
        binding.rvPost.setAdapter(adapter);
        refreshData();
    }

    public void refreshData() {
        adapter.refresh(helper.getAllData());
    }
}