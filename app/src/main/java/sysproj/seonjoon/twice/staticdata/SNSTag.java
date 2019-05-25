package sysproj.seonjoon.twice.staticdata;

public class SNSTag {

    public static final int None = 0;
    public static final int Origin = 1;
    public static final int Image = 2;
    public static final int Video = 3;
    public static final int Link = 4;
    public static final int Carousel = 5;

    public static final int Facebook = 0;
    public static final int Instagram = 1;
    public static final int Twitter = 2;

    public static final int Platform = 100;
    public static final int Extension = 10;

    // Facebook Tag
    public static final String FacebookTokenTag = "FbTk";
    public static final String FacebookUIDTag = "FbUID";
    public static final String FacebookAIDTag = "FbAID";
    public static final String FacebookPermissionTag = "FbPerm";
    public static final String FacebookDPermissionTag = "FbDPerm";
    public static final String FacebookTokenSourceTag = "FbTkSrc";
    public static final String FacebookExpTimeTag = "FbExpTime";
    public static final String FacebookLastTimeTag = "FbLastTime";
    public static final String FacebookDataAccExpTimeTag = "FbDataAccExpTime";
    public static final String FacebookResultTag = "FbRes";

    // Twitter Tag
    public static final String TWITTER_BASE_URL = "https://api.twitter.com/1.1";
    public static final String TWITTER_URL_TIMELINE = "/statuses/home_timeline.json";
    public static final String TWITTER_URL_SEARCH = "/search/tweets.json";
    public static final String TWITTER_URL_USER_INFO= "/account/verify_credentials.json";
    public static final String TwitterTokenTag = "TwTk";
    public static final String TwitterTokenSecretTag = "TwTkScrt";
    public static final String TwitterUIDTag = "TwUID";
    public static final String TwitterUNameTag = "TwUName";
    public static final String TwitterResultTag = "TwRes";

    // Instagram Tag
    public static final String InstagramTokenTag = "ITk";
    // Twice Tag
    public static final String TWICE_EMAIL_TAIL= "@twice.com";

}
