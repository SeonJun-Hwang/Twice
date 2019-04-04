package sysproj.seonjoon.twice.entity;

import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FacebookPost extends Post {

    private final static String TAG = "FacebookPost";

    public FacebookPost(int snsTag, String user, String contentText, @Nullable String profileImage, String createdTime, PostRFS postRFS
            , @Nullable ArrayList<PostMedia> imageList, @Nullable ArrayList<PostExtendInfo> extendInfos) {
        super(snsTag, user, contentText, profileImage, createdTime, postRFS, imageList, extendInfos);
    }

    @Override
    Date convertDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date res = new Date();
        try {
            res = format.parse(str);
        } catch (ParseException e) {
            Log.e(TAG, str + "Date Convert Error");
        }

        return res;
    }
}
