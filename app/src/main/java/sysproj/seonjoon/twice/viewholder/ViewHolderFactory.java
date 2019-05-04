package sysproj.seonjoon.twice.viewholder;

import android.view.View;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.staticdata.SNSTag;

public class ViewHolderFactory {
    public static int getLayoutResId(int type) {
        int res = 0;
        int platform = Post.calPlatformType(type);
        int extension = Post.calExtensionType(type);
        int content = Post.calContentType(type);

        if (platform == SNSTag.Facebook)
            res = R.layout.card_timeline;
        else if (platform == SNSTag.Twitter) {
            if (extension == SNSTag.None) {
                if (content == SNSTag.Origin)
                    res = R.layout.card_timeline_twitter_to;
                else if (content == SNSTag.Image)
                    res = R.layout.card_timeline_twitter_tii;
            }
            else {
                if (content == SNSTag.Origin)
                    res = R.layout.card_timeline_twitter_to_rt;
                else if (content == SNSTag.Image)
                    res = R.layout.card_timeline_twitter_tii_rt;
            }
        } else
            res = R.layout.card_timeline;

        return res;
    }

    public static BaseViewHolder getViewHolder(View view, int type) {
        BaseViewHolder res = new FOViewHolder(view);

        int platform = Post.calPlatformType(type);
        int extension = Post.calExtensionType(type);
        int content = Post.calContentType(type);

        if (platform == SNSTag.Facebook)
            res = new FOViewHolder(view);
        else if (platform == SNSTag.Twitter) {
            if (extension == SNSTag.None) {
                if (content == SNSTag.Origin)
                    res = new TOViewHolder(view);
                else if (content == SNSTag.Image)
                    res = new TIIViewHolder(view);
            }
            else {
                if ( content == SNSTag.Origin )
                    res = new RTOViewHodler(view);
                else if ( content == SNSTag.Image)
                    res = new RTIIViewHoler(view);
            }
        }
        else
            res = new TOViewHolder(view);

        return res;
    }
}
