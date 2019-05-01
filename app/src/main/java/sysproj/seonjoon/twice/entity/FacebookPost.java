package sysproj.seonjoon.twice.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sysproj.seonjoon.twice.entity.Post.Builder;
import sysproj.seonjoon.twice.staticdata.SNSTag;

public class FacebookPost extends Post{

    private final static String TAG = "FacebookPost";

    private FacebookPost(Builder b){
        super(b);
    }

    public static class Builder extends Post.Builder{

        public Builder(int type,UserProfile user, String contentText, String createTime, PostRFS postRFS) {
            super(type, user, contentText, createTime, postRFS);
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
        public Post build() {
            return new FacebookPost(this);
        }

    }
}
