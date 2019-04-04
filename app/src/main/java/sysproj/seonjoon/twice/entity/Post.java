package sysproj.seonjoon.twice.entity;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public abstract class Post {

    private int snsTag;
    private Date createDate;
    private String user;
    private String contentText;
    private String profileImage;
    private PostRFS postRFS;
    private ArrayList<PostMedia> imageList;
    private ArrayList<PostExtendInfo> extendInfo;

    public Post(int snsTag, String user, String contentText, @Nullable String profileImage, String createdTime, PostRFS postRFS, @Nullable ArrayList<PostMedia> imageList, @Nullable ArrayList<PostExtendInfo> extendInfos) {
        this.snsTag = snsTag;
        this.user = convertUser(user);
        this.contentText = convertText(contentText);
        this.imageList = imageList;
        this.profileImage = convertProfileImageUrl(profileImage);
        this.postRFS = postRFS;
        this.createDate = convertDate(createdTime);
        this.extendInfo = extendInfos;
    }

    private String convertUser(String str) {
        return str;
    }

    private String convertText(String str) {
        return str;
    }

    private String convertProfileImageUrl(String str) {
        return str;
    }

    abstract Date convertDate(String str);

    // Getters
    public String getUser() {
        return user;
    }

    public String getContentText() {
        return contentText;
    }

    public ArrayList<PostMedia> getImageList() {
        return imageList;
    }

    public int getSnsTag() {
        return snsTag;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public PostRFS getRFS() {
        return postRFS;
    }

    public ArrayList<PostExtendInfo> getExtendInfo() {
        return extendInfo;
    }

    // Method
    public int getViewType() {
        return imageList == null ? 1 : 2;
    }
}
