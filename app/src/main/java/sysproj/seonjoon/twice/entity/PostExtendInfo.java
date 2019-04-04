package sysproj.seonjoon.twice.entity;

public class PostExtendInfo {

    private String keyword;
    private int start;
    private int end;
    private String linkURL;

    public PostExtendInfo() {

    }

    public PostExtendInfo(int start, int end, String keyword, String linkURL) {
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
}
