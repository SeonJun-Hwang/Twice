package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.InstagramPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;

public abstract class InstagramViewHolder extends BaseViewHolder {

    private TextView likeCountTextview;
    private TextView commentCountTextView;

    public InstagramViewHolder(View itemView) {
        super(itemView);

        likeCountTextview = (TextView) itemView.findViewById(R.id.card_like_count);
        commentCountTextView = (TextView) itemView.findViewById(R.id.card_comment_count);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);

        setLikeComment(item);
    }

    @Override
    protected void setSubTitle(Post item) {

        String location = ((InstagramPost)item).getLocation();
        if (location != null)
            subTitleText.setText(location);
    }

    private void setLikeComment(Post post){

        PostRFS rfs = post.getRFS();

        likeCountTextview.setText("좋아요 " + rfs.getFavoriteCount() + "개");
        commentCountTextView.setText("댓글 " + rfs.getRepleCount() + "개");
    }
}
