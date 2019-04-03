package sysproj.seonjoon.twice.entity;

import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;

public class Post {

    private int snsTag;
    private Date createDate;
    private String user;
    private String contentText;
    private String profileImage;
    private PostRFS postRFS;
    private ArrayList<String> imageList;

    public Post(int snsTag, String user, String contentText, @Nullable String profileImage, String createdTime, PostRFS postRFS, @Nullable ArrayList<String> imageList) {
        this.snsTag = snsTag;
        this.user = user;
        this.contentText = contentText;
        this.imageList = imageList;
        this.profileImage = profileImage;
        this.postRFS = postRFS;
        this.createDate = new Date(Date.parse(createdTime));
        try {
            this.createDate = new Date(Date.parse(createdTime));
        } catch (Exception e) {
            this.createDate = new Date();
        }
    }

    public String getUser() {
        return user;
    }

    public String getContentText() {
        return contentText;
    }

    public ArrayList<String> getImageList() {
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

    public PostRFS getRFS(){
        return postRFS;
    }
}
