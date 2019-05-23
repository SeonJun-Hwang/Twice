package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.FacebookLinkVO;
import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.Post;

public class FLViewHolder extends BaseViewHolder {

    private static final String TAG = "FLViewHolder";

    private TextView titleText;
    private TextView contentText;

    private ImageView linkImage;
    private TextView linkURL;
    private TextView linkTitle;
    private TextView linkDescription;

    public FLViewHolder(View itemView) {
        super(itemView);

        titleText = (TextView) itemView.findViewById(R.id.card_extend_title);
        contentText = (TextView) itemView.findViewById(R.id.card_extend_contents);

        linkImage = (ImageView) itemView.findViewById(R.id.inner_compo_link_image_view);
        linkURL = (TextView) itemView.findViewById(R.id.inner_compo_link);
        linkTitle = (TextView) itemView.findViewById(R.id.inner_compo_title);
        linkDescription = (TextView) itemView.findViewById(R.id.inner_compo_desc);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);
        setShareData(((FacebookPost) item).getShareLink());
    }

    @Override
    protected void setImageContent(Post post) {

    }

    @Override
    protected void setExtendField(Post extPost) {

    }

    private void setShareData(FacebookLinkVO linkVO) {

        if (linkVO.getImageSrc() != null && !linkVO.getImageSrc().isEmpty()) {
            Glide.with(context)
                    .load(linkVO.getImageSrc())
                    .fitCenter()
                    .into(linkImage);
        }

        if (linkVO.getLinkSrc() != null && !linkVO.getLinkSrc().isEmpty()) {
            String originLink = linkVO.getLinkSrc();
            String linkUrl = originLink.substring(originLink.indexOf("u="));
            int startPos = linkUrl.indexOf("%3A%2F%2F") + "%3A%2F%2F".length();
            int endPos = linkUrl.indexOf("%2F", startPos);
            if (endPos > 0)
                linkUrl = linkUrl.substring(startPos, endPos);
            else
                linkUrl = linkUrl.substring(startPos);
            linkURL.setText(linkUrl);
        }

        if (linkVO.getTitle() != null && !linkVO.getTitle().isEmpty())
            linkTitle.setText(linkVO.getTitle());

        if (linkVO.getDescription() != null && !linkVO.getDescription().isEmpty())
            linkDescription.setText(linkVO.getDescription());

    }
}
