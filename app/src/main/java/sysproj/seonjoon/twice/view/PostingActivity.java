package sysproj.seonjoon.twice.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sysproj.seonjoon.twice.BuildConfig;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.FacebookPageVO;
import sysproj.seonjoon.twice.entity.TwiceTime;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.staticdata.UserSession;
import sysproj.seonjoon.twice.view.custom.PostImageAdapter;
import sysproj.seonjoon.twice.view.custom.TwiceGallery.GalleryActivity;

public class PostingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int IMAGE_SELECT = 1000;
    private static final String TAG = "PostingActivity";
    private static final String USER_AGENT = "Mozilla/5.0";

    private EditText postMessage;
    private ImageButton loadImage;
    private CheckBox postReserveCheckBox;
    private CheckBox postFacebookCheck;
    private CheckBox postTwitterCheck;
    private CheckBox postInstagramCheck;
    private TextView postReservieTime;
    private Context mContext;
    private ArrayList<String> selectedImage;
    private ViewPager postImagePager;
    private PostImageAdapter postImageAdapter;
    private SendQueryAsync async;

    private DatePickerDialog pickerDialog;

    private TwiceTime bookTime;
    private TwiceTime.Builder bookBuilder;

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
        loadImage = (ImageButton) findViewById(R.id.post_include_image);
        postReserveCheckBox = (CheckBox) findViewById(R.id.post_reserve_radio);
        postImagePager = (ViewPager) findViewById(R.id.create_post_image_pager);
        postImageAdapter = new PostImageAdapter(mContext, selectedImage);

        postFacebookCheck = (CheckBox) findViewById(R.id.create_facebook_check);
        postTwitterCheck = (CheckBox) findViewById(R.id.create_twitter_check);
        postInstagramCheck = (CheckBox) findViewById(R.id.create_instagram_check);

        postReservieTime = (TextView) findViewById(R.id.post_reserve_time);

        postImagePager.setAdapter(postImageAdapter);
        postImagePager.setPageMargin(20);
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

        postReserveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postReserveCheckBox.isChecked()) {
                    if (pickerDialog == null) {
                        Calendar now = Calendar.getInstance();
                        Calendar maxDay = Calendar.getInstance();
                        maxDay.add(Calendar.DATE, 7);

                        pickerDialog = DatePickerDialog.newInstance(PostingActivity.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH));

                        pickerDialog.setCancelable(false);
                        pickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                pickerDialog = null;
                            }
                        });

                        pickerDialog.setMinDate(now);
                        pickerDialog.setMaxDate(maxDay);

                        pickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");

                    }
                } else {
                    new AlertDialog.Builder(mContext)
                            .setMessage("예약이 취소 됩니다.\n취소하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    postReserveCheckBox.setChecked(false);
                                    postReservieTime.setText("");
                                    bookTime = null;
                                    bookBuilder = null;
                                    postReserveCheckBox.setChecked(false);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    postReserveCheckBox.setChecked(true);

                                }
                            })
                            .show();

                }

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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        view.dismiss();

        bookBuilder = new TwiceTime.Builder(year, monthOfYear + 1, dayOfMonth);
        pickerDialog = null;

        Calendar now = Calendar.getInstance();

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(PostingActivity.this,
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);

        timePickerDialog.setCancelable(false);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                postReserveCheckBox.setChecked(false);
            }
        });
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        view.dismiss();

        bookTime = bookBuilder.Hour(hourOfDay).Minute(minute).build();
        postReservieTime.setText(bookTime.toString());
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
            String urls = BuildConfig.ServerIP + "facebook_page";

            Log.e(TAG, urls);

            try {
                URL url = new URL(urls);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(3000);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                String object = makeJSON();

                Log.e(TAG, object);

                osw.write(object);
                osw.flush();
                osw.close();

                Log.e(TAG, "Response : " + connection.getResponseCode());

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
            JSONArray facebookData = new JSONArray();

            try {
                // Time
                sendObject.put("time", postReserveCheckBox.isChecked() ? bookTime.toFormatString() : null);
                sendObject.put("uid", DBManager.getInstance().getUser().getUid());

                // Image
                JSONArray imageArray = new JSONArray();
                for (int i = 0; i < selectedImage.size(); i++) {
                    String filePath = selectedImage.get(i);
                    String endcoded = Base64.encodeToString(readFile(filePath), Base64.NO_WRAP | Base64.URL_SAFE);
                    imageArray.put(endcoded);
                }

                sendObject.put("images", imageArray);
                sendObject.put("message", postMessage.getText().toString());

                // Facebook
                if (postFacebookCheck.isChecked()) {
                    if (UserSession.FacebookPageProfile != null) {
                        for (int pageNum = 0; pageNum < UserSession.FacebookPageProfile.size(); pageNum++) {
                            FacebookPageVO pageVO = UserSession.FacebookPageProfile.get(pageNum);

                            JSONObject item = new JSONObject();

                            item.put("token", pageVO.getAccessToken());
                            item.put("page_id", pageVO.getPageId());

                            facebookData.put(item);
                        }
                    }
                    sendObject.put("facebook", facebookData);

                } else
                    sendObject.put("facebook", null);

                // Twitter
                if (postTwitterCheck.isChecked()) {
                    JSONObject twitterData = new JSONObject();
                    twitterData.put("tvn", UserSession.TwitterToken.getAuthToken().secret);
                    twitterData.put("cgv", UserSession.TwitterToken.getAuthToken().token);
                    sendObject.put("twitter", twitterData);
                } else
                    sendObject.put("twitter", null);

                // Instagram
                if (postInstagramCheck.isChecked()) {
                    JSONObject instagramData = new JSONObject();
                    instagramData.put("mbc", UserSession.InstagramToken);
                    sendObject.put("instagram", instagramData);
                } else
                    sendObject.put("instagram", null);


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
