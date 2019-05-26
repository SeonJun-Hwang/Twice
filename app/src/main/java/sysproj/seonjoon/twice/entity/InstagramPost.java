package sysproj.seonjoon.twice.entity;

import java.util.Date;

public class InstagramPost extends Post {

    private String location;
    private static final String TAG = "InstagramPost";

    protected InstagramPost(Builder b) {
        super(b);

        location = b.location;
    }

    public String getLocation() {
        return location;
    }

    public static class Builder extends Post.Builder{

        private String location;

        public Builder(long id, int type, UserProfile user, String contentText, String createTime, PostRFS postRFS) {
            super(id, type, user, contentText, createTime, postRFS);
        }

        public Builder location(String location){
            this.location = location;
            return this;
        }

        @Override
        protected Date convertDate(String str) {
            long time = Long.parseLong(str);
            time += 70 * 365 * 24 * 60 * 60;
            time *= 1000;

            return new Date(time);
        }

        @Override
        public Post build() {
            return new InstagramPost(this);
        }
    }
}
