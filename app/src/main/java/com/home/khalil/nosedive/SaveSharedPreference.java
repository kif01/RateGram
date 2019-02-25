package com.home.khalil.nosedive;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by khalil on 7/18/18.
 */

public class SaveSharedPreference {

    static final String PREF_USER= "pref";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setPref(Context ctx, String pref)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER, pref);
        editor.commit();
    }

    public static String getPref(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER, "");
    }
}
