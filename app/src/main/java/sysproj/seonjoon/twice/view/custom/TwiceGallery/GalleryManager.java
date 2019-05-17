package sysproj.seonjoon.twice.view.custom.TwiceGallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class GalleryManager {

    private Context mContext;

    GalleryManager(Context context) {
        mContext = context;
    }
    List<PhotoVO> getAllPhotoPathList() {

        ArrayList<PhotoVO> photoList = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);

        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {

            PhotoVO photoVO = new PhotoVO(cursor.getString(columnIndexData),false);
            photoList.add(photoVO);
        }

        cursor.close();

        return photoList;
    }

}
