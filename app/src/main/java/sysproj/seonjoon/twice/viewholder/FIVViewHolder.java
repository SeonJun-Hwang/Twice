package sysproj.seonjoon.twice.viewholder;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;

public class FIVViewHolder extends BaseViewHolder {

    private final static String TAG = "FIVViewHolder";

    private VideoView videoPlayer;

    public FIVViewHolder(View itemView) {
        super(itemView);

        videoPlayer = (VideoView) itemView.findViewById(R.id.card_video_player);
    }

    @Override
    protected void setImageContent(Post post) {
        ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

            Log.e(TAG, "SIZE - " + imageList.size());
            Log.e(TAG, "Thumbnail " + imageList.get(0).getMediaURL());
            Log.e(TAG, "Video " + imageList.get(1).getMediaURL());

            videoPlayer.getVideoInfo().setBgColor(Color.WHITE).setAspectRatio(VideoInfo.AR_ASPECT_FIT_PARENT);//.setVideoPath(imageList.get(1).getMediaURL()).setAc;
            videoPlayer.setVideoPath(imageList.get(1).getMediaURL()).setFingerprint(getAdapterPosition()).getPlayer().start();
        }
    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
