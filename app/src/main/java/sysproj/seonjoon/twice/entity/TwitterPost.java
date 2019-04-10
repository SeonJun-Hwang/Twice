package sysproj.seonjoon.twice.entity;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class TwitterPost extends Post {

    protected TwitterPost(Post.Builder b) {
        super(b);
    }

    public static class Builder extends Post.Builder {

        public Builder(String user, String contentText, String createTime, PostRFS postRFS) {
            super(user, contentText, createTime, postRFS);
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
