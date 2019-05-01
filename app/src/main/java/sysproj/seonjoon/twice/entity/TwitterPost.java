package sysproj.seonjoon.twice.entity;

import java.util.Date;

public class TwitterPost extends Post {

    private UserProfile retweetUser;

    protected TwitterPost(Builder b) {
        super(b);

        this.retweetUser = b.retweetUser;
    }

    public UserProfile getRetweetUser() { return retweetUser ;}

    public static class Builder extends Post.Builder {

        private UserProfile retweetUser;

        public Builder(int type,UserProfile user, String contentText, String createTime, PostRFS postRFS) {
            super(type, user, contentText, createTime, postRFS);
        }

        public Builder retweetUser(UserProfile retweetUser){
            this.retweetUser = retweetUser;
            return this;
        }

        @Override
        protected Date convertDate(String str) {
            Date res = null;

            try {
                res = new Date(Date.parse(str));
            } catch (Exception e) {
                res = new Date();
            }

            return res;
        }

        @Override
        public Post build() {
            return new TwitterPost(this);
        }
    }
}
