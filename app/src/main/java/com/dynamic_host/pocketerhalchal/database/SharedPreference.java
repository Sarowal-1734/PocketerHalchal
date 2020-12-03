package com.dynamic_host.pocketerhalchal.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String PREFERENCE_NAME = "SharedPreference_Dark_AppLock_Language";
    public static final String PREF_THEME = "pref_theme_value";
    public static final String PREF_APPLOCK = "pref_applock_value";
    public static final String PREF_LANGUAGE = "pref_language_value";

    public static void setThemeValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_THEME, value);
        editor.apply();
    }

    public static int getThemeValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PREF_THEME, 0);
    }

    public static void setAppLockValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_APPLOCK, value);
        editor.apply();
    }

    public static int getAppLockValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PREF_APPLOCK, 0);
    }

    public static void setLanguageValue(Context context, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_LANGUAGE, value);
        editor.apply();
    }

    public static int getLanguageValue(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(PREF_LANGUAGE, 0);
    }


}
