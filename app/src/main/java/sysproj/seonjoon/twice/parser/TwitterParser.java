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
import sysproj.seonjoon.twice.loader.PreferenceLoader;
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
                JSONObject retweetItem = parseRetweetStatus(item);
                ArrayList<PostExtendInfo> extendList = null;

                // Only Item Field
                long id = parseID(item);
                UserProfile userProfile = parseUserProfile(parserUserObject(item));
                ArrayList<PostExtendInfo> urls = parseExtendUrls(parseEntities(item));
                String createDate = parseCreatedDate(item);

                // Different Content about retweet Status
                String text = retweetItem != null ? parseContextText(retweetItem) : parseContextText(item);
                int retweetCount = retweetItem != null ? parseRetweetCount(retweetItem) : parseRetweetCount(item);
                int favoriteCount = retweetItem != null ? parseFavoriteCount(retweetItem) : parseFavoriteCount(item);
                UserProfile retweetUserProfile = retweetItem != null ? parseUserProfile(parserUserObject(retweetItem)) : null;
                ArrayList<PostMedia> imageList = retweetItem != null ? parseMediaInfo(retweetItem) : parseMediaInfo(item);
                ArrayList<PostExtendInfo> hashTags = retweetItem != null ? parseHashTags(parseEntities(retweetItem)) : parseHashTags(parseEntities(item));
                ArrayList<PostExtendInfo> mentions = retweetItem != null ? parseMentions(parseEntities(retweetItem)) : parseMentions(parseEntities(item));
                PostExtendInfo relatedThread = retweetItem != null ? parseRelatedThread(parseEntities(retweetItem)) : null;

                if ((hashTags != null && !hashTags.isEmpty())) {
                    extendList = new ArrayList<>(hashTags);
                }
                if (urls != null && !urls.isEmpty()) {
                    if (extendList == null)
                        extendList = new ArrayList<>(urls);
                    else
                        extendList.addAll(urls);
                }
                if (mentions != null && !mentions.isEmpty()) {
                    if (extendList == null)
                        extendList = new ArrayList<>(mentions);
                    else
                        extendList.addAll(mentions);
                }

                int type = (SNSTag.Twitter * SNSTag.Platform)
                        + (retweetUserProfile != null ? SNSTag.Extension : SNSTag.None)
                        + (imageList == null ? SNSTag.Origin : SNSTag.Image);

                Post post;

                if (retweetUserProfile == null) {
                    post = new TwitterPost.Builder(id, type, userProfile, text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                } else {
                    post = new TwitterPost.Builder(id, type, retweetUserProfile, text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .retweetUser(userProfile)
                            .relatedThread(relatedThread)
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                }

                result.add(post);

                if (i == (requestData.length() - 1)) {
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
                JSONObject retweetItem = parseRetweetStatus(item);
                ArrayList<PostExtendInfo> extendList = null;

                // Only Item Field
                long id = parseID(item);
                UserProfile userProfile = parseUserProfile(parserUserObject(item));
                ArrayList<PostExtendInfo> urls = parseExtendUrls(parseEntities(item));

                // Different Content about retweet Status
                String text = retweetItem != null ? parseContextText(retweetItem) : parseContextText(item);
                String createDate = retweetItem != null ? parseCreatedDate(retweetItem) : parseCreatedDate(item);
                int retweetCount = retweetItem != null ? parseRetweetCount(retweetItem) : parseRetweetCount(item);
                int favoriteCount = retweetItem != null ? parseFavoriteCount(retweetItem) : parseFavoriteCount(item);
                UserProfile retweetUserProfile = retweetItem != null ? parseUserProfile(parserUserObject(retweetItem)) : null;
                ArrayList<PostMedia> imageList = retweetItem != null ? parseMediaInfo(retweetItem) : parseMediaInfo(item);
                ArrayList<PostExtendInfo> hashTags = retweetItem != null ? parseHashTags(parseEntities(retweetItem)) : parseHashTags(parseEntities(item));
                ArrayList<PostExtendInfo> mentions = retweetItem != null ? parseMentions(parseEntities(retweetItem)) : parseMentions(parseEntities(item));
                PostExtendInfo relatedThread = retweetItem != null ? parseRelatedThread(parseEntities(retweetItem)) : null;

                if ((hashTags != null && !hashTags.isEmpty())) {
                    extendList = new ArrayList<>(hashTags);
                }
                if (urls != null && !urls.isEmpty()) {
                    if (extendList == null)
                        extendList = new ArrayList<>(urls);
                    else
                        extendList.addAll(urls);
                }
                if (mentions != null && !mentions.isEmpty()) {
                    if (extendList == null)
                        extendList = new ArrayList<>(mentions);
                    else
                        extendList.addAll(mentions);
                }

                int type = (SNSTag.Twitter * SNSTag.Platform)
                        + (imageList == null ? SNSTag.Origin : SNSTag.Image);

                Post post = null;

                if (retweetUserProfile == null) {
                    post = new TwitterPost.Builder(id, type, userProfile, text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .extendInfo(extendList)
                            .imageList(imageList).build();
                } else {
                    post = new TwitterPost.Builder(id, type, retweetUserProfile, text, createDate, new PostRFS(0, favoriteCount, retweetCount))
                            .retweetUser(userProfile)
                            .relatedThread(relatedThread)
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

    private ArrayList<PostMedia> parseMediaInfo(JSONObject entities) {
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
            //Log.e(TAG, "No Media Elements");
        }

        return imageList;
    }

    private ArrayList<PostExtendInfo> parseHashTags(JSONObject entities) {
        ArrayList<PostExtendInfo> hashtags = null;

        try {
            JSONArray hashtagArray = entities.getJSONArray("hashtags");

            hashtags = new ArrayList<>();

            for (int i = 0; i < hashtagArray.length(); i++) {
                JSONObject object = hashtagArray.getJSONObject(i);
                JSONArray indices = object.getJSONArray("indices");

                String text = object.getString("text");
                hashtags.add(new PostExtendInfo(PostExtendInfo.HASH_TAG, indices.getInt(0), indices.getInt(1), text, null));
            }

        } catch (Exception e) {
            Log.e(TAG, "Parsing Error Hash Tag");
        }

        return hashtags;
    }

    private ArrayList<PostExtendInfo> parseExtendUrls(JSONObject entities) {
        ArrayList<PostExtendInfo> urls = null;

        try {
            JSONArray urlArray = entities.getJSONArray("urls");

            urls = new ArrayList<>();

            for (int i = 0; i < urlArray.length(); i++) {
                JSONObject object = urlArray.getJSONObject(i);
                JSONArray indices = object.getJSONArray("indices");

                String keyword = object.getString("url");
                String linkURL = object.getString("expanded_url");
                urls.add(new PostExtendInfo(PostExtendInfo.URLS, indices.getInt(0), indices.getInt(1), keyword, linkURL));
            }

        } catch (Exception e) {
            Log.e(TAG, "Parsing Error in URL");
        }

        return urls;
    }

    private PostExtendInfo parseRelatedThread(JSONObject entities) {

        PostExtendInfo relThread = null;

        try {
            relThread = parseExtendUrls(entities).get(0);
        } catch (Exception e) {
        }

        return relThread;
    }

    private ArrayList<PostExtendInfo> parseMentions(JSONObject entities) {
        ArrayList<PostExtendInfo> mentions = null;

        try {
            JSONArray mentionArray = entities.getJSONArray("user_mentions");

            mentions = new ArrayList<>();

            for (int i = 0; i < mentionArray.length(); i++) {
                JSONObject object = mentionArray.getJSONObject(i);
                JSONArray indices = object.getJSONArray("indices");

                String screenName = object.getString("screen_name");
                String keyword = '@' + screenName;
                String linkURL = "https://twitter.com/" + screenName;

                mentions.add(new PostExtendInfo(PostExtendInfo.MENTION, indices.getInt(0), indices.getInt(1), keyword, linkURL));
            }

        } catch (Exception e) {
            Log.e(TAG, "Parsing Error in Mention / " + e.getMessage());
        }

        return mentions;
    }

    private JSONObject parseRetweetStatus(JSONObject object) {
        JSONObject res = null;
        try {
            res = object.getJSONObject("retweeted_status");
        } catch (JSONException e) {

        }
        return res;
    }

    private JSONObject parseEntities(JSONObject object) {
        JSONObject res = null;

        try {
            res = object.getJSONObject("entities");
        } catch (JSONException e) {
        }

        return res;
    }

    private String parseContextText(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("text");
    }

    private String parseMediaUrl(JSONObject mediaObject) throws JSONException {
        if (Build.VERSION.SDK_INT < 28)
            return mediaObject.getString("media_url");
        return mediaObject.getString("media_url_https");
    }

    private JSONObject parserUserObject(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("user");
    }

    @Override
    public UserProfile parseUserProfile(JSONObject jsonObject) throws JSONException {

        long id = parseID(jsonObject);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            return new UserProfile.Builder(id, jsonObject.getString( "name")).profileImage(jsonObject.getString("profile_image_url")).build();
        return new UserProfile.Builder(id, jsonObject.getString("name")).profileImage(jsonObject.getString("profile_image_url_https")).build();
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
}
