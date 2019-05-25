package sysproj.seonjoon.twice.viewholder;

import android.util.Log;
import android.view.View;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.staticdata.SNSTag;

public class ViewHolderFactory {

    private final static String TAG = "ViewHolderFactory";

    public static int getLayoutResId(int type) {
        int res = 0;
        int platform = Post.calPlatformType(type);
        int extension = Post.calExtensionType(type);
        int content = Post.calContentType(type);

        if (platform == SNSTag.Facebook) {
            if (content == SNSTag.Origin)
                res = R.layout.card_timeline_facebook_fo;
            else if (content == SNSTag.Image)
                res = R.layout.card_timeline_facebook_fim;
            else if (content == SNSTag.Video)
                res = R.layout.card_timeline_facebook_fiv;
            else if (content == SNSTag.Link)
                res = R.layout.card_timeline_facebook_fl;
        } else if (platform == SNSTag.Twitter) {
            if (extension == SNSTag.None) {
                if (content == SNSTag.Origin)
                    res = R.layout.card_timeline_twitter_to;
                else if (content == SNSTag.Image)
                    res = R.layout.card_timeline_twitter_tii;
            } else {
                if (content == SNSTag.Origin)
                    res = R.layout.card_timeline_twitter_to_rt;
                else if (content == SNSTag.Image)
                    res = R.layout.card_timeline_twitter_tii_rt;
            }
        } else {
            if (content == SNSTag.Video)
                res = R.layout.card_timeline_instagram_video;
            else if (content == SNSTag.Image)
                res = R.layout.card_timeline_instagram_image;
            else if (content == SNSTag.Carousel)
                res = R.layout.card_timeline_instagram_carousel;
        }

        return res;
    }

    public static BaseViewHolder getViewHolder(View view, int type) {
        BaseViewHolder res = new FOViewHolder(view);

        int platform = Post.calPlatformType(type);
        int extension = Post.calExtensionType(type);
        int content = Post.calContentType(type);

        if (platform == SNSTag.Facebook) {
            if (content == SNSTag.Origin)
                res = new FOViewHolder(view);
            else if (content == SNSTag.Image)
                res = new FIMViewHolder(view);
            else if (content == SNSTag.Video)
                res = new FIVViewHolder(view);
            else if (content == SNSTag.Link)
                res = new FLViewHolder(view);
        } else if (platform == SNSTag.Twitter) {
            if (extension == SNSTag.None) {
                if (content == SNSTag.Origin)
                    res = new TOViewHolder(view);
                else if (content == SNSTag.Image)
                    res = new TIIViewHolder(view);
            } else {
                if (content == SNSTag.Origin)
                    res = new RTOViewHodler(view);
                else if (content == SNSTag.Image)
                    res = new RTIIViewHolder(view);
            }
        } else {
            if (content == SNSTag.Image)
                res = new IIViewHolder(view);
            else if (content == SNSTag.Video)
                res = new IVViewHolder(view);
            else if (content == SNSTag.Carousel)
                res = new ICViewHolder(view);
        }

        return res;
    }
}
