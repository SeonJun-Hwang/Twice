package sysproj.seonjoon.twice.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.staticdata.SNSTag;

public class TwitterParser implements SNSParser {

    private static final String TAG = "TwitterParser";

    @Override
    public List<Post> parseItem(JSONObject object) {

        List<Post> result = new ArrayList<>();

        Log.e(TAG, "res : " + result.size());

        try {
            JSONArray requestData = object.getJSONArray("result");

            for (int i = 0; i < requestData.length(); i++) {
                JSONObject item = requestData.getJSONObject(i);
                JSONObject userObject = item.getJSONObject("user");
                JSONObject entities = item.getJSONObject("entities");

                String text = item.getString("text");
                String userName = userObject.getString("name");
                String profileUrl = userObject.getString("profile_image_url");
                String createDate = item.getString("created_at");
                int retweetCount= item.getInt("retweet_count");
                int favoriteCount= item.getInt("favorite_count");

                ArrayList<String> imageList = parseMediaInfo(entities);

                result.add(new Post(SNSTag.Twitter, userName, text, profileUrl, createDate,new PostRFS(0,favoriteCount,retweetCount), imageList));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    private ArrayList parseMediaInfo(JSONObject entities) {
        ArrayList imageList = null;

        try {
            JSONArray medias = entities.getJSONArray("media");

            imageList = new ArrayList<String>();

            for (int i = 0; i < medias.length(); i++) {
                JSONObject mediaObject = medias.getJSONObject(i);
                imageList.add(mediaObject.getString("media_url"));
            }

        } catch (Exception e) { }

        return imageList;
    }
}
