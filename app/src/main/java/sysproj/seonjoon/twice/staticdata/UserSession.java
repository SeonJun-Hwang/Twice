package sysproj.seonjoon.twice.staticdata;

import com.facebook.AccessToken;
import com.twitter.sdk.android.core.TwitterSession;

public class UserSession {

    public static String TwitterAPI = "H3qNM38a3TzDXpWz6yY1hknFy";
    public static String TwitterAPISecret = "GU4uxItEP3ZM926o1NcUP2gGbBoivm4cWge9dzxsvpJFLHLzRe";

    public static AccessToken FacebookToken = null;
    public static TwitterSession TwitterToken = null;

    public static String UserFileName = "AuthFile";
    public static final String UserIDTag = "AuthUID" ;
    public static final String UserPasswordTag = "AuthPassword";

    public static final int MIN_PASSWORD_LENGTH = 8;
}
