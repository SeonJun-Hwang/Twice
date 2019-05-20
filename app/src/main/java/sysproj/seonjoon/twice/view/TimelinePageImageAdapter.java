package sysproj.seonjoon.twice.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.PostMedia;

public class TimelinePageImageAdapter extends PagerAdapter {

    private static final String TAG = "TimelinePageImageAdapter";
    private Context context;
    private ArrayList<PostMedia> imageList;

    public TimelinePageImageAdapter() {
    }

    public TimelinePageImageAdapter(Context context, ArrayList<PostMedia> imageList){
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        //Log.e(TAG, "Context" + context.toString());

        if (context != null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.timeline_image_viewer, container, false);

            ImageView item = (ImageView) view.findViewById(R.id.timeline_image_viewer_image);
            Glide.with(context).load(imageList.get(position).getMediaURL()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(item);
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View)object;
    }
}
