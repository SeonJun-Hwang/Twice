package sysproj.seonjoon.twice.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.entity.UserProfile;

public class RTOViewHodler extends BaseViewHolder {

    private TextView retweetText;
    private static final String TAG = "RTOHolder";

    public RTOViewHodler(View itemView) {
        super(itemView);

        retweetText = (TextView) itemView.findViewById(R.id.card_retweet_status);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);

        UserProfile retweetUser = ((TwitterPost)item).getRetweetUser();

        if (retweetUser == null)
            retweetText.setHeight(0);
        else {
            Log.e(TAG, retweetUser.getName());
            retweetText.setText(retweetUser.getName() + " 님이 리트윗 하셨습니다.");
        }
    }

    @Override
    protected void setImageContent(Post post) {

    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
