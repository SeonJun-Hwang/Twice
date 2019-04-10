package sysproj.seonjoon.twice.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostExtendInfo;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.entity.TwitterPost;

public class TwitterParser implements SNSParser {

    private static final String TAG = "TwitterParser";

    @Override
    public ArrayList<Post> parseTimeline(JSONObject object) {

        ArrayList<Post> result = new ArrayList<>();
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
                int retweetCount = item.getInt("retweet_count");
                int favoriteCount = item.getInt("favorite_count");

                ArrayList<PostMedia> imageList = parseMediaInfo(entities);
                ArrayList<PostExtendInfo> extendList = parseExtendInfo(entities);

                Post post = new TwitterPost.Builder(userName, text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                        .profileImage(profileUrl)
                        .extendInfo(extendList)
                        .imageList(imageList).build();

                result.add(post);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    @Override
    public ArrayList<Post> parseSearch(JSONObject object) {

        ArrayList<Post> result = new ArrayList<>();
        try {
            JSONArray requestData = object.getJSONArray("statuses");

            //Log.e(TAG, requestData.toString(2));

            for (int i = 0; i < requestData.length(); i++) {
                JSONObject item = requestData.getJSONObject(i);
                JSONObject userObject = item.getJSONObject("user");
                JSONObject entities = item.getJSONObject("entities");

                String text = item.getString("text");
                String userName = userObject.getString("name");
                String profileUrl = userObject.getString("profile_image_url");
                String createDate = item.getString("created_at");
                int retweetCount = item.getInt("retweet_count");
                int favoriteCount = item.getInt("favorite_count");

                ArrayList<PostMedia> imageList = parseMediaInfo(entities);
                ArrayList<PostExtendInfo> extendList = parseExtendInfo(entities);

                Post post = new TwitterPost.Builder(userName, text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                        .profileImage(profileUrl)
                        .extendInfo(extendList)
                        .imageList(imageList)
                        .build();

                result.add(post);
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

            imageList = new ArrayList<PostMedia>();

            for (int i = 0; i < medias.length(); i++) {
                JSONObject mediaObject = medias.getJSONObject(i);
                String tag = mediaObject.getString("type");
                String mediaURL = mediaObject.getString("media_url");
                String keyword = mediaObject.getString("url");

                imageList.add(new PostMedia(tag.contentEquals("video") ? PostMedia.VIDEO : PostMedia.PHOTO, keyword, mediaURL));
            }

        } catch (Exception e) {
        }

        return imageList;
    }

    private ArrayList parseExtendInfo(JSONObject entities) {
        ArrayList extendInfo = null;

        try {
            JSONArray urls = entities.getJSONArray("urls");
            JSONArray mention = entities.getJSONArray("user_mentions");
            JSONArray hashtag = entities.getJSONArray("hashtags");

            extendInfo = new ArrayList<PostExtendInfo>();

            for (int i = 0; i < hashtag.length(); i++) {
                JSONObject object = hashtag.getJSONObject(i);
                JSONArray indices = object.getJSONArray("indices");

                String text = object.getString("text");
                extendInfo.add(new PostExtendInfo(PostExtendInfo.HASH_TAG, indices.getInt(0), indices.getInt(1), text, null));
            }

            for (int i = 0; i < urls.length(); i++) {
                JSONObject object = urls.getJSONObject(i);
                JSONArray indices = object.getJSONArray("indices");

                String keyword = object.getString("url");
                String linkURL = object.getString("expanded_url");
                extendInfo.add(new PostExtendInfo(PostExtendInfo.URLS, indices.getInt(0), indices.getInt(1), keyword, linkURL));
            }

            for (int i = 0; i < mention.length(); i++) {
                JSONObject object = mention.getJSONObject(i);
                JSONArray indices = object.getJSONArray("indices");

                String screenName = object.getString("screen_name");
                String keyword = '@' + screenName;
                String linkURL = "https://twitter.com/" + screenName;

                extendInfo.add(new PostExtendInfo(PostExtendInfo.MENTION, indices.getInt(0), indices.getInt(1), keyword, linkURL));
            }

        } catch (Exception e) {
        }

        return extendInfo;
    }
}
