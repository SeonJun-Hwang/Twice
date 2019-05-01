package sysproj.seonjoon.twice.viewholder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.view.TimelinePageImageAdapter;

public class ETIITOViewHolder extends BaseViewHolder {

    private ViewPager contentImageList;
    private TextView imageCountText;

    public ETIITOViewHolder(View itemView) {
        super(itemView);

        contentImageList = (ViewPager) itemView.findViewById(R.id.card_content_image_view);
        imageCountText = (TextView) itemView.findViewById(R.id.card_content_image_count_text);
    }

    @Override
    protected void setImageContent(Post post) {
        ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

//            Log.e(TAG, "Post " + i + " has " + imageList.size() + " Images");

            if (imageList.size() > 1)
                imageCountText.setText("+" + (imageList.size() - 1));

            for (int i = 0; i < imageList.size(); i++) {
                PostMedia item = imageList.get(i);
                String keyword = item.getKeyword();

                String contextText = contentText.getText().toString().replaceAll(keyword, "");
                contentText.setText(contextText);
            }

            TimelinePageImageAdapter adapter = new TimelinePageImageAdapter(context, imageList);
            contentImageList.setAdapter(adapter);
        } else
            imageCountText.setHeight(0); // Non - Images
    }

    @Override
    protected void setExtendField(Post extPost) {
        // Not Contain ExtendField
    }
}
