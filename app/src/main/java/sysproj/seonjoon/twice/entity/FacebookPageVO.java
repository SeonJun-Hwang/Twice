package sysproj.seonjoon.twice.entity;

public class FacebookPageVO {

    private String accessToken;
    private long pageId;
    private String name;

    public FacebookPageVO(String accessToken, String name, long pageId) {
        this.accessToken = accessToken;
        this.name = name;
        this.pageId = pageId;
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
