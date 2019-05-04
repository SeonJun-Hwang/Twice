package sysproj.seonjoon.twice.parser;

import android.os.Build;
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
import sysproj.seonjoon.twice.entity.UserProfile;
import sysproj.seonjoon.twice.staticdata.LastUpdate;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class TwitterParser extends SNSParser {

    private static final String TAG = "TwitterParser";

    @Override
    public ArrayList<Post> parseTimeline(JSONObject object) {

        ArrayList<Post> result = new ArrayList<>();
        try {
            JSONArray requestData = object.getJSONArray("result");

            for (int i = 0; i < requestData.length(); i++) {
                JSONObject item = requestData.getJSONObject(i);
                String text = parseContextText(item);
                UserProfile userProfile = parseUserProfile(item);
                long id = parseID(item);
                UserProfile retweetUserProfile = null;

                //Log.e(TAG, userProfile.getName() + " - " + userProfile.getProfileImage());

                if (isRetweetedTweet(text)) {
                    JSONObject retweetObject = parseRetweetStatus(item);

                    retweetUserProfile = parseUserProfile(retweetObject);
                    Log.e(TAG, (i+1) + " : " +  retweetUserProfile.getName());
                    item = retweetObject;
                }

                text = parseContextText(item);
                String createDate = parseCreatedDate(item);
                int retweetCount = parseRetweetCount(item);
                int favoriteCount = parseFavoriteCount(item);
                JSONObject entities = parseEntities(item);

                ArrayList<PostMedia> imageList = parseMediaInfo(item);
                ArrayList<PostExtendInfo> extendList = parseExtendInfo(entities);

                int type = (SNSTag.Twitter * SNSTag.Platform)
                        + (retweetUserProfile != null ? SNSTag.Extension : SNSTag.None)
                        + (imageList == null ? SNSTag.Origin : SNSTag.Image);

                Post post;

                if (retweetUserProfile == null) {
                    post = new TwitterPost.Builder(id, type, userProfile , text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                }
                else
                {
                    post = new TwitterPost.Builder(id, type, retweetUserProfile , text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .retweetUser(userProfile)
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                }

                result.add(post);

                if (i == UserSession.TwitterPerOnce - 1){
                    LastUpdate.setMaxIds(SNSTag.Twitter, id);
                }
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
                String text = parseContextText(item);
                UserProfile userProfile = parseUserProfile(item);
                long id = parseID(item);
                UserProfile retweetUserProfile = null;

                if (isRetweetedTweet(text)) {
                    JSONObject retweetObject = parseRetweetStatus(item);
                    retweetUserProfile = parseUserProfile(retweetObject);
                    item = retweetObject;
                }

                String createDate = parseCreatedDate(item);
                int retweetCount = parseRetweetCount(item);
                int favoriteCount = parseFavoriteCount(item);
                JSONObject entities = parseEntities(item);

                ArrayList<PostMedia> imageList = parseMediaInfo(item);
                ArrayList<PostExtendInfo> extendList = parseExtendInfo(entities);

                int type = (SNSTag.Twitter * SNSTag.Platform)
                        + (imageList == null ? SNSTag.Origin : SNSTag.Image);

                Post post = null;

                if (retweetUserProfile == null) {
                    post = new TwitterPost.Builder(id, type, userProfile , text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                }
                else
                {
                    post = new TwitterPost.Builder(id, type, retweetUserProfile , text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .retweetUser(userProfile)
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                }

                result.add(post);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    private ArrayList parseMediaInfo(JSONObject entities) {
        ArrayList<PostMedia> imageList = null;

        try {
            JSONArray medias = entities.getJSONObject("extended_entities").getJSONArray("media");

            imageList = new ArrayList<PostMedia>();

            for (int i = 0; i < medias.length(); i++) {
                JSONObject mediaObject = medias.getJSONObject(i);
                String tag = mediaObject.getString("type");
                String mediaURL = parseMediaUrl(mediaObject);
                String keyword = mediaObject.getString("url");

                imageList.add(new PostMedia(tag.contentEquals("video") ? PostMedia.VIDEO : PostMedia.PHOTO, keyword, mediaURL));
            }

        } catch (Exception e) {
            Log.e(TAG, "No Media Elements");
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

    private JSONObject parseRetweetStatus(JSONObject object) throws JSONException{
        return object.getJSONObject("retweeted_status");
    }

    private JSONObject parseEntities(JSONObject object) throws JSONException {
        return object.getJSONObject("entities");
    }

    private String parseContextText(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("text");
    }

    private String parseMediaUrl(JSONObject mediaObject) throws JSONException{
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            return mediaObject.getString("media_url");
        return mediaObject.getString("media_url_https");
    }

    @Override
    protected UserProfile parseUserProfile(JSONObject jsonObject) throws JSONException {
        JSONObject userObject = jsonObject.getJSONObject("user");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            return new UserProfile.Builder(userObject.getString("name")).profileImage(userObject.getString("profile_image_url")).build();
        return new UserProfile.Builder(userObject.getString("name")).profileImage(userObject.getString("profile_image_url_https")).build();
    }

    @Override
    protected String parseCreatedDate(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("created_at");
    }

    @Override
    protected long parseID(JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong("id");
    }

    private int parseRetweetCount(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("retweet_count");
    }

    private int parseFavoriteCount(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("favorite_count");
    }

    private boolean isRetweetedTweet(String text){
        return text.substring(0, 3).contains("RT");
    }

}
