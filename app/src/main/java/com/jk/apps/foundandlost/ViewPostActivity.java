package com.jk.apps.foundandlost;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jk.apps.foundandlost.databinding.ActivityViewPostBinding;
import com.jk.apps.foundandlost.model.PostModel;
import com.jk.apps.foundandlost.utils.Constant;
import com.jk.apps.foundandlost.utils.DbHelper;

import java.util.Calendar;

public class ViewPostActivity extends AppCompatActivity {

    ActivityViewPostBinding binding;
    int postId;
    PostModel postModel;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DbHelper(this);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        onClick();
        if (getIntent() != null && getIntent().hasExtra("postId")) {
            postId = getIntent().getIntExtra("postId", 0);
            postModel = dbHelper.getPostById(postId);
            if (postModel != null) {
                setData();
                return;
            }
        }
        Constant.showToast(this, "Unknown Error Occurred...");
        finish();
    }

    public void onClick() {
        binding.btnRemove.setOnClickListener(v -> {
            dbHelper.removePostById(postModel.postId);
            Constant.showToast(this, "Post Removed Successfully");
            setResult(RESULT_OK);
            finish();
        });
    }

    public void setData() {
        String adType = postModel.postType == Constant.LOST_POST ? "Lost" : "Found";
        binding.txtName.setText(adType + " " + postModel.postName);
        binding.txtLocation.setText("At " + postModel.postLocation);
        long timeMillis = System.currentTimeMillis();
        long diff = timeMillis - postModel.postDate;
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        if (numOfDays <= 0) {
            binding.txtDate.setText("Today");
        } else if (numOfDays == 1) {
            binding.txtDate.setText("Yesterday");
        } else {
            binding.txtDate.setText(numOfDays + " days ago");
        }
    }
}