package tn.esprit.freelance;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "userSession";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveUser(String fullName, String email, String role) {
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString("role", role);  // Ajouter le rôle
        editor.apply();
    }

    public String getRole() {
        return preferences.getString("role", "");  // Retourne le rôle de l'utilisateur
    }


    public String getFullName() {
        return preferences.getString(KEY_FULLNAME, "");
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public void clearSession() {
        editor.clear().apply();
    }
}
