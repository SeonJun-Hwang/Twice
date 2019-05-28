package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.view.custom.CarouselAdapter;

public class ICViewHolder extends InstagramViewHolder {

    private ViewPager carouselPager;
    private TextView imageCountText;

    public ICViewHolder(View itemView) {
        super(itemView);

        carouselPager = (ViewPager) itemView.findViewById(R.id.card_content_carousel_pager);
        imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
    }

    @Override
    protected void setImageContent(Post post) {
        final ArrayList<PostMedia> mediaList = post.getImageList();
        CarouselAdapter adapter = new CarouselAdapter(context, mediaList);
        carouselPager.setAdapter(adapter);

        imageCountText.setText("1 / " + mediaList.size());

        carouselPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                imageCountText.setText((position + 1) + " / " + mediaList.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
