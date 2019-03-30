package sysproj.seonjoon.twice.staticdata;

import com.facebook.login.DefaultAudience;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getTwitterPermission()
    {
        List<String> permissions = new ArrayList<>();

        return permissions;
    }

}
