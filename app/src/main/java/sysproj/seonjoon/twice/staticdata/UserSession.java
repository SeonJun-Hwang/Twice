package sysproj.seonjoon.twice.staticdata;

import com.facebook.AccessToken;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.FacebookPageVO;
import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.UserProfile;

public class UserSession {

    public static AccessToken FacebookToken = null;
    public static UserProfile FacebookProfile = null;
    public static ArrayList<FacebookPageVO> FacebookPageProfile = null;
    public static TwitterSession TwitterToken = null;
    public static UserProfile TwitterProfile = null;
    public static String InstagramToken = null;
    public static UserProfile InstagramProfile = null;

    public static final String UserFileName = "AuthFile";
    public static final String UserIDTag = "AuthUID";
    public static final String UserPasswordTag = "AuthPassword";

    public static final int MIN_PASSWORD_LENGTH = 8;

    public static void sessionClear() {
        FacebookToken = null;
        FacebookProfile = null;
        FacebookPageProfile = null;
        TwitterToken = null;
        TwitterProfile = null;
        InstagramProfile = null;
        InstagramProfile = null;
    }
}
