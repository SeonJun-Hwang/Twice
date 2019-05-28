package sysproj.seonjoon.twice.viewholder;

import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.view.custom.TimelinePageImageAdapter;

public class TIIViewHolder extends TwitterViewHolder {

    private static final String TAG = "TIIViewHolder";
    private ViewPager contentImageList;
    private TextView imageCountText;

    public TIIViewHolder(View itemView) {
        super(itemView);

        contentImageList = (ViewPager) itemView.findViewById(R.id.card_content_image_view);
        imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);
    }

    @Override
    protected void setImageContent(Post post) {
        final ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

            if (imageList.size() > 1)
                imageCountText.setText("1 / " + imageList.size());
            else
                imageCountText.setVisibility(View.GONE);

            for (int i = 0; i < imageList.size(); i++) {
                PostMedia item = imageList.get(i);
                String keyword = item.getKeyword();

                String contextText = contentText.getText().toString().replaceAll(keyword, "");
                contentText.setText(contextText);

                //Log.e(TAG, imageList.get(i).getMediaURL());
            }

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
        // Not Contain Extend Field
    }
}
