package com.jk.apps.foundandlost.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.jk.apps.foundandlost.R;
import com.jk.apps.foundandlost.databinding.DialogGpsBinding;

import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private Boolean checkGPSStatus(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }

    private void showDialogSettingGPS(Context context) {
        Dialog dialog = new Dialog(context);
        DialogGpsBinding gpsBinding = DialogGpsBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(gpsBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gpsBinding.btnSettings.setOnClickListener(view -> {
            dialog.setOnDismissListener(v -> {
                try {
                    context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dialog.dismiss();
        });
        dialog.show();
    }

    public interface onLocationListener {
        public void onSuccess(Location result);
    }

    private void getLocationMain(Context context, onLocationListener listener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            LocationRequest locationRequest = new LocationRequest.Builder(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    10000
            ).build();
            locationRequest.setPriority(100);
            locationRequest.setSmallestDisplacement(20.0f);
            locationRequest.setNumUpdates(1);
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        listener.onSuccess(locationResult.getLastLocation());
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            return;
        }
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
    }

    public String getAddress(Context context, Double latitude, Double longitude) {
        String str = null;
        try {
            List<Address> fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(latitude, longitude, 1);
            if (!fromLocation.isEmpty()) {

                str = fromLocation.get(0).getAddressLine(0) + ", " + fromLocation.get(0).getLocality() + ", " + fromLocation.get(0).getAdminArea() + ", " + fromLocation.get(0).getCountryName() + ".";
            } else {
                Constant.showToast(context, "Location Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str == null ? "" : str;
    }

    public void getLocation(Context context, onLocationListener listener) {
        if (checkGPSStatus(context)) {
            getLocationMain(context, listener);
        } else {
            showDialogSettingGPS(context);
        }
    }

}
