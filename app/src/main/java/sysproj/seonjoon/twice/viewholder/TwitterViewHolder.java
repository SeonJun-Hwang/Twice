package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.staticdata.LastUpdate;
import sysproj.seonjoon.twice.view.MainActivity;

public abstract class TwitterViewHolder extends BaseViewHolder {

    protected Button reply;
    protected Button thumbsUp;
    protected Button share;

    public TwitterViewHolder(View itemView) {
        super(itemView);

        reply = (Button) itemView.findViewById(R.id.card_reply);
        thumbsUp = (Button) itemView.findViewById(R.id.card_thumbs_up);
        share = (Button) itemView.findViewById(R.id.card_share);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);

        setRFSField(item);
    }

    @Override
    protected void setSubTitle(Post item) {
        subTitleText.setText(calTime(item.getCreateDate()));
    }

    private void setRFSField(Post post) {
        PostRFS rfs = post.getRFS();

        int favorCount = rfs.getFavoriteCount();
        int replyCount = rfs.getRepleCount();
        int retweetCount = rfs.getSharedCount();

        thumbsUp.setText("Favorite " + (favorCount > 9999 ? String.format("%.1f", favorCount / 1000f) + " K" : favorCount));
        reply.setText("Reple " + (replyCount > 9999 ? String.format("%.1f", replyCount / 1000f) + " K" : replyCount));
        share.setText("Retweet " + (retweetCount > 9999 ? String.format("%.1f", retweetCount / 1000f) + "K" : retweetCount));
    }
}
