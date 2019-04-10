package sysproj.seonjoon.twice.staticdata;

import java.util.Date;

public class LastUpadteTime {

    private static Date[] times = new Date[StaticAppData.MAINACTIVITY_FRAGMENT_COUNT];

    public static void updateTime(int showFragment){
        times[showFragment] = new Date();
    }

    public static Date getTime(int showFragment){
        if (times[showFragment] == null)
            times[showFragment] = new Date();
        return times[showFragment];
    }
}
