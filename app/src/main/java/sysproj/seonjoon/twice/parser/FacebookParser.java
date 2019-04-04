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
    public ArrayList<Post> parseItem(JSONObject object) {
        if (object == null)
            return null;

//        try {
//            Log.e(TAG, object.toString(2));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

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

                resultList.add(new FacebookPost(SNSTag.Facebook, uName, message, null, createdTime, new PostRFS(), null, null));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
