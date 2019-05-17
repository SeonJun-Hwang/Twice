package sysproj.seonjoon.twice.view.custom.TwiceGallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.R;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerGallery;
    private GalleryAdapter galleryAdapter;
    private static final String TAG = "GalleryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twice_gallery);

        initLayout();
        initRecyclerGallery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_done) {
            selectDone();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        recyclerGallery = (RecyclerView) findViewById(R.id.recyclerGallery);
    }

    private List<PhotoVO> initGalleryPathList() {

        GalleryManager mGalleryManager = new GalleryManager(getApplicationContext());
        return mGalleryManager.getAllPhotoPathList();
    }

    private void selectDone() {
        ArrayList<String> selectedPhotoList = galleryAdapter.getSelectedPhotoList();

        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("result", selectedPhotoList);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        AlertDialog exitDialog = new AlertDialog.Builder(this)
                .setMessage("이미지 선택을 취소 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setCancelable(true)
                .create();

        exitDialog.show();
    }

    private void initRecyclerGallery() {
        galleryAdapter = new GalleryAdapter(GalleryActivity.this, initGalleryPathList(), R.layout.item_photo);
        galleryAdapter.setOnItemClickListener(mOnItemClickListener);
        recyclerGallery.setAdapter(galleryAdapter);
        recyclerGallery.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerGallery.setItemAnimator(new DefaultItemAnimator());
        recyclerGallery.addItemDecoration(new GridDividerDecoration(getResources(), R.drawable.divider_recycler_gallery));
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void OnItemClick(GalleryAdapter.PhotoViewHolder photoViewHolder, int position) {
            PhotoVO photoVO = galleryAdapter.getmPhotoList().get(position);

            if (photoVO.isSelected()) {
                photoVO.setSelected(false);
            } else {
                photoVO.setSelected(true);
            }

            galleryAdapter.getmPhotoList().set(position, photoVO);
            galleryAdapter.notifyDataSetChanged();
        }
    };

}
