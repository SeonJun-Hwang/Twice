package sysproj.seonjoon.twice.view.custom;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import sysproj.seonjoon.twice.R;

public class PostImageAdapter extends PagerAdapter {

    private static final String TAG = "PostImageAdapter";

    private ArrayList<String> uriList;
    private Context context;

    private PostImageAdapter() {
    }

    public PostImageAdapter(Context context, ArrayList<String> uris) {
        this.context = context;
        this.uriList = uris;
    }

    @Override
    public int getCount() {
        return uriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (View)o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public void setUriList(ArrayList<String> arr) {
        uriList = arr;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        if (context != null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.post_create_image_recycler, container, false);

            ImageView item = (ImageView) view.findViewById(R.id.post_create_image);

            Glide.with(context)
                    .applyDefaultRequestOptions(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, 50, 10)))
                    .load(uriList.get(position))
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(context.getResources().getDrawable(R.drawable.search_active))
                    .into(item);
        }

        container.addView(view);

        return view;
    }
}
