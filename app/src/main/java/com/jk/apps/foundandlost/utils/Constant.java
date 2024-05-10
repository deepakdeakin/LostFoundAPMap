package com.jk.apps.foundandlost.utils;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ForwardScope;

import java.util.List;

public class Constant {

    public static final int LOST_POST = 0, FOUND_POST = 1;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public interface onPermissionListner {
        public void onSuccess();
    }

    public static void getPermissions(AppCompatActivity activity, onPermissionListner listner) {
        PermissionX.init(activity)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .explainReasonBeforeRequest()
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel");
                    }
                }).request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        if (allGranted) {
                            listner.onSuccess();
                        } else if (deniedList.size() > 0) {
                            Toast.makeText(activity, "These Permissions are Denied: " + deniedList.get(0), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "Permissions are Denied", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
