package com.example.cookpad.until;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.cookpad.model.User;
import com.google.gson.Gson;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "com.example.cookpad";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String SAVE_OBJECT_USER = "objectUser";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(SAVE_OBJECT_USER, json);
        editor.commit();
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = pref.getString(SAVE_OBJECT_USER, "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public void saveBol(String key, boolean b) {

        editor.putBoolean(key, b);
        editor.commit();
    }

    public boolean getBol(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveString(String key, String val) {
        editor.putString(key, val);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }

    public void deleteAll(){
        editor.clear();
    }
}
