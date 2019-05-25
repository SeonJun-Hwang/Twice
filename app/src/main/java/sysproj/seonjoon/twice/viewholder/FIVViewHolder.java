package sysproj.seonjoon.twice.viewholder;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;

public class FIVViewHolder extends FacebookViewHolder {

    private final static String TAG = "FIVViewHolder";

    private PlayerView videoPlayer;

    public FIVViewHolder(View itemView) {
        super(itemView);

        videoPlayer = (PlayerView) itemView.findViewById(R.id.card_video_player);
    }

    @Override
    protected void setImageContent(Post post) {
        ArrayList<PostMedia> imageList = post.getImageList();

        if (imageList != null && imageList.size() > 0) {

            Log.e(TAG, "SIZE - " + imageList.size());
            Log.e(TAG, "Thumbnail " + imageList.get(0).getMediaURL());
            Log.e(TAG, "Video " + imageList.get(1).getMediaURL());

            SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);

            videoPlayer.setPlayer(player);

            MediaSource source = buildMediaSource(Uri.parse(imageList.get(1).getMediaURL()));

            player.prepare(source,true, false);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
