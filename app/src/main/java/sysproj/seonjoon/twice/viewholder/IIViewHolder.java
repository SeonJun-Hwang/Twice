package sysproj.seonjoon.twice.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;

public class IIViewHolder extends InstagramViewHolder {

    private static final String TAG = "IIViewHolder";

    private ImageView imageView;

    public IIViewHolder(View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.card_content_image);
    }

    @Override
    protected void setImageContent(Post post) {
        PostMedia image = post.getImageList().get(0);

        Glide.with(context).load(image.getMediaURL()).fitCenter().into(imageView);
    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
