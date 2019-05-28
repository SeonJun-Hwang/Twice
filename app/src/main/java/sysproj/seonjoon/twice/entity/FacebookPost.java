package sysproj.seonjoon.twice.entity;

import androidx.annotation.Nullable;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FacebookPost extends Post {

    private final static String TAG = "FacebookPost";

    private FacebookLinkVO shareLink;

    private FacebookPost(Builder b) {
        super(b);

        this.shareLink = b.shareLink;
    }

    public FacebookLinkVO getShareLink() {
        return shareLink;
    }


    public static class Builder extends Post.Builder {

        private FacebookLinkVO shareLink;

        public Builder(long id, int type, UserProfile user, String contentText, String createTime, PostRFS postRFS) {
            super(id, type, user, contentText, createTime, postRFS);
        }

        @Override
        protected Date convertDate(String str) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date res = new Date();
            try {
                res = format.parse(str);
            } catch (ParseException e) {
                Log.e(TAG, str + "Date Convert Error");
            }

            return res;
        }

        @Override
        public Post.Builder imageList(ArrayList imageList) {
            return super.imageList(imageList);
        }

        public Post.Builder shareLink(FacebookLinkVO shareLink) {
            this.shareLink = shareLink;
            return this;
        }

        @Override
        public Post build() {
            return new FacebookPost(this);
        }

    }
}
