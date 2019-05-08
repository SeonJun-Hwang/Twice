package sysproj.seonjoon.twice.entity;

import java.util.Date;

public class TwitterPost extends Post {

    private UserProfile retweetUser;
    private PostExtendInfo relatedThread;

    protected TwitterPost(Builder b) {
        super(b);

        this.retweetUser = b.retweetUser;
        this.relatedThread = b.relatedThread;
    }

    public UserProfile getRetweetUser() {
        return retweetUser;
    }

    public PostExtendInfo getRelatedThread() { return relatedThread ; }

    public static class Builder extends Post.Builder {

        private UserProfile retweetUser;
        private PostExtendInfo relatedThread;

        public Builder(long id, int type, UserProfile user, String contentText, String createTime, PostRFS postRFS) {
            super(id, type, user, contentText, createTime, postRFS);
        }

        public Builder retweetUser(UserProfile retweetUser) {
            this.retweetUser = retweetUser;
            return this;
        }

        public Builder relatedThread(PostExtendInfo relThread){
            this.relatedThread = relThread;
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
