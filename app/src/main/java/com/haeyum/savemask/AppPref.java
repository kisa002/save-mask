package com.haeyum.savemask;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final String KEY_TERMS_AGREE = "terms_agree";
    private final String KEY_BIRTH = "birth";

    public AppPref(Context activity) {
        String PREF_NAME = "save-mask";
        preferences = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }

    public void setTermsAgree(boolean isAgree) {
        editor.putBoolean(KEY_TERMS_AGREE, isAgree);
        editor.commit();
    }

    public void setBirth(int birth) {
        editor.putInt(KEY_BIRTH, birth);
        editor.commit();
    }

    public Boolean getTermsAgree() {
        return preferences.getBoolean(KEY_TERMS_AGREE, false);
    }

    public int getBirth() {
        return preferences.getInt(KEY_BIRTH, -1);
    }
}
