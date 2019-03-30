package sysproj.seonjoon.twice.entity;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class TimeLineItem {

    private int snsTag;
    private String user;
    private String contentText;
    private String profileImage;
    private ArrayList<Bitmap> imageList;

    public TimeLineItem(int snsTag, String user, String contentText,@Nullable String profileImage ,@Nullable ArrayList<Bitmap> imageList) {
        this.snsTag = snsTag;
        this.user = user;
        this.contentText = contentText;
        this.imageList = imageList;
        this.profileImage = profileImage;
    }

    public String getUser() {
        return user;
    }

    public String getContentText() {
        return contentText;
    }

    public ArrayList<Bitmap> getImageList() {
        return imageList;
    }

    public int getSnsTag() {
        return snsTag;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
