package sysproj.seonjoon.twice.entity;

import android.util.Pair;

public class PostMedia {

    public static final int PHOTO = 1;
    public static final int VEDIO = 2;

    private String keyword;
    private String mediaURL;
    private int mediaTag;

    public PostMedia(int mediaTag, String keword, String mediaURL) {
        this.mediaTag = mediaTag;
        this.keyword = keword;
        this.mediaURL = mediaURL;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public int getMediaTag() {
        return mediaTag;
    }

    public String getKeyword() {
        return keyword;
    }
}
