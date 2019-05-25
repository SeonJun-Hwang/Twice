package sysproj.seonjoon.twice.view.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

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
        return true; //view == (View)o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        if (context != null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.carousel_view, container, false);

            ImageView item = (ImageView) view.findViewById(R.id.carousel_view_image);
            VideoView videoView = (VideoView) view.findViewById(R.id.carousel_view_video);


//            Glide.with(context)
//                    .applyDefaultRequestOptions(RequestOptions.bitmapTransform(new RoundedCorners(16)))
//                    .load(uriList.get(position))
//                    .dontAnimate()
//                    .into(item);
        }

        container.addView(view);

        return view;
    }

}
