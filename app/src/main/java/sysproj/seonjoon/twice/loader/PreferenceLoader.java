package sysproj.seonjoon.twice.loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import sysproj.seonjoon.twice.BuildConfig;

public class PreferenceLoader {
    public static final String KEY_TWITTER = "load_item_twitter";
    public static final String KEY_FACEBOOK = "load_item_facebook";
    public static final String KEY_INSTAGRAM = "load_item_instagram";

    private PreferenceLoader(){

    }

    public static String loadPreferenceFromDefault(Context context, String key)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(key, "");
    }
    public static String loadPreference(Context context, String key){

        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        return preferences.getString(key,"");
    }

    public static void removePreference(Context context, String key){

        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void savePreference(Context context, String key, String value){

        SharedPreferences preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
