package sysproj.seonjoon.twice.staticdata;

import com.facebook.AccessToken;
import com.twitter.sdk.android.core.TwitterSession;

import sysproj.seonjoon.twice.entity.UserProfile;

public class UserSession {

    public static AccessToken FacebookToken = null;
    public static UserProfile FacebookProfile = null;
    public static TwitterSession TwitterToken = null;
    public static String InstagramToekn = null;

    public static final String UserFileName = "AuthFile";
    public static final String UserIDTag = "AuthUID";
    public static final String UserPasswordTag = "AuthPassword";

    public static final int MIN_PASSWORD_LENGTH = 8;
}
