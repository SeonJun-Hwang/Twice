package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.view.custom.TimelinePageImageAdapter;

public class FIMViewHolder extends FacebookViewHolder {

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

        final ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

            if (imageList.size() == 1) imageCountText.setVisibility(View.GONE);

            TimelinePageImageAdapter adapter = new TimelinePageImageAdapter(context, imageList);
            contentImageList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    imageCountText.setText((position + 1) + " / " + imageList.size());
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            contentImageList.setAdapter(adapter);
        } else
            imageCountText.setHeight(0); // Non - Images

    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
