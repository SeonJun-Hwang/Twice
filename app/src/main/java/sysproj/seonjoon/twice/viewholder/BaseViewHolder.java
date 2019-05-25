package sysproj.seonjoon.twice.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostExtendInfo;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.staticdata.LastUpdate;
import sysproj.seonjoon.twice.view.MainActivity;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private final static String TAG = "BaseViewHolder";

    protected Context context;
    protected ImageView snsLogo;
    protected ImageView profileImage;
    protected TextView titleText;
    protected TextView contentText;
    protected TextView subTitleText;

    public BaseViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        snsLogo = (ImageView) itemView.findViewById(R.id.card_title_logo);
        profileImage = (ImageView) itemView.findViewById(R.id.card_profile_image);
        titleText = (TextView) itemView.findViewById(R.id.card_title_text);
        subTitleText = (TextView) itemView.findViewById(R.id.card_subtitle);
        contentText = (TextView) itemView.findViewById(R.id.card_content_text);
    }

    public void bind(Post item) {

        snsLogo.setImageDrawable(getLogo(item));
        titleText.setText(item.getUser().getName());

        setSubTitle(item);
        setContent(item.getContentText(), item.getExtendInfo());
        setProfileImage(item);
        setImageContent(item);
        setExtendField(item);
    }

    private Drawable getLogo(Post post) {
        Drawable result = null;

        if (post instanceof FacebookPost) {
            result = context.getDrawable(R.drawable.facebook);
        } else if (post instanceof TwitterPost) {
            result = context.getDrawable(R.drawable.twitter);
        } else {
            result = context.getDrawable(R.drawable.instagram);
        }

        return result;
    }

    protected abstract void setSubTitle(Post item);

    private void setContent(String content, ArrayList<PostExtendInfo> post) {

        if (content != null) {
            contentText.setText(content);
        } else {
            contentText.setHeight(0);
            return ;
        }

        if (post != null) {
            Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher matcher, String s) {
                    return "";
                }
            };

            SpannableString contentSpannable = new SpannableString(content);

            int lastTagPos = 0;

            for (PostExtendInfo item : post) {
                Pattern pattern = Pattern.compile(item.getKeyword());

                //Log.e(TAG, item.getKeyword());

                switch (item.getPostTag()) {
                    case PostExtendInfo.HASH_TAG:
                        try{
                        int startPos = content.indexOf('#' + item.getKeyword(), lastTagPos);
                        int endPos =  startPos + item.getKeyword().length() + 1;
                        addHashTags(contentSpannable, startPos, endPos, item.getKeyword());
                        lastTagPos = endPos;
                        } catch (Exception e){
                            Log.e(TAG, e.getMessage() + " / " + item.getKeyword());
                        }
                        break;
                    case PostExtendInfo.MENTION:
                    case PostExtendInfo.URLS:
                        //String checker = contentText.getText().toString();
                        //Log.e(TAG, "Key : " + item.getKeyword() + " / " + checker.contains(item.getKeyword()));
                        Linkify.addLinks(contentSpannable, pattern, item.getLinkURL(), null, transformFilter);
                        break;
                }
            }

            removeUnderlines(contentSpannable);
            convertSpannedText(contentText, contentSpannable);
        }
    }

    private void setProfileImage(Post post) {
        if (post.getUser().getProfileImage() != null)
            Glide.with(context).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(post.getUser().getProfileImage()).into(profileImage);
        else
            Glide.with(context).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(R.drawable.default_user).into(profileImage);
    }

    protected abstract void setImageContent(Post post);

    protected abstract void setExtendField(Post extPost);

    private void removeUnderlines(SpannableString s) {
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
    }

    private void addHashTags(SpannableString spannableString, int strPos, int endPos, String keyword) {
        HashTagURLSpan hashtagSpan = new HashTagURLSpan('#' + keyword);
        spannableString.setSpan(hashtagSpan, strPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void convertSpannedText(TextView textView, SpannableString spannableString){
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private class HashTagURLSpan extends ClickableSpan {

        private String keyword;

        public HashTagURLSpan(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public void onClick(@NonNull View view) {
            ((MainActivity) context).onClickHashTag(keyword);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);

            // TODO : Set Border & Black TextColor
            ds.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
            ds.setUnderlineText(false);
        }
    }

    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    final String calTime(Date time) {
        Date curTime = LastUpdate.getTime(MainActivity.getShowingFragmentNumber());
        long diff = (curTime.getTime() - time.getTime()) / 1000;

        if (diff < 10) // Before 10 Second
            return "a Moment ago";
        else if (diff < 60) // Before 1 Minute
            return diff + " seconds";
        else if (diff < (60 * 60)) // Before 1 Hour
            return (diff / 60) + " Minutes";
        else if (diff < (60 * 60 * 24)) // Before 1 Day
            return (diff / (60 * 60)) + " Hours";
        else if (diff < (60 * 60 * 24 * 2)) // Yesterday
            return "Yesterday";

        if (curTime.getYear() == time.getYear())
            return (new SimpleDateFormat("MMM dd 'at' k:mm a", Locale.US)).format(time);

        return (new SimpleDateFormat("MMM dd yyyy', at' k:mm a", Locale.US)).format(time);
    }

    final MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(context, "Twice");

        return new ProgressiveMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri);
    }
}


