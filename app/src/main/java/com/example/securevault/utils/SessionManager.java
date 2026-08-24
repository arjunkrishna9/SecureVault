package com.example.securevault.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "SecureVaultSession";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String KEY_USER_ID = "userId";

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {

        sharedPreferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        editor = sharedPreferences.edit();
    }

    // SAVE LOGIN SESSION
    public void createLoginSession(int userId) {

        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        editor.putInt(KEY_USER_ID, userId);

        editor.apply();
    }

    // CHECK LOGIN
    public boolean isLoggedIn() {

        return sharedPreferences.getBoolean(
                KEY_IS_LOGGED_IN,
                false
        );
    }

    // GET USER ID
    public int getUserId() {

        return sharedPreferences.getInt(
                KEY_USER_ID,
                -1
        );
    }

    // LOGOUT
    public void logoutUser() {

        editor.clear();

        editor.apply();
    }
}