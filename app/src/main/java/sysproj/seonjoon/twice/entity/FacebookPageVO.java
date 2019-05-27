package sysproj.seonjoon.twice.entity;

public class FacebookPageVO {

    private String accessToken;
    private long pageId;
    private String name;
    private String pageImage;

    public FacebookPageVO(String accessToken, String name, String pageImage, long pageId) {
        this.accessToken = accessToken;
        this.name = name;
        this.pageImage = pageImage;
        this.pageId = pageId;
    }

    public String getPageImage() {
        return pageImage;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getPageId() {
        return pageId;
    }

    public String getName() {
        return name;
    }
}
