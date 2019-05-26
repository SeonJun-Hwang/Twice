package sysproj.seonjoon.twice.view.custom;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.PostMedia;

public class CarouselAdapter extends PagerAdapter {

    private static final String TAG = "CarImageAdapter";

    private ArrayList<PostMedia> mediaList;
    private Context context;

    private CarouselAdapter() {
    }

    public CarouselAdapter(Context context, ArrayList<PostMedia> uris) {
        this.context = context;
        this.mediaList = uris;
    }

    @Override
    public int getCount() {
        return mediaList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (View) o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.carousel_view, container, false);

            PostMedia media = mediaList.get(position);

            ImageView imageView = (ImageView) view.findViewById(R.id.carousel_view_image);
            PlayerView videoView = (PlayerView) view.findViewById(R.id.carousel_view_video);

            if (media.getMediaTag() == PostMedia.PHOTO) {
                videoView.setVisibility(View.INVISIBLE);

                Glide.with(context).load(media.getMediaURL()).centerCrop().into(imageView);
            } else if (media.getMediaTag() == PostMedia.VIDEO) {
                imageView.setVisibility(View.GONE);

                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);

                videoView.setPlayer(player);

                String userAgent = Util.getUserAgent(context, "Twice");

                MediaSource source = new ProgressiveMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                        .createMediaSource(Uri.parse(media.getMediaURL()));

                player.prepare(source, true, false);
                player.setPlayWhenReady(true);

            }

        }

        container.addView(view);

        return view;
    }

}
