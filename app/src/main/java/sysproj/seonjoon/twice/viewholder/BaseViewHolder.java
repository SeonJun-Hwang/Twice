package sysproj.seonjoon.twice.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.staticdata.LastUpadteTime;
import sysproj.seonjoon.twice.view.MainActivity;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder{

    protected Context context;
    protected ImageView snsLogo;
    protected ImageView profileImage;
    protected TextView titleText;
    protected TextView contentText;
    protected TextView contentTime;

    protected Button reply;
    protected Button thumbsUp;
    protected Button share;

    public BaseViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        snsLogo = (ImageView) itemView.findViewById(R.id.card_title_logo);
        profileImage = (ImageView) itemView.findViewById(R.id.card_profile_image);
        titleText = (TextView) itemView.findViewById(R.id.card_title_text);
        contentTime = (TextView) itemView.findViewById(R.id.card_title_time);
        contentText = (TextView) itemView.findViewById(R.id.card_content_text);

        reply = (Button) itemView.findViewById(R.id.card_reply);
        thumbsUp = (Button) itemView.findViewById(R.id.card_thumbs_up);
        share = (Button) itemView.findViewById(R.id.card_share);
    }

    public void bind(Post item){
        //innerLinearLayout.setBackgroundColor(context.getColor(R.color.timelineThemeBackWhite));

        snsLogo.setImageDrawable(getLogo(item));
        contentTime.setText(calTime(item.getCreateDate()));
        titleText.setText(item.getUser().getName());

        setRFSField(item);
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

    private String calTime(Date time) {
        Date curTime = LastUpadteTime.getTime(MainActivity.getShowingFragmentNumber());
        long diff = (curTime.getTime() - time.getTime()) / 1000;

//        Log.e("TimelineRecycler", diff + " / " + curTime.toString() + " / " + time.toString());

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

    private void setContent(String content, ArrayList<PostExtendInfo> post) {

        if (content != null) {
            contentText.setText(content);
        } else
            contentText.setHeight(0);

        if (post != null) {
            Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher matcher, String s) {
                    return "";
                }
            };

            for (PostExtendInfo item : post) {
                Pattern pattern = Pattern.compile(item.getKeyword());

                switch (item.getPostTag()) {
                    case PostExtendInfo.HASH_TAG:
                        //convertHashtag(contentText, item.getStart(), item.getEnd(), item.getKeyword());
                        break;
                    case PostExtendInfo.MENTION:
                    case PostExtendInfo.URLS:
                        removeUnderlines(contentText);
                        Linkify.addLinks(contentText, pattern, item.getLinkURL(), null, transformFilter);
                        break;
                }
            }
        }
    }

    private void setRFSField(final Post post) {

        if (post instanceof FacebookPost) {
            thumbsUp.setText("Thumbs Up!");
            reply.setText("Comment");
            share.setText("Share");
        } else if (post instanceof TwitterPost) {
            PostRFS rfs = post.getRFS();

            int favorCount = rfs.getFavoriteCount();
            int replyCount = rfs.getRepleCount();
            int retweetCount = rfs.getSharedCount();

            thumbsUp.setText("Favorite " + (favorCount > 9999 ? String.format("%.1f", favorCount / 1000f) + " K" : favorCount));
            reply.setText("Reple " + (replyCount > 9999 ? String.format("%.1f", replyCount / 1000f) + " K" : replyCount));
            share.setText("Retweet " + (retweetCount > 9999 ? String.format("%.1f", retweetCount / 1000f) + "K" : retweetCount));
        }
    }

    private void setProfileImage(Post post){
        if (post.getUser().getProfileImage() != null)
            Glide.with(context).load(post.getUser().getProfileImage()).into(profileImage);
        else
            Glide.with(context).load(R.drawable.default_user).into(profileImage);
    }

    protected abstract void setImageContent(Post post);

    protected abstract void setExtendField(Post extPost);

    private void removeUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private void convertHashtag(TextView textView, int strPos, int endPos, String keyword) {
        SpannableString tagSpan = new SpannableString('#' + keyword);
        HashTagURLSpan hashtagSpan = new HashTagURLSpan(keyword);

        tagSpan.setSpan(hashtagSpan, strPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(tagSpan);
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
}


