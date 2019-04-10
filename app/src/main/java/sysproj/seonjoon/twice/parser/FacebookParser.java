package sysproj.seonjoon.twice.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.GraphRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookParser implements SNSParser {

    private final static String TAG = "FacebookParser";
    private ArrayList<Bitmap> imageList;

    public FacebookParser() {
        imageList = new ArrayList<>();
    }

    @Override
    public ArrayList<Post> parseTimeline(JSONObject object) {
        if (object == null)
            return null;

        ArrayList<Post> resultList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String uid = jsonObject.getString("id").split("_")[0];
                String message = null;
                String createdTime = jsonObject.getString("created_time");
                String uName = uid;

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (uName == null || uName.isEmpty())
                    uName = "Unknown";

                Post post = new FacebookPost.Builder(uName, message, createdTime, new PostRFS()).build();

                resultList.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public ArrayList<Post> parseSearch(JSONObject object) {
        if (object == null)
            return null;

        ArrayList<Post> resultList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String uid = jsonObject.getString("id").split("_")[0];
                String message = null;
                String createdTime = jsonObject.getString("created_time");
                String uName = uid;

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (uName == null || uName.isEmpty())
                    uName = "Unknown";

                Post post = new FacebookPost.Builder(uName, message, createdTime, new PostRFS()).build();

                resultList.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
