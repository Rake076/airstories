package com.example.android.air_stories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {


    static final String PREF_USER_NAME = "username";
    static final String  PREF_USER_ID = "userID";
    static final String PREF_JSON_STRING = "jsonString";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setUserID(Context ctx, int userID)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_ID, userID);
        editor.commit();
    }

    public static int getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_USER_ID, -1);
    }

    public static void setJSONString(Context ctx, String jsonString)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_JSON_STRING, jsonString);
        editor.commit();
    }


    public static String getJSONString(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_JSON_STRING, "");
    }

    public static void clear(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();


    }
}
