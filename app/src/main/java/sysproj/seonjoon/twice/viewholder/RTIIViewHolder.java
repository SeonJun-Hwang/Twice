package sysproj.seonjoon.twice.viewholder;

import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.entity.UserProfile;
import sysproj.seonjoon.twice.view.custom.TimelinePageImageAdapter;

public class RTIIViewHolder extends TwitterViewHolder {

    private TextView retweetText;
    private ViewPager timelineImagePager;
    private TextView imageCountText;

    private static final String TAG = "RTIIHolder";

    public RTIIViewHolder(View itemView) {
        super(itemView);

        timelineImagePager = (ViewPager) itemView.findViewById(R.id.card_content_image_view);
        retweetText = (TextView) itemView.findViewById(R.id.card_retweet_status);
        imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);

        UserProfile retweetUser = ((TwitterPost) item).getRetweetUser();

        if (retweetUser == null)
            retweetText.setHeight(0);
        else {
            //Log.e(TAG, retweetUser.getName());
            retweetText.setText(retweetUser.getName() + " 님이 리트윗 하셨습니다.");
        }
    }

    @Override
    protected void setImageContent(Post post) {
        final ArrayList<PostMedia> imageList = post.getImageList();

        int imageCount = imageList.size();

        if (imageCount > 1)
            imageCountText.setText("1 / " + imageCount);

        for (int i = 0; i < imageCount; i++) {
            PostMedia media = imageList.get(i);
            String keyword = media.getKeyword();

            String removedText = contentText.getText().toString().replaceAll(keyword, "");
            contentText.setText(removedText);
        }

        TimelinePageImageAdapter adapter = new TimelinePageImageAdapter(context, imageList);
        timelineImagePager.setAdapter(adapter);

        timelineImagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
