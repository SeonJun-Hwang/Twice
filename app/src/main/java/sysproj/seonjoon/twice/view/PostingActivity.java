package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.view.custom.PostImageAdapter;
import sysproj.seonjoon.twice.view.custom.PostImagePager;
import sysproj.seonjoon.twice.view.custom.TwiceGallery.GalleryActivity;

public class PostingActivity extends AppCompatActivity {

    private static final int IMAGE_SELECT = 1000;
    private static final String TAG = "PostingActivity";

    private ImageView profileImage;
    private ImageButton loadImage;
    private RadioButton postReserveRadio;
    private Context mContext;
    private ArrayList<String> selectedImage;
    private ViewPager postImagePager;
    private PostImageAdapter postImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mContext = this;
        selectedImage = new ArrayList<>();

        initLayout();
        initListener();
    }

    private void initLayout() {
        profileImage = (ImageView) findViewById(R.id.create_post_profile_image);
        loadImage = (ImageButton) findViewById(R.id.post_include_image);
        postReserveRadio = (RadioButton) findViewById(R.id.post_reserve_radio);
        postImagePager = (ViewPager) findViewById(R.id.create_post_image_pager);
        postImageAdapter = new PostImageAdapter(mContext, selectedImage);

        postImagePager.setAdapter(postImageAdapter);
        postImagePager.setPageMargin(20);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("profile_url");

        Glide.with(mContext).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(profileUrl).into(profileImage);
    }

    private void initListener() {
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog imageCountDialog = new AlertDialog.Builder(mContext)
                        .setMessage("Twitter는 최대 4개까지의 이미지만 적용됩니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                Intent gallery = new Intent(PostingActivity.this, GalleryActivity.class);
                                startActivityForResult(gallery, IMAGE_SELECT);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create();

                imageCountDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, requestCode + " get Result : " + resultCode);

        if (requestCode == IMAGE_SELECT) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> selectedImageUri = data.getStringArrayListExtra("result");

                if (selectedImageUri != null && !selectedImageUri.isEmpty()) {
                    selectedImage.clear();
                    selectedImage.addAll(selectedImageUri);
                    //postImageAdapter.setUriList(selectedImage);
                    postImageAdapter.notifyDataSetChanged();
                }
                else
                    Log.e(TAG, "Selected Image is Null or empty");
            }
        }

    }
}
