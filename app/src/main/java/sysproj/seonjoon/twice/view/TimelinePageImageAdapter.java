package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;

public class TimelinePageImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imageList;

    public TimelinePageImageAdapter() {
    }

    public TimelinePageImageAdapter(Context context, ArrayList<String> imageList){
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

        if (context != null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.timeline_image_viewer, container, false);

            ImageView item = (ImageView) view.findViewById(R.id.timeline_image_viewer_image);
            Glide.with(context).load(imageList.get(position)).into(item);
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
