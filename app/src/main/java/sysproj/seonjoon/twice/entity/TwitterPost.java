package sysproj.seonjoon.twice.entity;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class TwitterPost extends Post {

    public TwitterPost(int snsTag, String user, String contentText, @Nullable String profileImage, String createdTime, PostRFS postRFS, @Nullable ArrayList<PostMedia> imageList, @Nullable ArrayList<PostExtendInfo> extendInfo) {
        super(snsTag, user, contentText, profileImage, createdTime, postRFS, imageList, extendInfo);
    }

    @Override
    Date convertDate(String str) {
        Date res = null;

        try{
            res = new Date(Date.parse(str));
        } catch (Exception e){
            res = new Date();
        }

        return res;
    }
}
