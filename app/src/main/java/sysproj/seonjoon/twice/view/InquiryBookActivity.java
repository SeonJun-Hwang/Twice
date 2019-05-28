package sysproj.seonjoon.twice.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.xml.transform.Templates;

import sysproj.seonjoon.twice.BuildConfig;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.BookPostVO;
import sysproj.seonjoon.twice.manager.DBManager;
import sysproj.seonjoon.twice.staticdata.UserSession;
import sysproj.seonjoon.twice.view.custom.BookListAdapter;

public class InquiryBookActivity extends AppCompatActivity {

    private final String TAG = "InquiryBookActivity";

    private Context mContext;
    private ListView listView;
    private BookListAdapter adapter;
    private LoadInquiryListAsync async;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_post);

        mContext = this;

        listView = (ListView) findViewById(R.id.book_list);

        if (async == null) {
            async = new LoadInquiryListAsync();
            async.execute();
        }

    }

    private class LoadInquiryListAsync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private ArrayList<BookPostVO> resResult = new ArrayList<>();

        public LoadInquiryListAsync() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("조회 중입니다.");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject object = DBManager.getInstance().LoadBookInquiryList();

            try {
                JSONArray dataArray = object.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject item = dataArray.getJSONObject(i);

                    long id = item.getLong("post_id");
                    String message = item.getString("message");
                    long time = item.getLong("bookingTime");
                    boolean cf = item.getBoolean("facebook");
                    boolean ci = item.getBoolean("instagram");
                    boolean ct = item.getBoolean("twitter");

                    BookPostVO bookPostVO = new BookPostVO(id, cf, ci, ct, time, message);

                    resResult.add(bookPostVO);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
            progressDialog = null;

            if (resResult.isEmpty())
                Toast.makeText(mContext, "예약된 게시글이 없습니다.", Toast.LENGTH_SHORT).show();

            adapter = new BookListAdapter(resResult);
            listView.setAdapter(adapter);
        }
    }
}
