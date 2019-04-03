package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Post post = items.get(i);
        ArrayList<String> imageList = post.getImageList();

        // Log.e("Post", post.getUser() + " , " + post.getContentText()); // For Debug

        viewHolder.linearLayout.setBackgroundColor(getColor(post.getSnsTag()));
        viewHolder.innerLinearLayout.setBackgroundColor(context.getColor(R.color.timelineThemaBackWhite));
        viewHolder.logoButton.setImageDrawable(getLogo(post.getSnsTag()));
        viewHolder.contentTime.setText(calTime(post.getCreateDate()));
        viewHolder.titleText.setText(post.getUser());

        setRFSField(viewHolder, post.getSnsTag(), post.getRFS());

        if (post.getContentText() != null)
            viewHolder.contentText.setText(post.getContentText());
        else
            viewHolder.contentText.setHeight(0);

        if (imageList != null && imageList.size() > 0) {

            Log.e(TAG, "Post " + i + " has " + imageList.size() + " Images");

            if (imageList.size() > 1)
                viewHolder.imageCountText.setText("+" + (imageList.size() - 1));

            TimelinePageImageAdapter adapter = new TimelinePageImageAdapter(context, imageList);
            viewHolder.contextImageList.setAdapter(adapter);
        }
        else
            viewHolder.imageCountText.setHeight(0); // Non - Images

        if (post.getProfileImage() != null)
            Glide.with(context).load(post.getProfileImage()).into(viewHolder.profileButton);
        else
            Glide.with(context).load(R.drawable.default_user).into(viewHolder.profileButton);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    private int getColor(int tag) {
        int result = 0;

        switch (tag) {
            case SNSTag.Facebook:
                result = context.getColor(R.color.colorFacebook);
                break;
            case SNSTag.Instagram:
                result = context.getColor(R.color.colorInstagram);
                break;
            case SNSTag.Twitter:
                result = context.getColor(R.color.colorTwitter);
        }

        return result;
    }

    private Drawable getLogo(int tag) {
        Drawable result = null;

        switch (tag) {
            case SNSTag.Facebook:
                result = context.getDrawable(R.drawable.facebook);
                break;
            case SNSTag.Instagram:
                result = context.getDrawable(R.drawable.instagram);
                break;
            case SNSTag.Twitter:
                result = context.getDrawable(R.drawable.twitter);
        }

        return result;
    }

    private String calTime(Date time) {
        Date curTime = new Date();
        long diff = (curTime.getTime() - time.getTime()) / 1000;

        Log.e("TimelineRecycler", curTime.toString() + " / " + time.toString());

        if (diff < 10) // Before 10 Second
            return "a Moment ago";
        else if ( diff < 60) // Before 1 Minute
            return diff + " seconds";
        else if ( diff < (60 * 60)) // Before 1 Hour
            return (diff / 60) + " Minutes";
        else if ( diff < (60 * 60 * 24)) // Before 1 Day
            return (diff / (60 * 60)) + " Hours";
        else if ( diff < (60 * 60 * 24 * 2)) // Yesterday
            return "Yesterday";

        if (curTime.getYear() > time.getYear())
            return (new SimpleDateFormat("MMM dd")).format(time);

        return (new SimpleDateFormat("YYYY MMM dd")).format(time);
    }

    private void setRFSField(ViewHolder viewHolder, int tag, PostRFS rfs){

        if (tag == SNSTag.Facebook){
            viewHolder.thumbsUp.setText("Thumbs Up!");
            viewHolder.reple.setText("Comment");
            viewHolder.share.setText("Share");
        }
        else if (tag == SNSTag.Twitter){
            int favor = rfs.getFavoriteCount();
            int reple = rfs.getRepleCount();
            int retweet = rfs.getSharedCount();

            viewHolder.thumbsUp.setText("Favorite " + (favor > 9999 ? String.format("%.1f", favor/1000f) + " K" : favor));
            viewHolder.reple.setText("Reple " + (reple > 9999 ? String.format("%.1f", reple/1000f) + " K" : reple));
            viewHolder.share.setText("Retweet " + (retweet > 9999 ? String.format("%.1f", retweet/ 1000f) + "K" : retweet));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardview;
        private LinearLayout linearLayout;
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
            linearLayout = (LinearLayout) itemView.findViewById(R.id.card_back_linear);
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

            if (StaticAppData.TimeLineHeight == 0) {
                LinearLayout timeLineLinear = (LinearLayout) itemView.findViewById(R.id.card_inner_linear);
                StaticAppData.TimeLineHeight = (int) (timeLineLinear.getHeight() * 0.8);
            }
        }
    }

}
