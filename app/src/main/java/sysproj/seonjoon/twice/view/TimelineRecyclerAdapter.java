package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.TimeLineItem;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.StaticAppData;

public class TimelineRecyclerAdapter extends RecyclerView.Adapter<TimelineRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TimeLineItem> items;

    public TimelineRecyclerAdapter(Context context, ArrayList<TimeLineItem> items) {
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
        final TimeLineItem post = items.get(i);
        ArrayList<Bitmap> imageList = post.getImageList();

        // Log.e("Post", post.getUser() + " , " + post.getContentText()); // For Debug

        viewHolder.linearLayout.setBackgroundColor(getColor(post.getSnsTag()));
        viewHolder.innerLinearLayout.setBackgroundColor(context.getColor(R.color.timelineThemaBackWhite));
        viewHolder.logoButton.setImageDrawable(getLogo(post.getSnsTag()));
        viewHolder.titleText.setText(post.getUser());

        if (post.getContentText() != null)
            viewHolder.contentText.setText(post.getContentText());
        else
            viewHolder.contentText.setHeight(0);

        if (imageList != null && imageList.size() > 0)
            viewHolder.contextImage.setImageBitmap(imageList.get(0));

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardview;
        private LinearLayout linearLayout;
        private LinearLayout innerLinearLayout;
        private ImageButton logoButton;
        private ImageButton profileButton;
        private TextView titleText;
        private TextView contentText;
        private ImageView contextImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.main_time_line_card);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.card_back_linear);
            innerLinearLayout = (LinearLayout) itemView.findViewById(R.id.card_inner_linear);
            logoButton = (ImageButton) itemView.findViewById(R.id.card_title_logo);
            profileButton = (ImageButton) itemView.findViewById(R.id.card_profile_image);
            titleText = (TextView) itemView.findViewById(R.id.card_title_text);
            contentText = (TextView) itemView.findViewById(R.id.card_content_text);
            contextImage = (ImageView) itemView.findViewById(R.id.card_content_image);

            if (StaticAppData.TimeLineHeight == 0) {
                LinearLayout timeLineLinear = (LinearLayout) itemView.findViewById(R.id.card_inner_linear);
                StaticAppData.TimeLineHeight = (int) (timeLineLinear.getHeight() * 0.8);
            }
        }
    }

}
