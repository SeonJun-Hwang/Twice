package sysproj.seonjoon.twice.entity;

public class PostMedia {

    public static final int PHOTO = 1;
    public static final int VIDEO = 2;

    private String keyword;
    private String mediaURL;
    private int mediaTag;

    public PostMedia(int mediaTag, String keyword, String mediaURL) {
        this.mediaTag = mediaTag;
        this.keyword = keyword;
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
