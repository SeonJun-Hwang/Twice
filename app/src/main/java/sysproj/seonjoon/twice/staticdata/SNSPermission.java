package sysproj.seonjoon.twice.staticdata;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.loader.PreferenceLoader;

public class SNSPermission {

    public static List<String> getFacebookPermission() {
        List<String> permissions = new ArrayList<>();

        permissions.add("email");
        permissions.add("user_events");
        permissions.add("user_friends");
        permissions.add("user_gender");
        permissions.add("user_likes");
        permissions.add("user_link");
        permissions.add("user_photos");
        permissions.add("user_posts");
        permissions.add("user_tagged_places");
        permissions.add("user_videos");
        //permissions.add("read_stream");
        permissions.add("public_profile");

        return permissions;
    }

    public static List<String> getTwitterPermission() {
        List<String> permissions = new ArrayList<>();

        return permissions;
    }

    public static ArrayList<String> getFacebookField(Context context){
        ArrayList<String> fields = new ArrayList<>();

        fields.add("feed.limits(" + PreferenceLoader.loadPreference(context, PreferenceLoader.KEY_FACEBOOK) + ")");
        fields.add("id");
        fields.add("name");
        fields.add("full_picture");
        fields.add("created_time");
        fields.add("comments");

        return fields;
    }

}
