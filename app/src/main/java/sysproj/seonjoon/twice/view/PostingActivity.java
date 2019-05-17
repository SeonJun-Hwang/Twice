package sysproj.seonjoon.twice.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sysproj.seonjoon.twice.BuildConfig;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.staticdata.UserSession;
import sysproj.seonjoon.twice.view.custom.PostImageAdapter;
import sysproj.seonjoon.twice.view.custom.PostImagePager;
import sysproj.seonjoon.twice.view.custom.TwiceGallery.GalleryActivity;

public class PostingActivity extends AppCompatActivity {

    private static final int IMAGE_SELECT = 1000;
    private static final String TAG = "PostingActivity";

    private EditText postMessage;
    private ImageView profileImage;
    private ImageButton loadImage;
    private RadioButton postReserveRadio;
    private Context mContext;
    private ArrayList<String> selectedImage;
    private ViewPager postImagePager;
    private PostImageAdapter postImageAdapter;
    private SendQueryAsync async;

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
        postMessage = (EditText) findViewById(R.id.create_post_edit_text);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.posting_done) {
            if (async == null) {
                async = new SendQueryAsync();
                async.execute();
            }
        }

        return super.onOptionsItemSelected(item);
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
                } else
                    Log.e(TAG, "Selected Image is Null or empty");
            }
        }

    }

    private class SendQueryAsync extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("작성 중입니다.");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            boolean res = true;
            String urls = BuildConfig.ServerIP + "facebook_post";
            try {
                URL url = new URL(urls);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(3000);
                connection.setDoOutput(true);

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(makeJSON());
                osw.flush();
                osw.close();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    res = false;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);

            dialog.dismiss();
            dialog = null;

            if (res) {
                Toast.makeText(mContext, "게시 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(mContext, "게시에 실패하였습니다.\n 잠시후에 다시 시도해보시기 바랍니다.", Toast.LENGTH_SHORT).show();
            }

            async = null;
        }

        private String makeJSON() {
            JSONObject sendObject = new JSONObject();
            JSONObject facebookObject = new JSONObject();
            JSONArray facebookData = new JSONArray();

            try {
                facebookObject.put("token", UserSession.FacebookToken.getToken());

                JSONObject item = new JSONObject();
                item.put("message", postMessage.getText().toString());

                JSONArray imageArray = new JSONArray();

                for (int i = 0; i < selectedImage.size(); i++) {
                    String filePath = selectedImage.get(i);
                    String endcoded = Base64.encodeToString(readFile(filePath), Base64.NO_WRAP | Base64.URL_SAFE);
                    imageArray.put(endcoded);
                }

                item.put("images", imageArray);
                facebookData.put(item);
                facebookObject.put("data", facebookData);
                sendObject.put("facebook", facebookObject);

                Log.e(TAG, sendObject.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sendObject.toString();
        }

        private byte[] readFile(String filePath) {

            File file = new File(filePath);
            byte[] ret = null;
            try (FileInputStream fis = new FileInputStream(file)) {

                ret = new byte[(int) fis.getChannel().size()];
                fis.read(ret);

            } catch (IOException e) {
                Log.e(TAG, "File Not Found");
            }

            return ret;
        }
    }
}
