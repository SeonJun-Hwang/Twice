package sysproj.seonjoon.twice.entity;

public class FacebookLinkVO {

    private String name;
    private String description;
    private String imageSrc;
    private String title;
    private String linkSrc;

    private FacebookLinkVO() {
    }

    public FacebookLinkVO(Builder b) {
        this.name = b.name;
        this.description = b.description;
        this.imageSrc = b.imageSrc;
        this.title = b.title;
        this.linkSrc = b.linkSrc;
    }

    public String getDescription() {
        return description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkSrc() {
        return linkSrc;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private String name;
        private String description;
        private String imageSrc;
        private String title;
        private String linkSrc;

        public Builder() {
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder imageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder linkSrc(String linkSrc) {
            this.linkSrc = linkSrc;
            return this;
        }

        public FacebookLinkVO build() {
            return new FacebookLinkVO(this);
        }
    }
}
