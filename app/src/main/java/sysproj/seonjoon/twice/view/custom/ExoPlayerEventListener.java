package sysproj.seonjoon.twice.view.custom;

import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import sysproj.seonjoon.twice.R;

public class ExoPlayerEventListener implements ExoPlayer.EventListener {

    private PlayerView videoView;

    public ExoPlayerEventListener(PlayerView playerView) {

        this.videoView = playerView;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        PlayerControlView controlView = videoView.findViewById(R.id.exo_controller);

        switch (playbackState) {
            case SimpleExoPlayer.STATE_BUFFERING:
                controlView.findViewById(R.id.exo_play).setVisibility(View.GONE);
                controlView.findViewById(R.id.exo_pause).setVisibility(View.GONE);
                break;
            case SimpleExoPlayer.STATE_READY:
                controlView.findViewById(R.id.exo_play).setVisibility(View.VISIBLE);
                break;
            case SimpleExoPlayer.STATE_ENDED:
                controlView.findViewById(R.id.exo_play).setVisibility(View.VISIBLE);
                controlView.findViewById(R.id.exo_pause).setVisibility(View.GONE);
        }
    }
}
