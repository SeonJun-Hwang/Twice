package sysproj.seonjoon.twice.view.custom.TwiceGallery;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    private Activity mActivity;

    private int itemLayout;
    private List<PhotoVO> mPhotoList;

    private OnItemClickListener onItemClickListener;

    List<PhotoVO> getmPhotoList() {
        return mPhotoList;
    }

    ArrayList<String> getSelectedPhotoList() {

        ArrayList<String> mSelectPhotoList = new ArrayList<>();

        for (int i = 0; i < mPhotoList.size(); i++) {

            PhotoVO photoVO = mPhotoList.get(i);
            if (photoVO.isSelected()) {
                mSelectPhotoList.add(photoVO.getImgPath());
            }
        }

        return mSelectPhotoList;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    GalleryAdapter(Activity activity, List<PhotoVO> photoList, int itemLayout) {
        mActivity = activity;

        this.mPhotoList = photoList;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoViewHolder viewHolder, final int position) {

        final PhotoVO photoVO = mPhotoList.get(position);

        Glide.with(mActivity)
                .load(photoVO.getImgPath())
                .dontAnimate()
                .centerCrop()
                .into(viewHolder.imgPhoto);

        //선택
        if (photoVO.isSelected()) {
            viewHolder.layoutSelect.setVisibility(View.VISIBLE);
        } else {
            viewHolder.layoutSelect.setVisibility(View.INVISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(viewHolder, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        RelativeLayout layoutSelect;
        PhotoViewHolder(View itemView) {
            super(itemView);

            imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);
            layoutSelect = (RelativeLayout) itemView.findViewById(R.id.layoutSelect);
        }

    }
}

