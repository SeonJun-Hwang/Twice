package sysproj.seonjoon.twice.viewholder;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;

public class FOViewHolder extends BaseViewHolder {
    private CardView cardview;
    private LinearLayout innerLinearLayout;
    private ImageView logoButton;
    private ImageView profileButton;
    private TextView titleText;
    private TextView contentText;
    private TextView contentTime;
    private TextView imageCountText;
    private ViewPager contextImageList;

    private Button reple;
    private Button thumbsUp;
    private Button share;

    public FOViewHolder(@NonNull View itemView) {
        super(itemView);
        cardview = (CardView) itemView.findViewById(R.id.main_timeline_card);
        innerLinearLayout = (LinearLayout) itemView.findViewById(R.id.card_inner_linear);
        logoButton = (ImageView) itemView.findViewById(R.id.card_title_logo);
        profileButton = (ImageView) itemView.findViewById(R.id.card_profile_image);
        titleText = (TextView) itemView.findViewById(R.id.card_title_text);
        contentTime = (TextView) itemView.findViewById(R.id.card_title_time);
        contentText = (TextView) itemView.findViewById(R.id.card_content_text);
        imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
        contextImageList = (ViewPager) itemView.findViewById(R.id.card_content_image_view);

        reple = (Button) itemView.findViewById(R.id.card_reply);
        thumbsUp = (Button) itemView.findViewById(R.id.card_thumbs_up);
        share = (Button) itemView.findViewById(R.id.card_share);

    }

    @Override
    protected void setImageContent(Post post) {

    }

    @Override
    protected void setExtendField(Post extPost) {

    }


}
