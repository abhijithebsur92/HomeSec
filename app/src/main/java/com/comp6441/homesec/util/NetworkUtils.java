package com.comp6441.homesec.util;

import com.example.homesec.BuildConfig;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getPostAndroidMConnectionStatus(connectivityManager);
        } else {
            return getPreAndroidMConnectionStatus(connectivityManager);
        }
    }

    @RequiresApi (api = VERSION_CODES.M)
    private static boolean getPostAndroidMConnectionStatus(@NonNull ConnectivityManager cm) {
        Network network = cm.getActiveNetwork();
        NetworkCapabilities capabilities = network != null ? cm.getNetworkCapabilities(network) : null;

        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH);
        }

        return false;
    }

    private static boolean getPreAndroidMConnectionStatus(@NonNull ConnectivityManager cm) {
        NetworkInfo nwInfo = cm.getActiveNetworkInfo();
        return nwInfo.isConnected();
    }

    public static String getCurrentSsid(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = network != null ? connectivityManager.getNetworkCapabilities(network) : null;
            if (capabilities != null &&
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                        return connectionInfo.getSSID();
                    }
                }
            }
            return "default";
        } else {
            String ssid = null;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) : null;
            if (isConnected(context)) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    ssid = connectionInfo.getSSID();
                }
            }
            return ssid;
        }
    }

    public static boolean isLocnEnabled(Context context) {
        List locnProviders = null;
        try {
            LocationManager lm = (LocationManager) context.getApplicationContext().getSystemService(Activity.LOCATION_SERVICE);
            if (lm != null) {
                locnProviders = lm.getProviders(true);
            }

            return (locnProviders != null && locnProviders.size() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (BuildConfig.DEBUG) {
                if ((locnProviders == null) || (locnProviders.isEmpty())) {
                    Log.d(TAG, "Location services disabled");
                } else {
                    Log.d(TAG, "locnProviders: " + locnProviders.toString());
                }
            }
        }
        return false;
    }
}
