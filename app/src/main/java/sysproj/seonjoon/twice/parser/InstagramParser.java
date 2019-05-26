package sysproj.seonjoon.twice.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.InstagramPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.entity.UserProfile;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class InstagramParser extends SNSParser {

    private static final String TAG = "InsatagramParser";

    @Override
    public ArrayList<Post> parseTimeline(JSONObject object) {
        if (object == null)
            return null;

        ArrayList<Post> result = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                int snsType = SNSTag.Instagram * SNSTag.Platform;

                JSONObject data = dataArray.getJSONObject(i);

                int commentCount = parseCommentCount(data);
                long createdTime = parseCreatedTime(data);
                String type = parseType(data);
                int likesCount = parseLikeCount(data);
                long id = parseID(data);
                JSONObject caption = parseCaption(data);
                String contents = caption != null ? parseContent(caption) : "";
                String location = parseLocation(data);
                ArrayList<PostMedia> postMedia = new ArrayList<>();

                //Log.e(TAG, (i + 1) + " : " + type);

                switch (type) {
                    case "video":
                        snsType += SNSTag.Video;
                        postMedia.add(parseVideo(data));
                        break;
                    case "image":
                        snsType += SNSTag.Image;
                        postMedia.add(parseImage(data));
                        break;
                    case "carousel":
                        snsType += SNSTag.Carousel;
                        postMedia = parseCarousel(data);
                        break;
                }

                PostRFS rfs = new PostRFS(commentCount, likesCount, 0);

                result.add(new InstagramPost.Builder(id, snsType, UserSession.InstagramProfile, contents, Long.toString(createdTime), rfs)
                        .location(location)
                        .imageList(postMedia)
                        .build());
            }


        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage());
            Log.e(TAG, e.toString());
        }

        return result;
    }

    @Override
    public ArrayList<Post> parseSearch(JSONObject object) {
        return null;
    }

    @Override
    public UserProfile parseUserProfile(JSONObject jsonObject) {

        JSONObject dataObject = null;
        String userName = "Unknown";
        String profileImage = null;
        try {
            dataObject = jsonObject.getJSONObject("data");

            userName = dataObject.getString("username");
            profileImage = dataObject.getString("profile_picture");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new UserProfile.Builder(userName).profileImage(profileImage).build();
    }

    @Override
    protected String parseCreatedDate(JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    protected long parseID(JSONObject jsonObject) throws JSONException {
        return Long.parseLong(jsonObject.getString("id").split("_")[0]);
    }

    private int parseCommentCount(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("comments").getInt("count");
    }

    private int parseLikeCount(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("likes").getInt("count");
    }

    private JSONObject parseCaption(JSONObject jsonObject) throws JSONException {
        return jsonObject.isNull("caption") ? null : jsonObject.getJSONObject("caption");
    }

    private String parseType(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("type");
    }

    private long parseCreatedTime(JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong("created_time");
    }

    private String parseContent(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("text");
    }

    private String parseUrl(JSONObject jsonObject) throws JSONException {
        JSONObject solution = jsonObject.getJSONObject("standard_resolution");

        return solution.getString("url");
    }

    private PostMedia parseImage(JSONObject jsonObject) throws JSONException {
        JSONObject images = jsonObject.getJSONObject("images");

        return new PostMedia(PostMedia.PHOTO, "", parseUrl(images));
    }

    private PostMedia parseVideo(JSONObject jsonObject) throws JSONException {
        JSONObject images = jsonObject.getJSONObject("images");

        return new PostMedia(PostMedia.VIDEO, "", parseUrl(images));
    }

    private ArrayList<PostMedia> parseCarousel(JSONObject jsonObject) throws JSONException {

        JSONArray carousel = jsonObject.getJSONArray("carousel_media");

        ArrayList<PostMedia> resultList = new ArrayList<>();

        for (int i = 0; i < carousel.length(); i++) {
            JSONObject media = carousel.getJSONObject(i);
            String type = parseType(media);

            switch (type) {
                case "image":
                    resultList.add(parseImage(media));
                    break;
                case "video":
                    resultList.add(parseVideo(media));
                    break;
            }

        }

        return resultList;
    }

    private String parseLocation(JSONObject jsonObject) throws JSONException {
        return jsonObject.isNull("location") ? null : jsonObject.getJSONObject("location").getString("name");
    }
}
