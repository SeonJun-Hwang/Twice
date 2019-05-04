package sysproj.seonjoon.twice.staticdata;

import java.util.Date;

public class LastUpdate {

    public static final long NONE = -1;
    private static Date[] times = new Date[StaticAppData.MAINACTIVITY_FRAGMENT_COUNT];
    private static long[] maxIds = new long[StaticAppData.SNS_PLATFORM_COUNT];

    static {
        for (int i = 0; i < maxIds.length; i++ )
            maxIds[i] = NONE;
    }

    public static void updateTime(int showFragment){
        times[showFragment] = new Date();
    }

    public static Date getTime(int showFragment){
        if (times[showFragment] == null)
            times[showFragment] = new Date();
        return times[showFragment];
    }

    public static long getMaxIds(int platform) { return maxIds[platform];}
    public static void setMaxIds(int platform, long id) { maxIds[platform] = id;}
}
