package com.jk.apps.foundandlost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.jk.apps.foundandlost.databinding.ActivityNewPostBinding;
import com.jk.apps.foundandlost.utils.Constant;
import com.jk.apps.foundandlost.utils.DbHelper;
import com.jk.apps.foundandlost.utils.LocationHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class NewPostActivity extends AppCompatActivity {

    ActivityNewPostBinding binding;
    DbHelper dbHelper;
    long adTime = 0;
    Calendar calendar;
    int adType = Constant.LOST_POST;
    double latitude = 0, longitude = 0;
    String location = "";

    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DbHelper(this);
        calendar = Calendar.getInstance();
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Place place = Autocomplete.getPlaceFromIntent(intent);
                            Log.e("TAG",place.getAddress().toString()+" HELL");
                            Log.e("TAG",place.getLatLng().latitude+" HELL");
                            Log.e("TAG",place.getLatLng().longitude+" HELL");

                            binding.edLocation.setText(place.getAddress());
                            if (place.getLatLng() != null) {
                                latitude = place.getLatLng().latitude;
                                longitude = place.getLatLng().longitude;
                            }
                        }
                    }
                });
        onClick();
    }

    public void onClick() {
        binding.btnSave.setOnClickListener(v -> {
            saveData();
        });
        binding.edDate.setOnClickListener(v -> {
            calendar.setTimeInMillis(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat") DatePickerDialog dialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                adTime = calendar.getTimeInMillis();
                binding.edDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(adTime));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            dialog.show();
        });
        binding.edLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            resultLauncher.launch(intent);
        });
        binding.btnGetLocation.setOnClickListener(v -> {
            Constant.getPermissions(this, () -> new LocationHelper().getLocation(NewPostActivity.this, result -> {
                latitude = result.getLatitude();
                longitude = result.getLongitude();
                new Thread(() -> {
                    location = new LocationHelper().getAddress(NewPostActivity.this, latitude, longitude);
                    runOnUiThread(() -> {
                        binding.edLocation.setText(location);
                    });
                }).start();

            }));
        });
    }

    public void saveData() {
        if (binding.edName.getText().length() <= 0) {
            binding.edName.setError("Please Enter Name");
        } else if (binding.edPhone.getText().length() <= 0) {
            binding.edPhone.setError("Please Enter Phone");
        } else if (binding.edInfo.getText().length() <= 0) {
            binding.edInfo.setError("Please Enter Description");
        } else if (binding.edDate.getText().length() <= 0) {
            Constant.showToast(this, "Please Enter Date");
        } else if (binding.edLocation.getText().length() <= 0) {
            binding.edLocation.setError("Please Enter Location");
        } else {
            adType = binding.rdLost.isChecked() ? Constant.LOST_POST : Constant.FOUND_POST;
            dbHelper.addNewAdvert(binding.edName.getText().toString(), binding.edPhone.getText().toString(), binding.edInfo.getText().toString(), adTime, binding.edLocation.getText().toString(), latitude, longitude, adType);
            Constant.showToast(this, "Post Added Successfully");
            finish();
        }
    }
}