package sysproj.seonjoon.twice.entity;

public class PostExtendInfo {

    public static final int HASH_TAG = 1;
    public static final int MENTION = 2;
    public static final int URLS = 3;

    private int postTag;
    private String keyword;
    private int start;
    private int end;
    private String linkURL;

    public PostExtendInfo() {

    }

    public PostExtendInfo(int postTag, int start, int end, String keyword, String linkURL) {
        this.postTag = postTag;
        this.start = start;
        this.end = end;
        this.keyword = keyword;
        this.linkURL = linkURL;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public int getPostTag() {
        return postTag;
    }
}
