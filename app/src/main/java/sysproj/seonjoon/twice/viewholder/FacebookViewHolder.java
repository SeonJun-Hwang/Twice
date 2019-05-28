package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;

public abstract class FacebookViewHolder extends BaseViewHolder {

    protected Button reply;
    protected Button thumbsUp;
    protected Button share;

    private ImageView likeImage;
    private TextView likeCount;
    private TextView commentCount;

    public FacebookViewHolder(View itemView) {
        super(itemView);

        reply = (Button) itemView.findViewById(R.id.card_reply);
        thumbsUp = (Button) itemView.findViewById(R.id.card_thumbs_up);
        share = (Button) itemView.findViewById(R.id.card_share);

        likeImage = (ImageView) itemView.findViewById(R.id.card_content_likes_image);
        likeCount = (TextView) itemView.findViewById(R.id.card_content_likes);
        commentCount = (TextView) itemView.findViewById(R.id.card_content_reple_share);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);

        setLikeCommentCount(item);
        setRFSField();
    }

    @Override
    protected void setSubTitle(Post item) {
        subTitleText.setText(calTime(item.getCreateDate()));
    }

    private void setLikeCommentCount(Post item) {
        PostRFS postRFS = item.getRFS();

        int like = postRFS.getFavoriteCount();
        int comment = postRFS.getRepleCount();

        if (like > 0) {
            likeCount.setText(String.format("%d", like));
        } else {
            likeImage.setVisibility(View.GONE);
            likeCount.setVisibility(View.GONE);
        }

        if (comment > 0) {
            commentCount.setText(String.format("댓글 %d 개", comment));
        } else {
            commentCount.setVisibility(View.GONE);
        }

    }

    private void setRFSField() {
        thumbsUp.setText("Thumbs Up!");
        reply.setText("Comment");
        share.setText("Share");
    }
}
