package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.view.TimelinePageImageAdapter;

public class FIMViewHolder extends BaseViewHolder {

    private static final String TAG = "TIIViewHolder";
    private ViewPager contentImageList;
    private TextView imageCountText;

    public FIMViewHolder(View itemView) {
        super(itemView);

        contentImageList = (ViewPager) itemView.findViewById(R.id.card_content_image_view);
        imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
    }

    @Override
    protected void setImageContent(Post post) {

        ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

            if (imageList.size() > 1)
                imageCountText.setText("+" + (imageList.size() - 1));
            else
                imageCountText.setVisibility(View.GONE);

            TimelinePageImageAdapter adapter = new TimelinePageImageAdapter(context, imageList);
            contentImageList.setAdapter(adapter);
        } else
            imageCountText.setHeight(0); // Non - Images

    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
