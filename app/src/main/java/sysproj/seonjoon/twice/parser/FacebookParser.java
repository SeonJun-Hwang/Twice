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

import sysproj.seonjoon.twice.entity.FacebookPageVO;
import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.entity.UserProfile;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookParser extends SNSParser {

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

                String message = null;
                String createdTime = parseCreatedDate(jsonObject);
                UserProfile userProfile = parseUserProfile(jsonObject);
                long id = parseIDPost(jsonObject);

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (userProfile.getName() == null || userProfile.getName().isEmpty())
                    userProfile = new UserProfile.Builder("Unknown").build();

                Post post = new FacebookPost.Builder(id, SNSTag.Facebook * SNSTag.Platform + SNSTag.Origin, userProfile, message, createdTime, new PostRFS()).build();

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

                String message = null;
                String createdTime = parseCreatedDate(jsonObject);
                UserProfile userProfile = parseUserProfile(jsonObject);
                long id = parseIDPost(jsonObject);

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (userProfile.getName() == null || userProfile.getName().isEmpty())
                    userProfile = new UserProfile.Builder("Unknown").build();

                Post post = new FacebookPost.Builder(id, SNSTag.Facebook * SNSTag.Platform + SNSTag.Origin, userProfile, message, createdTime, new PostRFS()).build();

                resultList.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public ArrayList<FacebookPageVO> parsePageList(JSONObject object) {
        if (object == null)
            return null;

        ArrayList<FacebookPageVO> returnList = new ArrayList<>();

        try {
            Log.e(TAG, object.toString(2));

            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject data = dataArray.getJSONObject(i);

                String token = parseToken(data);
                String name = parseName(data);
                long id = parseID(data);



                returnList.add(new FacebookPageVO(token, name, id));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Missing Data - " + e.getMessage());
        }

        return returnList;
    }

    @Override
    protected UserProfile parseUserProfile(JSONObject jsonObject) {
        String name;
        try {
            name = parseName(jsonObject);
        } catch (JSONException e) {
            name = "Unknown";
        }

        return new UserProfile.Builder(name).build();
    }

    @Override
    protected String parseCreatedDate(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("created_time");
    }

    @Override
    protected long parseID(JSONObject jsonObject) throws JSONException {
        return Long.parseLong(jsonObject.getString("id"));
    }

    protected long parseIDPost(JSONObject jsonObject) throws JSONException {
        return Long.parseLong(jsonObject.getString("id").split("_")[1]);
    }

    protected String parseName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("name");
    }

    protected String parseMedia(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("full_picture");
    }

    protected String parseToken(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("access_token");
    }

}
