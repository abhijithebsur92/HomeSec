package com.comp6441.homesec.util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.comp6441.homesec.MainActivity;
import com.example.homesec.R;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class FCMUtils {

    private static final String TAG = "FCM";

    private static String token = "";
    public static void generateToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Log.d("FCM - Util " , " Token " + token);
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);

                    }
                });
    }

    public static String getToken() {
        return token;
    }
}
