package sysproj.seonjoon.twice.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static PreferenceManager instance = null;

    private PreferenceManager() {
    }

    public static PreferenceManager getInstance() {
        if (instance == null)
            instance = new PreferenceManager();

        return instance;
    }

    private SharedPreferences getPreference(Context context, String filename) {
        return context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(Context context, String filename) {
        return getPreference(context, filename).edit();
    }

    public String getString(Context context, String filename, String tag) {
        return getPreference(context, filename).getString(tag, "");
    }

    public boolean getBoolean(Context context, String filename, String tag) {
        return getPreference(context, filename).getBoolean(tag, false);
    }

    public void saveString(Context context, String filename, String tag, String data) {
        getEditor(context, filename).putString(tag, data);
    }
}
