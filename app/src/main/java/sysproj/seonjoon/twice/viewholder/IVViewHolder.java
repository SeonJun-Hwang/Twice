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
import sysproj.seonjoon.twice.view.custom.ExoPlayerEventListener;

public class IVViewHolder extends InstagramViewHolder {

    private PlayerView videoView;

    public IVViewHolder(View itemView) {
        super(itemView);

        videoView = (PlayerView) itemView.findViewById(R.id.card_content_video);
    }

    @Override
    protected void setImageContent(Post post) {
        PostMedia video = post.getImageList().get(0);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);

        videoView.setPlayer(player);

        player.addListener(new ExoPlayerEventListener(videoView));

        MediaSource source = buildMediaSource(Uri.parse(video.getMediaURL()));

        player.prepare(source, true, false);
        player.setPlayWhenReady(true);

    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
