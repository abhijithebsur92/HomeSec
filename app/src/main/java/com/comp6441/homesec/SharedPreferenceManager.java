package com.comp6441.homesec;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import androidx.preference.PreferenceManager;

public class SharedPreferenceManager {

    private static final String KEY_ENABLE_ALARM = "enable_alarm";
    private static final String KEY_ADD_SSID = "add_ssid";
    private static SharedPreferenceManager sInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    //Make default constructor private
    private SharedPreferenceManager() {

    }

    private SharedPreferenceManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceManager(context);
        }

        return sInstance;
    }

    public void setEnableAlarm(boolean enable) {
        mEditor.putBoolean(KEY_ENABLE_ALARM, enable);
        mEditor.apply();
    }

    public boolean isAlarmEnabled() {
        return mSharedPreferences.getBoolean(KEY_ENABLE_ALARM, true);
    }

    public Set<String> getSSIDs() {
        return mSharedPreferences.getStringSet(KEY_ADD_SSID, null);
    }

    public void setSSIDs(Set<String> ssids) {
        mEditor.putStringSet(KEY_ADD_SSID, ssids);
        mEditor.apply();
    }
}
