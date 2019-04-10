package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostExtendInfo;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.staticdata.LastUpadteTime;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.StaticAppData;

public class TimelineRecyclerAdapter extends RecyclerView.Adapter<TimelineRecyclerAdapter.ViewHolder> {

    private static final String TAG = "TimeRecyclerAdapter";
    private Context context;
    private ArrayList<Post> items;

    public TimelineRecyclerAdapter(Context context, ArrayList<Post> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timeline, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Post post = items.get(i);

        // Log.e("Post", post.getUser() + " , " + post.getContentText()); // For Debug
        viewHolder.innerLinearLayout.setBackgroundColor(context.getColor(R.color.timelineThemeBackWhite));
        viewHolder.logoButton.setImageDrawable(getLogo(post));
        viewHolder.contentTime.setText(calTime(post.getCreateDate()));
        viewHolder.titleText.setText(post.getUser());

        setRFSField(viewHolder, post);
        setContent(viewHolder.contentText, post.getContentText(), post.getExtendInfo());
        setImageContent(viewHolder, post);

    }

    @Override
    public int getItemCount() {
        return this.items.size();
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

    private void setContent(TextView contentText, String content, ArrayList<PostExtendInfo> post) {

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

    private void setRFSField(ViewHolder viewHolder, final Post post) {

        if (post instanceof FacebookPost) {
            viewHolder.thumbsUp.setText("Thumbs Up!");
            viewHolder.reple.setText("Comment");
            viewHolder.share.setText("Share");
        } else if (post instanceof TwitterPost) {
            PostRFS rfs = post.getRFS();

            int favor = rfs.getFavoriteCount();
            int reple = rfs.getRepleCount();
            int retweet = rfs.getSharedCount();

            viewHolder.thumbsUp.setText("Favorite " + (favor > 9999 ? String.format("%.1f", favor / 1000f) + " K" : favor));
            viewHolder.reple.setText("Reple " + (reple > 9999 ? String.format("%.1f", reple / 1000f) + " K" : reple));
            viewHolder.share.setText("Retweet " + (retweet > 9999 ? String.format("%.1f", retweet / 1000f) + "K" : retweet));
        }
    }

    private void setImageContent(ViewHolder viewHolder, Post post) {

        ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

//            Log.e(TAG, "Post " + i + " has " + imageList.size() + " Images");

            if (imageList.size() > 1)
                viewHolder.imageCountText.setText("+" + (imageList.size() - 1));

            for (int i = 0; i < imageList.size(); i++) {
                PostMedia item = imageList.get(i);
                String keyword = item.getKeyword();

                String contextText = viewHolder.contentText.getText().toString().replaceAll(keyword, "");
                viewHolder.contentText.setText(contextText);
            }

            TimelinePageImageAdapter adapter = new TimelinePageImageAdapter(context, imageList);
            viewHolder.contextImageList.setAdapter(adapter);
        } else
            viewHolder.imageCountText.setHeight(0); // Non - Images

        if (post.getProfileImage() != null)
            Glide.with(context).load(post.getProfileImage()).into(viewHolder.profileButton);
        else
            Glide.with(context).load(R.drawable.default_user).into(viewHolder.profileButton);
    }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardview;
        private LinearLayout innerLinearLayout;
        private ImageButton logoButton;
        private ImageButton profileButton;
        private TextView titleText;
        private TextView contentText;
        private TextView contentTime;
        private TextView imageCountText;
        private ViewPager contextImageList;

        private Button reple;
        private Button thumbsUp;
        private Button share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.main_time_line_card);
            innerLinearLayout = (LinearLayout) itemView.findViewById(R.id.card_inner_linear);
            logoButton = (ImageButton) itemView.findViewById(R.id.card_title_logo);
            profileButton = (ImageButton) itemView.findViewById(R.id.card_profile_image);
            titleText = (TextView) itemView.findViewById(R.id.card_title_text);
            contentTime = (TextView) itemView.findViewById(R.id.card_title_time);
            contentText = (TextView) itemView.findViewById(R.id.card_content_text);
            imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
            contextImageList = (ViewPager) itemView.findViewById(R.id.card_content_image_view);

            reple = (Button) itemView.findViewById(R.id.card_reply);
            thumbsUp = (Button) itemView.findViewById(R.id.card_thumbs_up);
            share = (Button) itemView.findViewById(R.id.card_share);

            if (StaticAppData.TimeLineHeight == 0)
                StaticAppData.TimeLineHeight = (int) (innerLinearLayout.getHeight() * 0.8);

        }
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
