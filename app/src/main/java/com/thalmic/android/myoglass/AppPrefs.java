package com.thalmic.android.myoglass;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {
    private static final String PREF_MAC_ADDRESS = "PREF_MAC_ADDRESS";
    private static final String PREF_GLASS_MAC_ADDRESS = "PREF_GLASS_MAC_ADDRESS";

    private SharedPreferences mPrefs;

    public AppPrefs(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getMyoAddress() {
        return mPrefs.getString(PREF_MAC_ADDRESS, "");
    }

    public void setMyoAddress(String address) {
        mPrefs.edit().putString(PREF_MAC_ADDRESS, address).apply();
    }

    public String getGlassAddress() {
        return mPrefs.getString(PREF_GLASS_MAC_ADDRESS, "");
    }

    public void setGlassAddress(String address) {
        mPrefs.edit().putString(PREF_GLASS_MAC_ADDRESS, address).apply();
    }
}
